package com.github.automeican.dao.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.automeican.common.TaskStatus;
import com.github.automeican.dao.entity.MeicanBooking;
import com.github.automeican.dao.mapper.MeicanBookingMapper;
import com.github.automeican.dao.service.IMeicanBookingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liyongbing
 * @since 2022-09-26
 */
@Service
public class MeicanBookingServiceImpl extends ServiceImpl<MeicanBookingMapper, MeicanBooking> implements IMeicanBookingService {

    @Override
    public List<String> recentSuccessBooking(String accountName, int limit) {
        return list(Wrappers.<MeicanBooking>lambdaQuery()
                .eq(MeicanBooking::getAccountName, accountName)
                .eq(MeicanBooking::getOrderStatus, TaskStatus.SUCCESS.name())
                .orderByDesc(MeicanBooking::getOrderDate)
                .last(" limit "+limit)
        ).stream().map(MeicanBooking::getOrderDish).toList();
    }
}
