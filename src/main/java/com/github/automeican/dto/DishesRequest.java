package com.github.automeican.dto;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName DishesRequest
 * @Description
 * @Author liyongbing
 * @Date 2022/9/23 11:28
 * @Version 1.0
 **/
@Data
public class DishesRequest extends BaseRequest {
    private String tabUniqueId;
    private Date targetTime;
}
