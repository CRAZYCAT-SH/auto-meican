package com.github.automeican.remote;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.automeican.dao.entity.MeicanBooking;
import com.github.automeican.dao.entity.MeicanDish;
import com.github.automeican.dao.service.IMeicanDishService;
import com.github.automeican.dto.*;
import com.github.automeican.remote.impl.CalendarItemsExecutor;
import com.github.automeican.remote.impl.DishesExecutor;
import com.github.automeican.remote.impl.OrdersAddExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName MeicanClient
 * @Description
 * @Author liyongbing
 * @Date 2022/9/22 15:44
 * @Version 1.0
 **/
@Slf4j
@Component
public class MeicanClient {
    private static final String OPEN_STATUS = "AVAILABLE";
    public static final String SUCCESS_ORDER = "SUCCESSFUL";
    @Resource
    private CalendarItemsExecutor calendarItemsExecutor;
    @Resource
    private DishesExecutor dishesExecutor;
    @Resource
    private OrdersAddExecutor ordersAddExecutor;
    @Resource
    private IMeicanDishService meicanDishService;



    public void executeTask(MeicanBooking task) {
        Assert.notNull(task, "任务为空");
        Assert.notNull(task.getAccountName(), "美餐账号未填写");
        Assert.notNull(task.getOrderDate(), "日期未填写");
        List<CalendarItemsResponse> calendars = getCalendar(task);
        if (CollectionUtils.isEmpty(calendars)) {
            throw new RuntimeException("未找到点餐日历");
        }
        CalendarItemsResponse calendar = calendars.stream().filter(e -> "欣和企业午餐".equals(e.getTitle())).findFirst()
                .orElseThrow(() -> new RuntimeException("未找到可用点餐日历"));
        if (!OPEN_STATUS.equals(calendar.getStatus())) {
            log.warn("点餐日历可能不可用：" + calendar.getTitle());
        }
        List<DishesResponse> dishes = getDishes(task, calendar);
        if (CollectionUtils.isEmpty(dishes)) {
            throw new RuntimeException("未找到点餐菜品");
        }
        updateDish(task, dishes);
        DishesResponse dish = dishes.stream().filter(e -> e.getName().contains(task.getOrderDish())).findFirst().orElse(dishes.get(0));
        if (!dish.getName().contains(task.getOrderDish())) {
            log.warn("未找到点餐菜品，默认选择了第一个菜品：" + dish.getName());
        }
        OrdersAddRequest param = new OrdersAddRequest();
        param.setUsername(task.getAccountName());
        param.setTabUniqueId(calendar.getUniqueId());
        param.setTargetTime(calendar.getTargetTime());
        param.setOrder(Collections.singletonList(OrdersAddRequest.Order.builder().dishId(dish.getId()).count(1).build()));
        param.setRemarks(Collections.singletonList(OrdersAddRequest.Remark.builder().dishId(dish.getId()).remark("").build()));
        String success = ordersAddExecutor.execute(param);
        if (!SUCCESS_ORDER.equals(success)) {
            throw new RuntimeException("点餐失败");
        }
    }

    private void updateDish(MeicanBooking task, List<DishesResponse> dishes) {
        List<String> dishNameList = dishes.stream().map(DishesResponse::getName).collect(Collectors.toList());
        MeicanDish meicanDish = meicanDishService.getOne(Wrappers.<MeicanDish>lambdaQuery()
                .eq(MeicanDish::getAccountName, task.getAccountName())
                .eq(MeicanDish::getOrderDate,task.getOrderDate())
                .last(" LIMIT 1 ")
        );
        if (meicanDish == null) {
            meicanDish = new MeicanDish();
            meicanDish.setAccountName(task.getAccountName());
            meicanDish.setOrderDate(task.getOrderDate());
        }
        meicanDish.setOrderDish(JSON.toJSONString(dishNameList));
        meicanDishService.saveOrUpdate(meicanDish);
    }

    private List<DishesResponse> getDishes(MeicanBooking task, CalendarItemsResponse calendar) {
        DishesRequest param = new DishesRequest();
        param.setUsername(task.getAccountName());
        param.setTabUniqueId(calendar.getUniqueId());
        param.setTargetTime(calendar.getTargetTime());
        return dishesExecutor.execute(param);
    }

    private List<CalendarItemsResponse> getCalendar(MeicanBooking task) {
        CalendarItemsRequest param = new CalendarItemsRequest();
        param.setUsername(task.getAccountName());
        param.setBeginDate(task.getOrderDate());
        param.setEndDate(task.getOrderDate());
        return calendarItemsExecutor.execute(param);
    }

}
