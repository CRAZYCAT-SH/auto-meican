package com.github.automeican.dto;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName RestaurantsListRequest
 * @Description
 * @Author liyongbing
 * @Date 2022/9/23 10:14
 * @Version 1.0
 **/
@Data
public class RestaurantsListRequest extends BaseRequest {
    private String tabUniqueId;
    private Date targetTime;
}
