package com.github.automeican.remote;

import com.github.automeican.config.MeicanConfigProperties;
import com.github.automeican.dto.BaseRequest;
import com.github.automeican.utils.ConvertUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.Resource;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @ClassName AbstractMeicanExecutor
 * @Description 美餐请求抽象模板
 * @Author liyongbing
 * @Date 2022/9/22 15:55
 * @Version 1.0
 **/
@Slf4j
@Component
public abstract class AbstractMeicanExecutor<T extends BaseRequest, D> implements Executor<T, D> {

    @Resource
    protected MeicanConfigProperties configProperties;
    @Resource
    protected RestTemplate restTemplate;
    @Resource
    protected AuthService authService;


    @SneakyThrows
    @Override
    public D execute(T param) {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Accept", "application/json, text/plain, */*");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");
        headers.add("Cache-Control", "no-cache");
        headers.add("Connection", "keep-alive");
        AuthService.AuthInfo authInfo = authService.auth(param);
        if (authInfo == null) {
            throw new RuntimeException("获取授权为空");
        }
        headers.add("Cookie", authInfo.getCookie());
        if (needToken()) {
            headers.add("Authorization", "Bearer " + authInfo.getToken());
        }
        prepareHeaders(headers);
        Map<String, String> urlParams = Optional.ofNullable(prepareUrlParams(param)).orElse(new HashMap<>());
        urlParams.put("client_id", configProperties.getClient_id());
        urlParams.put("client_secret", configProperties.getClient_secret());
        String url = configProperties.getBaseUrl() + getPath() + "?" + ConvertUtil.convertParamMap2FormStr(urlParams);
        HttpMethod httpMethod = getHttpMethod();
        String body = prepareBodyParam(param);
        RequestEntity<String> requestEntity = new RequestEntity<>(body, headers, httpMethod, new URI(url));
        log.info("========================================");
        log.info("request meican api:[{}]", requestEntity);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
            log.info("response meican api:[{}]", responseEntity);
            log.info("========================================");
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return parseResult(responseEntity.getBody());
            } else {
                throw new RuntimeException("error:" + responseEntity);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("请求美餐api异常:" + e.getMessage());
        }
    }


    protected abstract D parseResult(String body);

    protected abstract HttpMethod getHttpMethod();

    protected abstract String getPath();

    protected abstract Map<String, String> prepareUrlParams(T param);

    protected void prepareHeaders(MultiValueMap<String, String> headers) {
    }

    protected String prepareBodyParam(T param) {
        return null;
    }

    protected boolean needToken() {
        return true;
    }

}
