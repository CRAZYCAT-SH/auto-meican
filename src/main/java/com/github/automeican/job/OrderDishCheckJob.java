package com.github.automeican.job;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.automeican.common.DishRecommender;
import com.github.automeican.common.TaskStatus;
import com.github.automeican.dao.entity.MeicanAccountDishCheck;
import com.github.automeican.dao.entity.MeicanBooking;
import com.github.automeican.dao.service.IMeicanAccountDishCheckService;
import com.github.automeican.dao.service.IMeicanBookingService;
import com.github.automeican.dto.AiDishResult;
import com.github.automeican.dto.UserPreference;
import com.github.automeican.remote.MeicanClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

/**
 * @ClassName OrderDishCheckJob
 * @Description 点餐检查
 * 若当日未点餐，则在配置的过期时间内帮用户随机挑选当日菜品自动下单
 * @Author liyongbing
 * @Date 2022/10/28 16:06
 * @Version 1.0
 **/

@AllArgsConstructor
@Slf4j
public class OrderDishCheckJob extends QuartzJobBean {
    private static final Random RANDOM = new Random();
    private MeicanClient meicanClient;
    private IMeicanAccountDishCheckService meicanAccountDishCheckService;
    private IMeicanBookingService meicanBookingService;
    private DishRecommender dishRecommender;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String today = LocalDate.now().toString();
        List<MeicanAccountDishCheck> dishChecks = meicanAccountDishCheckService.list();
        for (MeicanAccountDishCheck dishCheck : dishChecks) {
            String expireDate = dishCheck.getExpireDate();
            String accountName = dishCheck.getAccountName();
            final List<String> blackDishes = convertSettings(dishCheck.getNoOrderDishes());
            LocalDate expire = LocalDate.parse(expireDate);
            if (expire.isBefore(LocalDate.now())) {//设置点餐检查已过期
                continue;
            }
            long count = meicanBookingService.count(Wrappers.<MeicanBooking>lambdaQuery()
                    .eq(MeicanBooking::getAccountName, accountName)
                    .eq(MeicanBooking::getOrderDate, today)
            );
            if (count > 0) {//今日已点餐
                continue;
            }
            List<String> dishList = meicanClient.currentDishList(accountName, today);
            if (CollectionUtils.isEmpty(dishList)) {//今天没菜
                continue;
            }
            AiDishResult aiDishResult = aiSelectDish(today, dishCheck);
            if (aiDishResult != null && StringUtils.isNotEmpty(aiDishResult.getDishName())) {
                meicanBookingService.save(MeicanBooking.builder()
                        .accountName(accountName)
                        .orderDate(today)
                        .orderDish("[auto]"+aiDishResult.getDishName())
                        .selectReason(aiDishResult.getSelectReason())
                        .orderStatus(TaskStatus.INIT.name())
                        .createDate(new Date())
                        .updateDate(new Date())
                        .build()
                );
                return;
            }
            String dish = dishList.get(RANDOM.nextInt(dishList.size()));//随机点菜
            while (matchAny(blackDishes,dish)){
                dish = dishList.get(RANDOM.nextInt(dishList.size()));//随机点菜
            }
            meicanBookingService.save(MeicanBooking.builder()
                    .accountName(accountName)
                    .orderDate(today)
                    .orderDish("[auto]"+dish)
                    .orderStatus(TaskStatus.INIT.name())
                    .createDate(new Date())
                    .updateDate(new Date())
                    .build()
            );
        }
    }

    private AiDishResult aiSelectDish(String today, MeicanAccountDishCheck dishCheck) {
        List<String> meicanBookings = meicanBookingService.recentSuccessBooking(dishCheck.getAccountName(), 5);
        UserPreference userPreference = UserPreference.builder()
                .accountName(dishCheck.getAccountName())
                .orderDate(today)
                .likes(convertSettings(dishCheck.getLikes()))
                .restrictions(convertSettings(dishCheck.getRestrictions()))
                .blacklist(convertSettings(dishCheck.getNoOrderDishes()))
                .recentDishes(meicanBookings)
                .build();
        try {
            return dishRecommender.recommend(userPreference);
        } catch (Exception e) {
            log.error("推荐菜失败",e);
        }
        return null;
    }

    private static List<String> convertSettings(String settings) {
        return Optional.ofNullable(settings)
                .map(e -> e.replace("，", ","))
                .map(e -> e.replace("\n", ","))
                .map(e -> e.split(","))
                .map(Arrays::stream)
                .map(Stream::toList)
                .orElse(Collections.emptyList());
    }

    private boolean matchAny(Collection<String> blackDishes, String dish) {
        for (String blackDish : blackDishes) {
            if (dish.contains(blackDish)) {
                return true;
            }
        }
        return false;
    }
}
