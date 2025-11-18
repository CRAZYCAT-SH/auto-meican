package com.github.automeican.service;

import com.github.automeican.dao.entity.MeicanAccount;
import com.github.automeican.dao.service.IMeicanAccountService;
import com.github.automeican.dto.BaseRequest;
import com.github.automeican.remote.AuthService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * @ClassName MericanAccountManagerService
 * @Description
 * @Author liyongbing
 * @Date 2022/9/28 10:40
 * @Version 1.0
 **/
@Slf4j
@Component
public class MeicanAccountManagerService {
    @Resource
    private IMeicanAccountService meicanAccountService;
    @Resource
    private AuthService authService;

    @Transactional(rollbackFor = Exception.class)
    public boolean saveAndAuth(MeicanAccount account) {
        meicanAccountService.save(account);
        BaseRequest param = new BaseRequest();
        param.setUsername(account.getAccountName());
        authService.auth(param);
        return true;
    }
}
