package com.github.automeican.job;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.automeican.common.TaskStatus;
import com.github.automeican.dao.entity.MeicanBooking;
import com.github.automeican.dao.service.IMeicanBookingService;
import com.github.automeican.remote.MeicanClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @ClassName OrderMeicanJob
 * @Description
 * @Author liyongbing
 * @Date 2022/9/23 16:08
 * @Version 1.0
 **/
@AllArgsConstructor
@Slf4j
public class OrderMeicanJob extends QuartzJobBean {
    private IMeicanBookingService meicanBookingService;
    private MeicanClient meicanClient;


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("================>>> OrderMeicanJob start");
        String today = LocalDate.now().toString();
        List<MeicanBooking> list = meicanBookingService.list(Wrappers.<MeicanBooking>lambdaQuery()
                .eq(MeicanBooking::getOrderDate, today)
                .ne(MeicanBooking::getOrderStatus, TaskStatus.SUCCESS.name())
        );
        if (CollectionUtils.isNotEmpty(list)) {
            for (MeicanBooking meicanTask : list) {
                try {
                    String orderDish = meicanTask.getOrderDish();
                    if (!"请假".equals(orderDish)) {// 请假直接跳过
                        meicanClient.executeTask(meicanTask);
                    }
                    meicanTask.setOrderStatus(TaskStatus.SUCCESS.name());
                    meicanTask.setErrorMsg("success");
                } catch (Exception e) {
                    log.error("OrderMeicanJob error",e);
                    meicanTask.setOrderStatus(TaskStatus.FAIL.name());
                    meicanTask.setErrorMsg(e.getMessage());
                }
                meicanTask.setTryCount(meicanTask.getTryCount() + 1);
                meicanTask.setUpdateDate(new Date());
            }
            meicanBookingService.updateBatchById(list);
        }
        log.info("================>>> OrderMeicanJob end");
    }
}
