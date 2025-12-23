package com.github.automeican.common;

import com.alibaba.fastjson.JSON;
import com.github.automeican.dto.AiDishResult;
import com.github.automeican.dto.UserPreference;
import com.github.automeican.remote.MeicanClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.ResponseFormat;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DishRecommender {

    private static final Logger log = LoggerFactory.getLogger(DishRecommender.class);

    private final DeepSeekChatModel chatModel;

    private final MeicanClient meicanClient;

    private final FixedWindowRateLimiter fixedWindowRateLimiter;

    // 初始化Prompt模板
    private static final String SYSTEM_PROMPT = """
            你是一个智能点餐助手，需要根据以下规则处理：
            1. 绝对排除用户黑名单中的菜品
            2. 优先推荐用户喜欢的类别(辣度/食材等)
            3. 考虑用户忌口
            最终返回JSON格式，包含推荐菜品名称和过滤原因，请以“dishName”和“selectReason”字段输出它们
            """;


    public AiDishResult recommend(UserPreference preference) {
        log.info("preference:[{}]", preference);
        if (fixedWindowRateLimiter.triggerLimit(preference.getAccountName(), 5L, 60L)) {
            AiDishResult result = new AiDishResult();
            result.setDishName("");
            result.setSelectReason("触发限流");
            return result;
        }
        // 1. 配置选项
        var options = DeepSeekChatOptions.builder()
                .model("deepseek-chat")
                .responseFormat(ResponseFormat.builder().type(ResponseFormat.Type.JSON_OBJECT).build())
                .toolCallbacks(List.of(
                        FunctionToolCallback
                                .builder("getDishList",
                                        (UserPreference p) -> meicanClient.currentDishList(p.getAccountName(), p.getOrderDate())
                                )
                                .inputType(UserPreference.class)
                                .description("获取当前可用的菜品列表")
                                .build()
                ))
                .build();
        // 1. 构建Prompt
        Prompt prompt = new Prompt(
                List.of(new SystemMessage(SYSTEM_PROMPT), new UserMessage(preferenceToPrompt(preference))),
                options
        );

        // 2. 执行AI调用
        ChatResponse response = chatModel.call(prompt);
        log.info("response:[{}]", response);
        String text = response.getResults().get(0).getOutput().getText();
        return JSON.parseObject(text, AiDishResult.class);
    }

    private String preferenceToPrompt(UserPreference pref) {
        return String.format("""
                        用户名：%s
                        日期：%s
                        用户偏好：%s
                        忌口：%s
                        黑名单：%s
                        请调用函数查询并返回推荐最匹配的一个结果""",
                pref.getAccountName(),
                pref.getOrderDate(),
                String.join(",", pref.getLikes()),
                String.join(",", pref.getRestrictions()),
                String.join(",", pref.getBlacklist())
        );
    }
}
