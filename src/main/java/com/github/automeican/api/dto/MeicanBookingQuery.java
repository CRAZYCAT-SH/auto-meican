package com.github.automeican.api.dto;

import com.github.automeican.dao.entity.MeicanBooking;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName MeicanBookingQuery
 * @Description
 * @Author liyongbing
 * @Date 2022/9/23 16:26
 * @Version 1.0
 **/
@NoArgsConstructor
@Data
public class MeicanBookingQuery extends MeicanBooking implements Serializable {
    private int pageNo;
    private int pageSize;
}
