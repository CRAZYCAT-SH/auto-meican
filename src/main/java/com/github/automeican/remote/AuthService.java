package com.github.automeican.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.automeican.common.CacheManager;
import com.github.automeican.config.MeicanConfigProperties;
import com.github.automeican.dao.entity.MeicanAccount;
import com.github.automeican.dao.service.IMeicanAccountService;
import com.github.automeican.dto.BaseRequest;
import com.github.automeican.dto.TokenResponse;
import com.github.automeican.utils.ConvertUtil;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @Resource
    private IMeicanAccountService meicanAccountService;

    @SneakyThrows
    public <T extends BaseRequest> AuthInfo auth(T param) {
        String username = param.getUsername();
        AuthInfo authInfo = TOKEN_CACHE_MANAGER.get(username);
        if (authInfo != null) {
            return authInfo;
        }
        MeicanAccount account = meicanAccountService.getOne(Wrappers.<MeicanAccount>lambdaQuery().eq(MeicanAccount::getAccountName, username));
        if (account == null) {
            throw new RuntimeException("账号未配置，请先配置您的美餐账号");
        }
        if (StringUtils.hasText(account.getAccountCookie())) {//有cookie走cookie
            requestAuthByRemember(account);
        }else if(StringUtils.hasText(account.getAccountPassword())){//密码验证
            requestAuth(account);
        }else {
            throw new RuntimeException("账号未配置完整，请先配置您的美餐账号");
        }
        return TOKEN_CACHE_MANAGER.get(username);
    }

    private void requestAuth(MeicanAccount param) throws URISyntaxException {
        Map<String, String> form = new HashMap<>();
        form.put("username", param.getAccountName());
        form.put("password", param.getAccountPassword());
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
        log.info("token response:[{}]", responseEntity);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            TokenResponse tokenResponse = Objects.requireNonNull(responseEntity.getBody(), "获取token body 异常");
            Integer expiresIn = tokenResponse.getExpires_in();
            Date expire = new Date(System.currentTimeMillis() + (expiresIn * 1000L) - 5000L);
            String access_token = tokenResponse.getAccess_token();
            List<String> list = responseEntity.getHeaders().get("set-cookie");
            String remember = Objects.requireNonNull(list).stream().filter(e -> e.contains("remember")).findFirst().get();
            meicanAccountService.updateById(MeicanAccount.builder().uid(param.getUid()).accountCookie(remember).updateDate(new Date()).build());
            TOKEN_CACHE_MANAGER.put(param.getAccountName(), new AuthInfo(access_token,remember), expire);
        } else {
            throw new RuntimeException("请求token异常" + responseEntity);
        }
    }

    //,token:{accessToken:"b8yltUCTibofmLZxxIvYhXQVQSaG8wv",refreshToken:"Sr06GzP8ZWHNJsH2FRkfUu6mdX5VURl"}
    private void requestAuthByRemember(MeicanAccount param) throws URISyntaxException {
        String url = configProperties.getBaseUrl();
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Accept", "application/json, text/plain, */*");
        headers.add("Cookie", param.getAccountCookie());
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");
        RequestEntity<String> requestEntity = new RequestEntity<>(headers, HttpMethod.POST, new URI(url));
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String indexHtml = Objects.requireNonNull(responseEntity.getBody(), "获取index body 异常");
            Date expire = new Date(System.currentTimeMillis() + (3000L * 1000L));
            Pattern patten= Pattern.compile("\\{accessToken:(.*?)\\}");
            Matcher m = patten.matcher(indexHtml);
            String token = null;
            while (m.find()){
                 token = m.group();
            }
            if (token == null) {
                throw new RuntimeException("未找到首页token数据");
            }
            JSONObject tokenJson = JSON.parseObject(token);
            TOKEN_CACHE_MANAGER.put(param.getAccountName(), new AuthInfo(tokenJson.getString("accessToken"),param.getAccountCookie()), expire);
        } else {
            throw new RuntimeException("请求首页异常" + responseEntity);
        }
    }

    @AllArgsConstructor
    @Data
    public static class AuthInfo {
        private String token;
        private String cookie;
    }


}
