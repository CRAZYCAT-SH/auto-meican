package com.shinho.automeicai.dto;

import lombok.Data;

/**
 * @ClassName CalendarItemsRequest
 * @Description
 * @Author liyongbing
 * @Date 2022/9/22 18:03
 * @Version 1.0
 **/
@Data
public class CalendarItemsRequest extends BaseRequest {
    private String beginDate;
    private String endDate;
}
