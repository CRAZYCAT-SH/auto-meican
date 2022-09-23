package com.github.automeican.remote.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.automeican.remote.AbstractMeicanExecutor;
import com.github.automeican.dto.CartUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * @ClassName CartUpdateExecutor
 * @Description
 * @Author liyongbing
 * @Date 2022/9/23 13:54
 * @Version 1.0
 **/
@Slf4j
@Component
public class CartUpdateExecutor extends AbstractMeicanExecutor<CartUpdateRequest, JSONObject> {
    @Override
    protected JSONObject parseResult(String body) {
        return JSON.parseObject(body);
    }

    @Override
    protected HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected String getPath() {
        return "/forward/api/preorder/cart/update";
    }

    @Override
    protected Map<String, String> prepareUrlParams(CartUpdateRequest param) {
        return null;
    }

    @Override
    protected void prepareHeaders(MultiValueMap<String, String> headers) {
        headers.add("Content-Type","application/json");
    }

    @Override
    protected String prepareBodyParam(CartUpdateRequest param) {
        JSONObject json = new JSONObject();
        json.put("dishes",param.getDishes());
        json.put("corpName",param.getCorpName());
        json.put("tabUUID",param.getTabUUID());
        json.put("tabName",param.getTabName());
        json.put("operativeDate",param.getOperativeDate());
        JSONObject result = new JSONObject();
        String key = param.getTabUUID()+"/"+param.getOperativeDate()+" 10:00";
        result.put(key,json);
        return result.toJSONString();
    }
}
