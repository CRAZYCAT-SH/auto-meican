package com.github.automeican.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author liyongbing
 * @since 2022-09-27
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@TableName("meican_dish")
@ApiModel(value = "MeicanDish对象", description = "")
public class MeicanDish implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ASSIGN_ID)
    private Long uid;

    private String accountName;

    private String orderDate;

    private String orderDish;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @TableLogic
    private Integer deleted;


}
