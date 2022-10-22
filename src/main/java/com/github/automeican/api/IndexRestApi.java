package com.github.automeican.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.automeican.api.dto.MeicanBookingQuery;
import com.github.automeican.common.CacheManager;
import com.github.automeican.common.HttpReturnEnums;
import com.github.automeican.common.JsonResult;
import com.github.automeican.common.TaskStatus;
import com.github.automeican.dao.entity.MeicanBooking;
import com.github.automeican.dao.service.IMeicanBookingService;
import com.github.automeican.remote.MeicanClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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
    private IMeicanBookingService meicanBookingService;
    @Resource
    private MeicanClient meicanClient;


    @ApiOperation("分页查询美餐预定任务")
    @GetMapping("/api/meicanTask/pageTask")
    public JsonResult<Page<MeicanBooking>> pageTask(MeicanBookingQuery query) {
        LambdaQueryWrapper<MeicanBooking> queryWrapper = Wrappers.lambdaQuery();
        if (query.getOrderDate() != null) {
            queryWrapper.like(MeicanBooking::getOrderDate, query.getOrderDate());
        }
        if (query.getOrderDish() != null) {
            queryWrapper.like(MeicanBooking::getOrderDish, query.getOrderDish());
        }
        if (query.getAccountName() != null) {
            queryWrapper.like(MeicanBooking::getAccountName, query.getAccountName());
        }
        queryWrapper.orderByDesc(MeicanBooking::getUid);
        return JsonResult.get(meicanBookingService.page(new Page<>(query.getPageNo(), query.getPageSize()), queryWrapper));
    }

    @ApiOperation("查询美餐推荐菜品")
    @GetMapping("/api/meicanTask/dishList")
    public JsonResult<List<String>> dishList(@RequestParam(required = false) String accountName,
                                             @RequestParam(required = false) String date) {
        return JsonResult.get(meicanClient.currentDishList(accountName,date));
    }

    @ApiOperation("添加美餐预定任务")
    @PostMapping("/api/meicanTask/addTask")
    public JsonResult<Boolean> addTask(@RequestBody MeicanBooking task) {
        meicanBookingService.remove(Wrappers.<MeicanBooking>lambdaQuery()
                .eq(MeicanBooking::getAccountName,task.getAccountName())
                .eq(MeicanBooking::getOrderDate,task.getOrderDate())
        );
        task.setOrderStatus(TaskStatus.INIT.name());
        task.setCreateDate(new Date());
        task.setUpdateDate(new Date());
        return JsonResult.get(meicanBookingService.save(task));
    }

    @ApiOperation("更新美餐预定任务")
    @PutMapping("/api/meicanTask/updateTask")
    public JsonResult<Boolean> updateTask(@RequestBody MeicanBooking task) {
        Assert.notNull(task.getUid(), "ID必填");
        task.setUpdateDate(new Date());
        return JsonResult.get(meicanBookingService.updateById(task));
    }

    @ApiOperation("删除美餐预定任务")
    @DeleteMapping("/api/meicanTask/removeTask")
    public JsonResult<Boolean> removeTask(@RequestParam Long taskId) {
        return JsonResult.get(meicanBookingService.removeById(taskId));
    }

    @ApiOperation("立刻执行预定")
    @GetMapping("/api/meicanTask/doTask")
    public JsonResult<Boolean> doTask(@RequestParam Long taskId) {
        MeicanBooking meicanTask = meicanBookingService.getById(taskId);
        try {
            meicanClient.executeTask(meicanTask);
            meicanTask.setOrderStatus(TaskStatus.SUCCESS.name());
            meicanTask.setUpdateDate(new Date());
            meicanBookingService.updateById(meicanTask);
        } catch (Exception e) {
            log.error("执行异常", e);
            meicanTask.setOrderStatus(TaskStatus.FAIL.name());
            meicanTask.setUpdateDate(new Date());
            meicanBookingService.updateById(meicanTask);
            return JsonResult.get(HttpReturnEnums.SystemError, false, e.getMessage());
        }
        return JsonResult.get(true);
    }

}
