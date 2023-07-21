package com.github.automeican;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.automeican.api.IndexRestApi;
import com.github.automeican.dao.entity.MeicanAccount;
import com.github.automeican.dao.entity.MeicanBooking;
import com.github.automeican.dao.service.IMeicanAccountService;
import com.github.automeican.dao.service.IMeicanBookingService;
import com.github.automeican.remote.AuthService;
import com.github.automeican.remote.MeicanClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class AutoMeicanApplicationTests {

    @Resource
    private AuthService authService;
    @Resource
    private MeicanClient meicanClient;
    @Resource
    private IMeicanAccountService meicanAccountService;
    @Resource
    private IMeicanBookingService meicanBookingService;
    @Resource
    private IndexRestApi indexRestApi;

    @Test
    void test() {
    }

    @Test
    void test1() {
        System.out.println(meicanClient.currentDishList(null,"2023-07-21"));
    }


}
