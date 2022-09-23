package com.github.automeican.dto;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName CalendarItemsResponse
 * @Description
 * @Author liyongbing
 * @Date 2022/9/23 10:49
 * @Version 1.0
 **/
@Data
public class CalendarItemsResponse {
    private String uniqueId;
    private Date targetTime;
    private String status;
    private String title;
    private String name;
}
