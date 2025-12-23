package com.github.automeican.dao.service;

import com.github.automeican.dao.entity.MeicanBooking;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liyongbing
 * @since 2022-09-26
 */
public interface IMeicanBookingService extends IService<MeicanBooking> {

    List<String> recentSuccessBooking(String accountName, int limit);

}
