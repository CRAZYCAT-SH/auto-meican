package com.github.automeican.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author liyongbing
 * @since 2022-09-26
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@TableName("meican_booking")
@ApiModel(value = "MeicanBooking对象", description = "")
public class MeicanBooking implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long uid;

    private String accountName;

    private String orderDate;

    private String orderDish;

    private String orderStatus;

    private Integer tryCount;

    private String errorMsg;

    private Date createDate;

    private Date updateDate;

    @TableLogic
    private Integer deleted;


}
