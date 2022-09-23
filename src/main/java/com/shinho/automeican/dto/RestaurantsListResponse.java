package com.shinho.automeican.dto;

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
public class RestaurantsListResponse extends BaseRequest {
    private String uniqueId;//":"58b187",
    private String name;//":"鑫旺饭庄",
    private String tel;//":"15901830138",
    private String rating;//":3,
    private Integer minimumOrder;//":0,
    private Double latitude;//":31.280041,
    private Double longitude;//":121.482914,
    private String warning;//":"",
    private String openingTime;//":"",
    private Boolean onlinePayment;//":true,
    private Boolean open;//":false,
    private Integer availableDishCount;//":0,
    private Integer dishLimit;//":70,
    private Integer restaurantStatus;//":2,
    private Boolean remarkEnabled;//":false
}
