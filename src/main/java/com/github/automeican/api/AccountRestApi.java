package com.github.automeican.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.automeican.common.JsonResult;
import com.github.automeican.dao.entity.MeicanAccount;
import com.github.automeican.dao.service.IMeicanAccountService;
import com.github.automeican.service.MeicanAccountManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName AccountRestApi
 * @Description
 * @Author liyongbing
 * @Date 2022/9/26 13:52
 * @Version 1.0
 **/
@Slf4j
@Api(value = "美餐账号配置", tags = "美餐账号配置")
@RestController
public class AccountRestApi {

    @Resource
    private IMeicanAccountService meicanAccountService;
    @Resource
    private MeicanAccountManagerService meicanAccountManagerService;


    @ApiOperation("获取所有已配置美餐账号")
    @GetMapping("/api/meicanAccount/listAll")
    public JsonResult<List<MeicanAccount>> listAll(){
        return JsonResult.get(meicanAccountService.list());
    }

    @ApiOperation("添加并验证美餐账号")
    @PostMapping("/api/meicanAccount/addAccount")
    public JsonResult<Boolean> addAccount(@RequestBody MeicanAccount account){
        meicanAccountService.remove(Wrappers.<MeicanAccount>lambdaQuery().eq(MeicanAccount::getAccountName,account.getAccountName()));
        return JsonResult.get(meicanAccountManagerService.saveAndAuth(account));
    }

    @ApiOperation("修改美餐账号")
    @PutMapping("/api/meicanAccount/updateAccount")
    public JsonResult<Boolean> updateAccount(@RequestBody MeicanAccount account){
        return JsonResult.get(meicanAccountService.updateById(account));
    }

    @ApiOperation("删除美餐账号")
    @DeleteMapping("/api/meicanAccount/removeAccount")
    public JsonResult<Boolean> removeAccount(@RequestParam Long accountId){
        return JsonResult.get(meicanAccountService.removeById(accountId));
    }


}
