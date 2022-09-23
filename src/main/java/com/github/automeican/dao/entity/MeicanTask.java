package com.github.automeican.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author liyongbing
 * @since 2022-09-22
 */
@Data
@TableName("meican_task")
@ApiModel(value = "MeicanTask对象", description = "")
public class MeicanTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long uid;

    private String meicanAccountName;

    private String meicanAccountPassword;

    private String orderName;

    private String orderDate;

    private String orderDish;

    private String orderStatus;

    private Date createDate;

    private Date updateDate;

    @TableLogic
    private Integer deleted;


}
