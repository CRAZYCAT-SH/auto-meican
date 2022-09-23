package com.shinho.automeican;

import com.shinho.automeican.dao.entity.MeicanTask;
import com.shinho.automeican.dao.service.IMeicanTaskService;
import com.shinho.automeican.dto.BaseRequest;
import com.shinho.automeican.remote.AuthService;
import com.shinho.automeican.remote.MeicanClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDate;

@SpringBootTest
class AutoMeicaiApplicationTests {

    @Resource
    private AuthService authService;
    @Resource
    private IMeicanTaskService meicanTaskService;
    @Resource
    private MeicanClient meicanClient;

    @Test
    void test() {
        System.out.println(meicanTaskService.list());
    }


    @Test
    void test1() {
        MeicanTask task = new MeicanTask();
        task.setMeicanAccountName("13764723439@shinho.net.cn");
        task.setMeicanAccountPassword("123456");
        task.setOrderDate(LocalDate.now().toString());
        task.setOrderDish("叉烧");
        meicanClient.executeTask(task);
    }

    private <T extends BaseRequest> void setBase(MeicanTask param) {
//        param.setUsername("18762036036@shinho.net.cn");
//        param.setPassword("Shinho123");
//        param.setUsername("13918961790@shinho.net.cn");
//        param.setPassword("Shinho123");
        param.setMeicanAccountName("13764723439@shinho.net.cn");
        param.setMeicanAccountPassword("123456");
    }

}
