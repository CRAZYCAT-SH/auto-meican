package com.github.automeican.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TokenResponse
 * @Description
 * @Author liyongbing
 * @Date 2022/9/22 16:25
 * @Version 1.0
 **/
@Data
public class TokenResponse implements Serializable {
    //{"access_token":"zEsNQUZXhGMJviruguWUZCoXON1R3kz",
// "token_type":"bearer",
// "refresh_token":"Gn2JMSIP1U6GeM84omJOJSEKFBLzy9e",
// "expires_in":3600,
// "need_reset_password":false}
    private String access_token;
    private String token_type;
    private String refresh_token;
    private Integer expires_in;
    private String need_reset_password;
}
