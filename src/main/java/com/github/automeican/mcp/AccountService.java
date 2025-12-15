package com.github.automeican.mcp;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.automeican.common.AccountContextHolder;
import com.github.automeican.common.HttpReturnEnums;
import com.github.automeican.common.JsonResult;
import com.github.automeican.dao.entity.MeicanAccount;
import com.github.automeican.dao.entity.MeicanAccountDishCheck;
import com.github.automeican.dao.service.IMeicanAccountDishCheckService;
import com.github.automeican.dao.service.IMeicanAccountService;
import com.github.automeican.service.MeicanAccountManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Slf4j
@Service
public class AccountService {

    private final MeicanAccountManagerService meicanAccountManagerService;
    private final IMeicanAccountService meicanAccountService;
    private final IMeicanAccountDishCheckService meicanAccountDishCheckService;


    @Tool(description = "更新账号密码")
    public JsonResult<Boolean> updateAccount(@ToolParam(description = "账号密码") String password) {
        log.info("更新账号密码");
        String account = AccountContextHolder.getAccount();
        try {
            meicanAccountManagerService.saveAndAuth(MeicanAccount.builder()
                    .accountName(account)
                    .accountPassword(password)
                    .build());
        } catch (Exception e) {
            return JsonResult.get(HttpReturnEnums.SystemError, false, e.getMessage());
        }
        return JsonResult.ok();
    }


    @Tool(description = "获取账号详细信息")
    public JsonResult<MeicanAccount> getAccount() {
        log.info("获取账号密码");
        String account = AccountContextHolder.getAccount();
        MeicanAccount meicanAccount = meicanAccountService.getOne(Wrappers.<MeicanAccount>lambdaQuery().eq(MeicanAccount::getAccountName, account));
        if (meicanAccount == null) {
            return JsonResult.get(HttpReturnEnums.SystemError, null, "账号不存在");
        }
        return JsonResult.get(meicanAccount);
    }

    @Tool(description = "更新当前自动点餐配置")
    public JsonResult<Boolean> updateAutoEatConfig(@ToolParam(description = "自动点餐有效期,格式：yyyy-MM-dd") String date,
                                                   @ToolParam(description = "自动点餐黑名单,多个黑名单使用,分隔") String blackList) {

        log.info("更新当前自动点餐配置");
        String account = AccountContextHolder.getAccount();
        MeicanAccount meicanAccount = meicanAccountService.getOne(Wrappers.<MeicanAccount>lambdaQuery().eq(MeicanAccount::getAccountName, account));
        if (meicanAccount == null) {
            return JsonResult.get(HttpReturnEnums.SystemError, false, "账号不存在");
        }
        MeicanAccountDishCheck dishCheck = meicanAccountDishCheckService.getOne(Wrappers.<MeicanAccountDishCheck>lambdaQuery().eq(MeicanAccountDishCheck::getAccountName, account));
        if (dishCheck == null) {
            dishCheck = MeicanAccountDishCheck.builder()
                    .accountName(account)
                    .expireDate(date)
                    .noOrderDishes(blackList)
                    .build();
        } else {
            dishCheck.setExpireDate(date);
            dishCheck.setNoOrderDishes(blackList);
        }
        return JsonResult.get(meicanAccountDishCheckService.saveOrUpdate(dishCheck));
    }

    @Tool(description = "获取当前自动点餐配置")
    public JsonResult<MeicanAccountDishCheck> getAutoEatConfig() {
        log.info("获取当前自动点餐配置");
        String account = AccountContextHolder.getAccount();
        MeicanAccountDishCheck dishCheck = meicanAccountDishCheckService.getOne(Wrappers.<MeicanAccountDishCheck>lambdaQuery().eq(MeicanAccountDishCheck::getAccountName, account));
        return JsonResult.get(dishCheck);
    }

}
