package com.shinho.automeican.remote;

import com.shinho.automeican.common.CacheManager;
import com.shinho.automeican.config.MeicanConfigProperties;
import com.shinho.automeican.dto.BaseRequest;
import com.shinho.automeican.dto.TokenResponse;
import com.shinho.automeican.utils.ConvertUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
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
import java.net.URISyntaxException;
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
public class AuthService {
    private static final CacheManager<String, AuthInfo> TOKEN_CACHE_MANAGER = new CacheManager<>();
    @Resource
    protected MeicanConfigProperties configProperties;
    @Resource
    private RestTemplate restTemplate;

    @SneakyThrows
    public <T extends BaseRequest> AuthInfo auth(T param) {
        String username = param.getUsername();
        AuthInfo authInfo = TOKEN_CACHE_MANAGER.get(username);
        if (authInfo != null) {
            return authInfo;
        }
        ResponseEntity<TokenResponse> responseEntity = requestAuth(param);
        log.info("token response:[{}]", responseEntity);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            TokenResponse tokenResponse = Objects.requireNonNull(responseEntity.getBody(), "获取token body 异常");
            Integer expiresIn = tokenResponse.getExpires_in();
            Date expire = new Date(System.currentTimeMillis() + (expiresIn * 1000L) - 5000L);
            String access_token = tokenResponse.getAccess_token();
            List<String> list = responseEntity.getHeaders().get("set-cookie");
            String remember = Objects.requireNonNull(list).stream().filter(e -> e.contains("remember")).findFirst().get();
            TOKEN_CACHE_MANAGER.put(username, new AuthInfo(access_token,remember), expire);
            return TOKEN_CACHE_MANAGER.get(username);
        } else {
            throw new RuntimeException("请求token异常" + responseEntity);
        }
    }

    private <T extends BaseRequest> ResponseEntity<TokenResponse> requestAuth(T param) throws URISyntaxException {
        Map<String, String> form = new HashMap<>();
        form.put("username", param.getUsername());
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
        return restTemplate.exchange(requestEntity, TokenResponse.class);
    }

    @AllArgsConstructor
    @Data
    public static class AuthInfo {
        private String token;
        private String cookie;
    }


}
