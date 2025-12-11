package com.github.automeican.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.automeican.common.BigIdSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author liyongbing
 * @since 2022-10-28
 */

@Builder
@Getter
@Setter
@TableName("meican_account_dish_check")
public class MeicanAccountDishCheck implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonSerialize(using = BigIdSerializer.class)
    @TableId(type = IdType.ASSIGN_ID)
    private Long uid;

    private String accountName;

    private String expireDate;

    private String noOrderDishes;

    private Date createDate;

    private Date updateDate;

    @TableLogic
    private Integer deleted;


}
