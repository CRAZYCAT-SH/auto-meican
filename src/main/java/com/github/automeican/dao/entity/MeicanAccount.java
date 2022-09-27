package com.github.automeican.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.automeican.common.BigIdSerializer;
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
@TableName("meican_account")
@ApiModel(value = "MeicanAccount对象", description = "")
public class MeicanAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = BigIdSerializer.class)
    @TableId(type = IdType.ASSIGN_ID)
    private Long uid;

    private String accountName;

    private String accountPassword;

    private String accountCookie;

    private Date createDate;

    private Date updateDate;

    @TableLogic
    private Integer deleted;


}
