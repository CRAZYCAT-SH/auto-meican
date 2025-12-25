package com.github.automeican.dto;

import lombok.Data;

/**
 * @ClassName DishesResponse
 * @Description
 * @Author liyongbing
 * @Date 2022/9/23 11:29
 * @Version 1.0
 **/
@Data
public class DishesResponse {
    //{"myRegularDishList":
    // [{
    // "dishSectionId":0,
    // "id":215465245,
    // "isSection":false,
    // "name":"菠萝鸡腿堡套餐(配香辣鸡翅2个60g\u0026魔法鸡块6块60g\u0026饮料/欣和专供 )",
    // "originalPriceInCent":3800,
    // "priceInCent":3800,
    // "priceString":"38",
    // "restaurant":
    // {
    // "name":"德克士(纪翟路店)",
    // "uniqueId":"360148",
    // "available":true}}],"othersRegularDishList":[{"dishSectionId":0,"id":215465248,"isSection":false,"name":"香煎脆皮鸡腿饭套餐(配香辣鸡翅2个60g\u0026辣味啃骨鸡1份60g\u0026饮料/欣和专供 )","originalPriceInCent":3800,"priceInCent":3800,"priceString":"38","restaurant":{"name":"德克士(纪翟路店)","uniqueId":"360148","available":true}},{"dishSectionId":0,"id":215465247,"isSection":false,"name":"脆皮手枪腿套餐(配德克士鸡块6块80g\u0026孜然鸡柳6根60g\u0026饮料/欣和专供 )","originalPriceInCent":3800,"priceInCent":3800,"priceString":"38","restaurant":{"name":"德克士(纪翟路店)","uniqueId":"360148","available":true}},{"dishSectionId":0,"id":215465243,"isSection":false,"name":"柠香鸡腿堡套餐(配脆皮炸鸡1个110g\u0026德克士鸡块6块6块80g\u0026饮料/欣和专供 )","originalPriceInCent":3800,"priceInCent":3800,"priceString":"38","restaurant":{"name":"德克士(纪翟路店)","uniqueId":"360148","available":true}},{"dishSectionId":0,"id":215465244,"isSection":false,"name":"椒香鸡腿堡套餐(微辣/配香辣鸡翅2个60g\u0026孜然鸡柳6根60g\u0026饮料/欣和专供  )","originalPriceInCent":3800,"priceInCent":3800,"priceString":"38","restaurant":{"name":"德克士(纪翟路店)","uniqueId":"360148","available":true}},{"dishSectionId":0,"id":215465246,"isSection":false,"name":"南阳炸鸡卷套餐(配脆皮炸鸡1个110g\u0026辣味啃骨鸡1份60g\u0026饮料/欣和专供 )","originalPriceInCent":3800,"priceInCent":3800,"priceString":"38","restaurant":{"name":"德克士(纪翟路店)","uniqueId":"360148","available":true}}],"othersRegularDishListSource":"TODAY_ORDER","showPrice":false}
    private String dishSectionId;
    private String id;
    private Boolean isSection;
    private String name;
    private String originalPriceInCent;
    private String priceInCent;
    private String priceString;
    private Restaurant restaurant;

    @Data
    public static class Restaurant {
        private String name;
        private String uniqueId;
        private Boolean open;
    }
}
