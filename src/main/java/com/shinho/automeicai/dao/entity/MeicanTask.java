package com.shinho.automeicai.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author liyongbing
 * @since 2022-09-22
 */
@ToString
@Getter
@Setter
@TableName("meican_task")
@ApiModel(value = "MeicanTask对象", description = "")
public class MeicanTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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
