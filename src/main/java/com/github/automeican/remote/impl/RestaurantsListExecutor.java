package com.github.automeican.remote.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.automeican.dto.RestaurantsListRequest;
import com.github.automeican.dto.RestaurantsListResponse;
import com.github.automeican.remote.AbstractMeicanExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.sqlite.date.DateFormatUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @ClassName RestaurantsListExecutor
 * @Description
 * @Author liyongbing
 * @Date 2022/9/23 10:12
 * @Version 1.0
 **/
@Slf4j
@Component
public class RestaurantsListExecutor extends AbstractMeicanExecutor<RestaurantsListRequest, List<RestaurantsListResponse>> {

    @Override
    protected List<RestaurantsListResponse> parseResult(String body) {
        return Optional.of(body)
                .map(JSONObject::parseObject)
                .map(e -> e.getJSONArray("restaurantList").toJavaList(RestaurantsListResponse.class))
                .get();
    }

    @Override
    protected HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    protected String getPath() {
        return "/forward/api/v2.1/restaurants/list";
    }

    @Override
    protected Map<String, String> prepareUrlParams(RestaurantsListRequest param) {
        Map<String, String> params = new HashMap<>();
        params.put("tabUniqueId", param.getTabUniqueId());
        params.put("targetTime", DateFormatUtils.format(param.getTargetTime(), "yyyy-MM-dd+HH:mm"));
        return params;
    }
}
