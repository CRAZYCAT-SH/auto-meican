package com.github.automeican.remote.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.automeican.dto.DishesResponse;
import com.github.automeican.dto.RestaurantsListResponse;
import com.github.automeican.dto.RestaurantsShowRequest;
import com.github.automeican.remote.AbstractMeicanExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.sqlite.date.DateFormatUtils;

import java.util.*;

/**
 * @ClassName RestaurantsShowExecutor
 * @Description
 * @Author liyongbing
 * @Date 2022/9/28 10:10
 * @Version 1.0
 **/
@Slf4j
@Component
public class RestaurantsShowExecutor extends AbstractMeicanExecutor<RestaurantsShowRequest, List<DishesResponse>> {
    @Override
    protected List<DishesResponse> parseResult(String body) {
        return Optional.of(body)
                .map(JSONObject::parseObject)
                .map(e -> e.getJSONArray("dishList"))
                .map(e -> e.toJavaList(DishesResponse.class))
                .orElse(Collections.emptyList());
    }

    @Override
    protected HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    protected String getPath() {
        return "/forward/api/v2.1/restaurants/show";
    }

    @Override
    protected Map<String, String> prepareUrlParams(RestaurantsShowRequest param) {
        Map<String, String> params = new HashMap<>();
        params.put("tabUniqueId", param.getTabUniqueId());
        params.put("targetTime", DateFormatUtils.format(param.getTargetTime(), "yyyy-MM-dd+HH:mm"));
        params.put("restaurantUniqueId",param.getRestaurantUniqueId());
        return params;
    }
}
