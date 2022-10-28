package com.github.automeican.config;

import com.github.automeican.job.OrderDishCheckJob;
import com.github.automeican.job.OrderMeicanJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName QuartzConfig
 * @Description
 * @Author liyongbing
 * @Date 2022/9/23 16:12
 * @Version 1.0
 **/
@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail orderMeicanJob() {
        return JobBuilder.newJob(OrderMeicanJob.class).storeDurably().build();
    }

    @Bean
    public JobDetail orderDishCheckJob() {
        return JobBuilder.newJob(OrderDishCheckJob.class).storeDurably().build();
    }

    @Bean
    public Trigger trigger1() {
        return TriggerBuilder
                .newTrigger()
                .forJob(orderMeicanJob())
                .withSchedule(CronScheduleBuilder.cronSchedule("30 1 0 * * ?"))
                .build();
    }

    @Bean
    public Trigger trigger2() {
        return TriggerBuilder
                .newTrigger()
                .forJob(orderMeicanJob())
                .withSchedule(CronScheduleBuilder.cronSchedule("30 10 0 * * ?"))
                .build();
    }

    @Bean
    public Trigger trigger3() {
        return TriggerBuilder
                .newTrigger()
                .forJob(orderDishCheckJob())
                .withSchedule(CronScheduleBuilder.cronSchedule("30 20 0 * * ?"))
                .build();
    }

    @Bean
    public Trigger trigger4() {
        return TriggerBuilder
                .newTrigger()
                .forJob(orderMeicanJob())
                .withSchedule(CronScheduleBuilder.cronSchedule("30 30 0 * * ?"))
                .build();
    }
}
