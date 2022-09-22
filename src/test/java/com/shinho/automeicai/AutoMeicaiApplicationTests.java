package com.shinho.automeicai;

import com.shinho.automeicai.dto.BaseRequest;
import com.shinho.automeicai.remote.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class AutoMeicaiApplicationTests {

    @Resource
    private TokenService tokenService;

    @Test
    void contextLoads() {
        BaseRequest param = new BaseRequest();
        param.setUsername("18762036036@shinho.net.cn");
        param.setPassword("Shinho123");
        System.out.println(tokenService.getToken(param));
    }

}
