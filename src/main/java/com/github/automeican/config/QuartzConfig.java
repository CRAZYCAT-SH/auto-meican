package com.github.automeican.config;

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
    public JobDetail jobDetail() {
        return JobBuilder.newJob(OrderMeicanJob.class).storeDurably().build();
    }

    @Bean
    public Trigger trigger1() {
        return TriggerBuilder
                .newTrigger()
                .forJob(jobDetail())
                .withSchedule(CronScheduleBuilder.cronSchedule("30 1 0 * * ?"))
                .build();
    }

    @Bean
    public Trigger trigger2() {
        return TriggerBuilder
                .newTrigger()
                .forJob(jobDetail())
                .withSchedule(CronScheduleBuilder.cronSchedule("30 10 0 * * ?"))
                .build();
    }
}
