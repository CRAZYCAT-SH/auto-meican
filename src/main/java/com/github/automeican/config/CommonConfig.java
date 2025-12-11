package com.github.automeican.config;

import com.github.automeican.mcp.AccountService;
import com.github.automeican.mcp.DishService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName CommonConfig
 * @Description
 * @Author liyongbing
 * @Date 2022/9/22 17:12
 * @Version 1.0
 **/
@Configuration
public class CommonConfig {
    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
//        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(30000);  // 单位为ms
        factory.setConnectTimeout(30000);  // 单位为ms
        return factory;
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }


    @Bean
    public ToolCallbackProvider toolCallbackProvider(AccountService accountService) {
        return MethodToolCallbackProvider.builder().toolObjects(accountService).build();
    }

    @Bean
    public ToolCallbackProvider toolCallbackProvider2(DishService dishService) {
        return MethodToolCallbackProvider.builder().toolObjects(dishService).build();
    }

}
