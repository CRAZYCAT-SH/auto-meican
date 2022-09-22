package com.shinho.automeicai;

import com.shinho.automeicai.dto.CalendarItemsRequest;
import com.shinho.automeicai.remote.TokenService;
import com.shinho.automeicai.remote.impl.CalendarItemsExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDate;

@SpringBootTest
class AutoMeicaiApplicationTests {

    @Resource
    private TokenService tokenService;
    @Resource
    private CalendarItemsExecutor calendarItemsExecutor;

    @Test
    void test() {
        CalendarItemsRequest param = new CalendarItemsRequest();
        param.setUsername("18762036036@shinho.net.cn");
        param.setPassword("Shinho123");
        param.setBeginDate(LocalDate.now().toString());
        param.setEndDate(LocalDate.now().toString());
        System.out.println(calendarItemsExecutor.execute(param));
    }

}
