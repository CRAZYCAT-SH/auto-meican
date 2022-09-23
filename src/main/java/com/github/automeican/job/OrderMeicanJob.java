package com.github.automeican.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.automeican.common.TaskStatus;
import com.github.automeican.dao.entity.MeicanTask;
import com.github.automeican.dao.service.IMeicanTaskService;
import com.github.automeican.remote.MeicanClient;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * @ClassName OrderMeicanJob
 * @Description
 * @Author liyongbing
 * @Date 2022/9/23 16:08
 * @Version 1.0
 **/
@Slf4j
@Component
public class OrderMeicanJob extends QuartzJobBean {
    @Resource
    private IMeicanTaskService meicanTaskService;

    @Resource
    private MeicanClient meicanClient;


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("================>>> OrderMeicanJob start");
        String today = LocalDate.now().toString();
        List<MeicanTask> list = meicanTaskService.list(Wrappers.<MeicanTask>lambdaQuery()
                .eq(MeicanTask::getOrderDate, today)
                .ne(MeicanTask::getOrderStatus, TaskStatus.SUCCESS.name())
        );
        for (MeicanTask meicanTask : list) {
            try {
                meicanClient.executeTask(meicanTask);
                meicanTask.setOrderStatus(TaskStatus.SUCCESS.name());
            } catch (Exception e) {
                meicanTask.setOrderStatus(TaskStatus.FAIL.name());
            }
        }
        meicanTaskService.updateBatchById(list);
        log.info("================>>> OrderMeicanJob end");
    }
}
