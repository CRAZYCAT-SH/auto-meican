package com.github.automeican.job;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.automeican.common.TaskStatus;
import com.github.automeican.dao.entity.MeicanAccountDishCheck;
import com.github.automeican.dao.entity.MeicanBooking;
import com.github.automeican.dao.service.IMeicanAccountDishCheckService;
import com.github.automeican.dao.service.IMeicanBookingService;
import com.github.automeican.remote.MeicanClient;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @ClassName OrderDishCheckJob
 * @Description 点餐检查
 * 若当日未点餐，则在配置的过期时间内帮用户随机挑选当日菜品自动下单
 * @Author liyongbing
 * @Date 2022/10/28 16:06
 * @Version 1.0
 **/
@Slf4j
@Component
public class OrderDishCheckJob extends QuartzJobBean {
    private Random random = new Random();


    @Resource
    private MeicanClient meicanClient;
    @Resource
    private IMeicanAccountDishCheckService meicanAccountDishCheckService;
    @Resource
    private IMeicanBookingService meicanBookingService;


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String today = LocalDate.now().toString();
        List<MeicanAccountDishCheck> dishChecks = meicanAccountDishCheckService.list();
        for (MeicanAccountDishCheck dishCheck : dishChecks) {
            String expireDate = dishCheck.getExpireDate();
            String accountName = dishCheck.getAccountName();
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
            String dish = dishList.get(random.nextInt(dishList.size()));//随机点菜
            meicanBookingService.save(MeicanBooking.builder()
                    .accountName(accountName)
                    .orderDate(today)
                    .orderDish(dish)
                    .orderStatus(TaskStatus.INIT.name())
                    .createDate(new Date())
                    .updateDate(new Date())
                    .build()
            );
        }
    }
}
