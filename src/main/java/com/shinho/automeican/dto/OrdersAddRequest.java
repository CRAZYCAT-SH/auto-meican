package com.shinho.automeican.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName OrdersAddRequest
 * @Description
 * @Author liyongbing
 * @Date 2022/9/23 14:19
 * @Version 1.0
 **/
@Data
public class OrdersAddRequest extends BaseRequest {

    //tabUniqueId=3c059203-bd15-4918-a797-035cf5386abe&
    // order=[{"count":1,"dishId":215465243}]&
    // remarks=[{"dishId":"215465243","remark":""}]&
    // targetTime=2022-09-22 10:00&
    // userAddressUniqueId=9a685dc748e5&
    // corpAddressUniqueId=9a685dc748e5&
    // corpAddressRemark=
    private String tabUniqueId;
    private List<Order> order;
    private List<Remark> remarks;
    private Date targetTime;
    private String corpAddressRemark;

    @Builder
    @Data
    public static class Order {
        private Integer count;
        private String dishId;
    }

    @Builder
    @Data
    public static class Remark {
        private String remark;
        private String dishId;
    }
}
