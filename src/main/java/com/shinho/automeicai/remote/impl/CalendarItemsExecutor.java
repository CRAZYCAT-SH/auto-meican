package com.shinho.automeicai.remote.impl;

import com.alibaba.fastjson.JSONObject;
import com.shinho.automeicai.dto.CalendarItemsRequest;
import com.shinho.automeicai.remote.AbstractMeicanExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CalendarItemsExecutor
 * @Description
 * @Author liyongbing
 * @Date 2022/9/22 18:03
 * @Version 1.0
 **/
@Slf4j
@Component
public class CalendarItemsExecutor extends AbstractMeicanExecutor<CalendarItemsRequest, JSONObject> {
    @Override
    protected Class<JSONObject> getResultClass() {
        return JSONObject.class;
    }

    @Override
    protected HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    protected String getPath() {
        return "/forward/api/v2.1/calendarItems/all";
    }

    @Override
    protected Map<String, String> prepareUrlParams(CalendarItemsRequest param) {
        Map<String, String> params = new HashMap<>();
        params.put("withOrderDetail", "false");
        params.put("beginDate", param.getBeginDate());
        params.put("endDate", param.getEndDate());
        return params;
    }

    @Override
    protected void prepareHeaders(MultiValueMap<String, String> headers) {

    }
}
