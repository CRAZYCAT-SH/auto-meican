package com.shinho.automeican.remote.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shinho.automeican.dto.CalendarItemsRequest;
import com.shinho.automeican.dto.CalendarItemsResponse;
import com.shinho.automeican.remote.AbstractMeicanExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName CalendarItemsExecutor
 * @Description
 * @Author liyongbing
 * @Date 2022/9/22 18:03
 * @Version 1.0
 **/
@Slf4j
@Component
public class CalendarItemsExecutor extends AbstractMeicanExecutor<CalendarItemsRequest, List<CalendarItemsResponse>> {

    @Override
    protected List<CalendarItemsResponse> parseResult(String body) {
        List<JSONObject> calendarItemList = Optional.of(body)
                .map(JSON::parseObject)
                .map(e -> e.getJSONArray("dateList"))
                .map(Collection::stream)
                .get()
                .map(e -> ((JSONObject) JSONObject.toJSON(e)).getJSONArray("calendarItemList"))
                .flatMap(Collection::stream)
                .map(e -> (JSONObject) JSONObject.toJSON(e))
                .collect(Collectors.toList());
        List<CalendarItemsResponse> result = new ArrayList<>();
        for (JSONObject calendarItem : calendarItemList) {
            CalendarItemsResponse response = new CalendarItemsResponse();
            response.setUniqueId(calendarItem.getJSONObject("userTab").getString("uniqueId"));
            response.setTargetTime(calendarItem.getDate("targetTime"));
            response.setStatus(calendarItem.getString("status"));
            response.setTitle(calendarItem.getString("title"));
            response.setName(calendarItem.getJSONObject("userTab").getString("name"));
            result.add(response);
        }
        return result;
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

}
