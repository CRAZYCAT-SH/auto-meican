package com.shinho.automeican;

import com.shinho.automeican.dao.entity.MeicanTask;
import com.shinho.automeican.dao.service.IMeicanTaskService;
import com.shinho.automeican.dto.CalendarItemsRequest;
import com.shinho.automeican.remote.TokenService;
import com.shinho.automeican.remote.impl.CalendarItemsExecutor;
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
    @Resource
    private IMeicanTaskService meicanTaskService;


//    @Rollback(value = false)
//    @Transactional
    @Test
    void test1(){
        MeicanTask entity = new MeicanTask();
        entity.setUid(1L);
        entity.setMeicanAccountName("1");
        entity.setMeicanAccountPassword("2");
        entity.setOrderDate("2022-10-01");
        entity.setOrderName("test");
        entity.setOrderDish("鸡腿堡");
//        System.out.println(meicanTaskService.save(entity));
        System.out.println(meicanTaskService.list());
    }

}
