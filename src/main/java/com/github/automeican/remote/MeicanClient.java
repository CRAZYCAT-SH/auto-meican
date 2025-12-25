package com.github.automeican.remote;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.automeican.common.CacheManager;
import com.github.automeican.config.MeicanConfigProperties;
import com.github.automeican.dao.entity.MeicanBooking;
import com.github.automeican.dto.*;
import com.github.automeican.remote.impl.CalendarItemsExecutor;
import com.github.automeican.remote.impl.OrdersAddExecutor;
import com.github.automeican.remote.impl.RestaurantsListExecutor;
import com.github.automeican.remote.impl.RestaurantsShowExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import jakarta.annotation.Resource;

import java.time.LocalDate;
import java.util.*;
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
    private static final CacheManager<String, List<DishesResponse>> DISH_CACHE_MANAGER = new CacheManager<>();
    private static final Random RANDOM = new Random();
    @Resource
    private MeicanConfigProperties meicanConfigProperties;
    @Resource
    private CalendarItemsExecutor calendarItemsExecutor;
    @Resource
    private OrdersAddExecutor ordersAddExecutor;
    @Resource
    private RestaurantsListExecutor restaurantsListExecutor;
    @Resource
    private RestaurantsShowExecutor restaurantsShowExecutor;


    public void executeTask(MeicanBooking task) {
        Assert.notNull(task, "任务为空");
        Assert.notNull(task.getAccountName(), "美餐账号未填写");
        Assert.notNull(task.getOrderDate(), "日期未填写");
        List<CalendarItemsResponse> calendars = getCalendar(task.getAccountName(), task.getOrderDate());
        if (CollectionUtils.isEmpty(calendars)) {
            throw new RuntimeException("未找到点餐日历");
        }
        CalendarItemsResponse calendar = calendars.stream().filter(e -> "欣和企业午餐".equals(e.getTitle())).findFirst()
                .orElseThrow(() -> new RuntimeException("未找到可用点餐日历"));
        if (!OPEN_STATUS.equals(calendar.getStatus())) {
            log.warn("点餐日历可能不可用：" + calendar.getTitle());
        }
        List<DishesResponse> dishes = getDishes(task.getAccountName(), calendar);
        if (CollectionUtils.isEmpty(dishes)) {
            throw new RuntimeException("未找到点餐菜品");
        }
        String originDish = task.getOrderDish();
        if (originDish.startsWith("[auto]")) {
            originDish = originDish.substring(6);
        }
        String finalOriginDish = originDish;
        DishesResponse dish = dishes.stream().filter(e -> e.getName().contains(finalOriginDish)).findFirst().orElse(dishes.get(RANDOM.nextInt(dishes.size())));
        if (!dish.getName().contains(finalOriginDish)) {
            log.warn("未找到点餐菜品，随机选择了一个菜品：" + dish.getName());
            task.setOrderDish("[miss]" + dish.getName());
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

    public List<DishesResponse> currentDishList(String account, String date) {
        if (!StringUtils.hasText(account)) {
            account = meicanConfigProperties.getSysAccount();
        }
        if (!StringUtils.hasText(date)) {
            date = LocalDate.now().plusDays(1).toString();
        }
        final String key = "dish:" + account + date;
        final List<DishesResponse> data = DISH_CACHE_MANAGER.get(key);
        if (CollectionUtils.isNotEmpty(data)) {
            return data;
        }
        try {
            List<CalendarItemsResponse> calendars = getCalendar(account, date);
            CalendarItemsResponse calendar = calendars.stream().filter(e -> "欣和企业午餐".equals(e.getTitle())).findFirst()
                    .orElse(null);
            if (calendar != null) {
                List<DishesResponse> dishes = getDishes(account, calendar);
                DISH_CACHE_MANAGER.put(key, dishes, new Date(System.currentTimeMillis() + (6 * 60 * 60 * 1000L)));
                return dishes;
            }
        } catch (Exception e) {
            log.error("获取菜品失败", e);
        }
        return Collections.emptyList();
    }


    private List<DishesResponse> getDishes(String account, CalendarItemsResponse calendar) {
        List<DishesResponse> result = new ArrayList<>();
        RestaurantsListRequest param = new RestaurantsListRequest();
        param.setUsername(account);
        param.setTabUniqueId(calendar.getUniqueId());
        param.setTargetTime(calendar.getTargetTime());
        List<RestaurantsListResponse> responseList = restaurantsListExecutor.execute(param);
        if (CollectionUtils.isNotEmpty(responseList)) {
            for (RestaurantsListResponse response : responseList) {
                RestaurantsShowRequest request = new RestaurantsShowRequest();
                request.setUsername(account);
                request.setTabUniqueId(calendar.getUniqueId());
                request.setTargetTime(calendar.getTargetTime());
                request.setRestaurantUniqueId(response.getUniqueId());
                List<DishesResponse> dishesResponses = restaurantsShowExecutor.execute(request);
                List<DishesResponse> list = new ArrayList<>();
                for (DishesResponse e : dishesResponses) {
                    if (!e.getIsSection()) {
                        DishesResponse.Restaurant restaurant = new DishesResponse.Restaurant();
                        restaurant.setName(response.getName());
                        restaurant.setUniqueId(response.getUniqueId());
                        restaurant.setOpen(response.getOpen());
                        e.setRestaurant(restaurant);
                        list.add(e);
                    }
                }
                result.addAll(list);
            }
        }
        return result;
    }

    private List<CalendarItemsResponse> getCalendar(String account, String date) {
        CalendarItemsRequest param = new CalendarItemsRequest();
        param.setUsername(account);
        param.setBeginDate(date);
        param.setEndDate(date);
        return calendarItemsExecutor.execute(param);
    }

}
