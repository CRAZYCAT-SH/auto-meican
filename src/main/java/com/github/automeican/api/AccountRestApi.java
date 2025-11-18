package com.github.automeican.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.automeican.common.JsonResult;
import com.github.automeican.dao.entity.MeicanAccount;
import com.github.automeican.dao.entity.MeicanAccountDishCheck;
import com.github.automeican.dao.service.IMeicanAccountDishCheckService;
import com.github.automeican.dao.service.IMeicanAccountService;
import com.github.automeican.service.MeicanAccountManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @ClassName AccountRestApi
 * @Description
 * @Author liyongbing
 * @Date 2022/9/26 13:52
 * @Version 1.0
 **/
@Slf4j
@Tag(name = "美餐账号配置", description = "美餐账号配置")
@RestController
public class AccountRestApi {

    @Resource
    private IMeicanAccountService meicanAccountService;
    @Resource
    private MeicanAccountManagerService meicanAccountManagerService;
    @Resource
    private IMeicanAccountDishCheckService meicanAccountDishCheckService;


    @Operation(summary = "获取所有已配置美餐账号")
    @GetMapping("/api/meicanAccount/listAll")
    public JsonResult<List<MeicanAccount>> listAll(){
        return JsonResult.get(meicanAccountService.list(Wrappers.lambdaQuery(MeicanAccount.class).select(MeicanAccount::getUid,MeicanAccount::getAccountName)));
    }

    @Operation(summary = "添加并验证美餐账号")
    @PostMapping("/api/meicanAccount/addAccount")
    public JsonResult<Boolean> addAccount(@RequestBody MeicanAccount account){
        meicanAccountService.remove(Wrappers.<MeicanAccount>lambdaQuery().eq(MeicanAccount::getAccountName,account.getAccountName()));
        return JsonResult.get(meicanAccountManagerService.saveAndAuth(account));
    }

    @Operation(summary = "修改美餐账号")
    @PutMapping("/api/meicanAccount/updateAccount")
    public JsonResult<Boolean> updateAccount(@RequestBody MeicanAccount account){
        return JsonResult.get(meicanAccountService.updateById(account));
    }

    @Operation(summary = "删除美餐账号")
    @DeleteMapping("/api/meicanAccount/removeAccount")
    public JsonResult<Boolean> removeAccount(@RequestParam Long accountId){
        return JsonResult.get(meicanAccountService.removeById(accountId));
    }

    @Operation(summary = "获取所有已配置点餐检查账号")
    @GetMapping("/api/meicanAccount/listAllCheck")
    public JsonResult<List<MeicanAccountDishCheck>> listAllCheck(){
        return JsonResult.get(meicanAccountDishCheckService.list());
    }


    @Operation(summary = "添加点餐检查")
    @PostMapping("/api/meicanAccount/addAccountDishCheck")
    public JsonResult<Boolean> addAccountDishCheck(@RequestBody MeicanAccountDishCheck account){
        meicanAccountDishCheckService.remove(Wrappers.<MeicanAccountDishCheck>lambdaQuery().eq(MeicanAccountDishCheck::getAccountName,account.getAccountName()));
        account.setCreateDate(new Date());
        account.setUpdateDate(new Date());
        return JsonResult.get(meicanAccountDishCheckService.save(account));
    }

    @Operation(summary = "删除点餐检查")
    @DeleteMapping("/api/meicanAccount/removeAccountDishCheck")
    public JsonResult<Boolean> removeAccountDishCheck(@RequestParam Long checkId){
        return JsonResult.get(meicanAccountDishCheckService.removeById(checkId));
    }
}
