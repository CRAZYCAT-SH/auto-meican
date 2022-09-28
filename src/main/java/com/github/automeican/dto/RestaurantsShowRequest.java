package com.github.automeican.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName RestaurantsShowRequest
 * @Description
 * @Author liyongbing
 * @Date 2022/9/28 10:12
 * @Version 1.0
 **/
@Data
public class RestaurantsShowRequest extends BaseRequest implements Serializable {
    private String tabUniqueId;
    private Date targetTime;
    private String restaurantUniqueId;
}
