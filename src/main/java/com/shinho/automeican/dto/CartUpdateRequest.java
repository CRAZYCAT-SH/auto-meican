package com.shinho.automeican.dto;

import lombok.Data;

import java.util.List;

/**
 * @ClassName CartUpdateRequest
 * @Description
 * @Author liyongbing
 * @Date 2022/9/23 13:54
 * @Version 1.0
 **/
@Data
public class CartUpdateRequest extends BaseRequest {
    //{"3c059203-bd15-4918-a797-035cf5386abe/2022-09-22 10:00":{"dishes":[{
    // "corpRestaurantId":"360148",
    // "count":1,
    // "name":"柠香鸡腿堡套餐(配脆皮炸鸡1个110g&德克士鸡块6块6块80g&饮料/欣和专供 )",
    // "priceInCent":3800,
    // "revisionId":215465243}],
    // "corpName":"欣和企业",
    // "tabUUID":"3c059203-bd15-4918-a797-035cf5386abe",
    // "tabName":"欣和企业午餐",
    // "operativeDate":"2022-09-22"}
    // }
    private List<Dish> dishes;
    private String corpName;
    private String tabUUID;
    private String tabName;
    private String operativeDate;

    @Data
    public static class Dish {
        private String corpRestaurantId;
        private String count;
        private String name;
        private String priceInCent;
        private String revisionId;
    }

}
