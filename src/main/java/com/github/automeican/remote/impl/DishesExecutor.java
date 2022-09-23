package com.github.automeican.remote.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.automeican.dto.DishesRequest;
import com.github.automeican.dto.DishesResponse;
import com.github.automeican.remote.AbstractMeicanExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.sqlite.date.DateFormatUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName DishesExecutor
 * @Description
 * @Author liyongbing
 * @Date 2022/9/23 11:28
 * @Version 1.0
 **/
@Slf4j
@Component
public class DishesExecutor extends AbstractMeicanExecutor<DishesRequest, List<DishesResponse>> {
    @Override
    protected List<DishesResponse> parseResult(String body) {
        List<DishesResponse> myRegularDishList = Optional.of(body)
                .map(JSONObject::parseObject)
                .map(e -> e.getJSONArray("myRegularDishList"))
                .map(e -> e.toJavaList(DishesResponse.class))
                .orElse(Collections.emptyList());
        List<DishesResponse> othersRegularDishList = Optional.of(body)
                .map(JSONObject::parseObject)
                .map(e -> e.getJSONArray("othersRegularDishList"))
                .map(e -> e.toJavaList(DishesResponse.class))
                .orElse(Collections.emptyList());
        return Stream.of(myRegularDishList, othersRegularDishList).flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    protected HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    protected String getPath() {
        return "/preorder/api/v2.1/recommendations/dishes";
    }

    @Override
    protected Map<String, String> prepareUrlParams(DishesRequest param) {
        Map<String, String> params = new HashMap<>();
        params.put("tabUniqueId", param.getTabUniqueId());
        params.put("targetTime", DateFormatUtils.format(param.getTargetTime(), "yyyy-MM-dd+HH:mm"));
        return params;
    }

    @Override
    protected boolean needToken() {
        return false;
    }
}
