package com.github.automeican.remote.impl;

import com.alibaba.fastjson.JSON;
import com.github.automeican.remote.AbstractMeicanExecutor;
import com.github.automeican.utils.ConvertUtil;
import com.github.automeican.dto.OrdersAddRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.sqlite.date.DateFormatUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName OrdersAddExecutor
 * @Description
 * @Author liyongbing
 * @Date 2022/9/23 14:14
 * @Version 1.0
 **/
@Slf4j
@Component
public class OrdersAddExecutor extends AbstractMeicanExecutor<OrdersAddRequest, String> {
    @Override
    protected String parseResult(String body) {
        return JSON.parseObject(body).getString("status");
    }

    @Override
    protected HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected String getPath() {
        return "/forward/api/v2.1/orders/add";
    }

    @Override
    protected Map<String, String> prepareUrlParams(OrdersAddRequest param) {
        return null;
    }

    @Override
    protected void prepareHeaders(MultiValueMap<String, String> headers) {
        headers.add("Content-Type","application/x-www-form-urlencoded");
    }

    @Override
    protected String prepareBodyParam(OrdersAddRequest param) {
        Map<String, String> params = new HashMap<>();
        params.put("tabUniqueId",param.getTabUniqueId());
        params.put("order",JSON.toJSONString(param.getOrder()));
        params.put("remarks",JSON.toJSONString(param.getRemarks()));
        params.put("targetTime", DateFormatUtils.format(param.getTargetTime(),"yyyy-MM-dd HH:mm"));
        params.put("userAddressUniqueId",configProperties.getAddressUniqueId());
        params.put("corpAddressUniqueId",configProperties.getAddressUniqueId());
        params.put("corpAddressRemark","");
        return ConvertUtil.convertParamMap2FormStr(params);
    }
}
