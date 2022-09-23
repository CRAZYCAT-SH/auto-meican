package com.shinho.automeican.remote;

import com.shinho.automeican.common.CacheManager;
import com.shinho.automeican.config.MeicanConfigProperties;
import com.shinho.automeican.dto.BaseRequest;
import com.shinho.automeican.dto.TokenResponse;
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
import java.util.*;

/**
 * @ClassName TokenService
 * @Description
 * @Author liyongbing
 * @Date 2022/9/22 17:02
 * @Version 1.0
 **/
@Slf4j
@Component
public class TokenService {
    private static final CacheManager<String, String> CACHE_MANAGER = new CacheManager<>();
    @Resource
    protected MeicanConfigProperties configProperties;
    @Resource
    private RestTemplate restTemplate;

    @SneakyThrows
    public <T extends BaseRequest> String getToken(T param) {
        String username = param.getUsername();
        String token = CACHE_MANAGER.get(username);
        if (StringUtils.hasText(token)) {
            return token;
        }
        Map<String, String> form = new HashMap<>();
        form.put("username", username);
        form.put("password", param.getPassword());
        form.put("grant_type", "password");
        form.put("username_type", "username");
        form.put("meican_credential_type", "password");
        form.put("code", "");
        form.put("appSource", "");
        String body = ConvertUtil.convertParamMap2FormStr(form);
        String url = configProperties.getBaseUrl() + "/forward/api/v2.1/oauth/token?remember=true&client_id=" + configProperties.getClient_id() + "&client_secret=" + configProperties.getClient_secret();
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Accept", "application/json, text/plain, */*");
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");
        RequestEntity<String> requestEntity = new RequestEntity<>(body, headers, HttpMethod.POST, new URI(url));
        ResponseEntity<TokenResponse> responseEntity = restTemplate.exchange(requestEntity, TokenResponse.class);
        log.info("token response:[{}]",responseEntity);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            TokenResponse tokenResponse = Objects.requireNonNull(responseEntity.getBody(), "获取token body 异常");
            Integer expiresIn = tokenResponse.getExpires_in();
            Date expire = new Date(System.currentTimeMillis() + (expiresIn * 1000L) - 5000L);
            CACHE_MANAGER.put(username, tokenResponse.getAccess_token(), expire);
            return CACHE_MANAGER.get(username);
        } else {
            throw new RuntimeException("请求token异常" + responseEntity);
        }
    }


}
