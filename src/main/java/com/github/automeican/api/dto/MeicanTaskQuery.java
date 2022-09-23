package com.github.automeican.api.dto;

import com.github.automeican.dao.entity.MeicanTask;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName MeicanTaskQuery
 * @Description
 * @Author liyongbing
 * @Date 2022/9/23 16:26
 * @Version 1.0
 **/
@Data
public class MeicanTaskQuery extends MeicanTask implements Serializable {
    private int pageNo;
    private int pageSize;
}
