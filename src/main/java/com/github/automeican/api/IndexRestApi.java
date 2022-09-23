package com.github.automeican.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.automeican.api.dto.MeicanTaskQuery;
import com.github.automeican.common.HttpReturnEnums;
import com.github.automeican.common.JsonResult;
import com.github.automeican.common.TaskStatus;
import com.github.automeican.dao.entity.MeicanTask;
import com.github.automeican.dao.service.IMeicanTaskService;
import com.github.automeican.remote.MeicanClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName IndexRestApi
 * @Description
 * @Author liyongbing
 * @Date 2022/9/22 15:30
 * @Version 1.0
 **/
@Slf4j
@Api(value = "首页", tags = "首页")
@RestController
public class IndexRestApi {

    @GetMapping("/hello")
    public Object hello() {
        return "hello auto meicai";
    }

    @Resource
    private IMeicanTaskService meicanTaskService;
    @Resource
    private MeicanClient meicanClient;


    @ApiOperation("分页查询美餐预定任务")
    @GetMapping("/api/meicanTask/pageTask")
    public JsonResult<Page<MeicanTask>> pageTask(MeicanTaskQuery query) {
        LambdaQueryWrapper<MeicanTask> queryWrapper = Wrappers.lambdaQuery();
        if (query.getOrderDate() != null) {
            queryWrapper.like(MeicanTask::getOrderDate, query.getOrderDate());
        }
        if (query.getOrderDish() != null) {
            queryWrapper.like(MeicanTask::getOrderDate, query.getOrderDish());
        }
        if (query.getMeicanAccountName() != null) {
            queryWrapper.like(MeicanTask::getOrderDate, query.getMeicanAccountName());
        }
        return JsonResult.get(meicanTaskService.page(new Page<>(query.getPageNo(), query.getPageSize()), queryWrapper));
    }

    @ApiOperation("添加美餐预定任务")
    @PostMapping("/api/meicanTask/addTask")
    public JsonResult<Boolean> addTask(@RequestBody MeicanTask task) {
        task.setOrderStatus(TaskStatus.INIT.name());
        task.setCreateDate(new Date());
        task.setUpdateDate(new Date());
        return JsonResult.get(meicanTaskService.save(task));
    }

    @ApiOperation("更新美餐预定任务")
    @PutMapping("/api/meicanTask/updateTask")
    public JsonResult<Boolean> updateTask(@RequestBody MeicanTask task) {
        Assert.notNull(task.getUid(),"ID必填");
        return JsonResult.get(meicanTaskService.updateById(task));
    }

    @ApiOperation("删除美餐预定任务")
    @DeleteMapping("/api/meicanTask/removeTask")
    public JsonResult<Boolean> removeTask(@RequestParam Long taskId){
        return JsonResult.get(meicanTaskService.removeById(taskId));
    }

    @ApiOperation("立刻执行预定")
    @GetMapping("/api/meicanTask/doTask")
    public JsonResult<Boolean> doTask(@RequestParam Long taskId){
        MeicanTask meicanTask = meicanTaskService.getById(taskId);
        try {
            meicanClient.executeTask(meicanTask);
            meicanTask.setOrderStatus(TaskStatus.SUCCESS.name());
            meicanTaskService.updateById(meicanTask);
        } catch (Exception e) {
            log.error("执行异常",e);
            meicanTask.setOrderStatus(TaskStatus.FAIL.name());
            meicanTaskService.updateById(meicanTask);
            return JsonResult.get(HttpReturnEnums.SystemError,false,e.getMessage());
        }
        return JsonResult.get(true);
    }

}
