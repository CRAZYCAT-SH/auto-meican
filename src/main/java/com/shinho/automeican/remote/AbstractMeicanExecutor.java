package com.shinho.automeican.remote;

import com.shinho.automeican.config.MeicanConfigProperties;
import com.shinho.automeican.dto.BaseRequest;
import com.shinho.automeican.utils.ConvertUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
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
    protected TokenService tokenService;


    @SneakyThrows
    @Override
    public D execute(T param) {
        String token = tokenService.getToken(param);
        if (!StringUtils.hasText(token)) {
            throw new RuntimeException("获取token 为空");
        }
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Accept", "application/json, text/plain, */*");
//        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");
        headers.add("Authorization", "Bearer " + token);
        headers.add("Cache-Control", "no-cache");
        headers.add("Connection", "keep-alive");
        prepareHeaders(headers);
        Map<String, String> urlParams = Optional.ofNullable(prepareUrlParams(param)).orElse(new HashMap<>());
        urlParams.put("client_id", configProperties.getClient_id());
        urlParams.put("client_secret", configProperties.getClient_secret());
        String url = configProperties.getBaseUrl() + getPath() + "?" + ConvertUtil.convertParamMap2FormStr(urlParams);
        HttpMethod httpMethod = getHttpMethod();
        String body = prepareBodyParam(param);
        RequestEntity<String> requestEntity = new RequestEntity<>(body, headers, httpMethod, new URI(url));
        ResponseEntity<D> responseEntity = restTemplate.exchange(requestEntity, getResultClass());
        log.info("response:[{}]", responseEntity);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            throw new RuntimeException("请求美餐api异常" + responseEntity);
        }
    }

    protected String prepareBodyParam(T param) {
        return null;
    }

    protected abstract Class<D> getResultClass();

    protected abstract HttpMethod getHttpMethod();

    protected abstract String getPath();

    protected abstract Map<String, String> prepareUrlParams(T param);

    protected abstract void prepareHeaders(MultiValueMap<String, String> headers);


}
