package com.shinho.automeicai;

import com.shinho.automeicai.dao.entity.MeicanTask;
import com.shinho.automeicai.dao.service.IMeicanTaskService;
import com.shinho.automeicai.dto.CalendarItemsRequest;
import com.shinho.automeicai.remote.TokenService;
import com.shinho.automeicai.remote.impl.CalendarItemsExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

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
