package com.github.automeican.mcp;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.automeican.common.AccountContextHolder;
import com.github.automeican.common.HttpReturnEnums;
import com.github.automeican.common.JsonResult;
import com.github.automeican.common.TaskStatus;
import com.github.automeican.dao.entity.MeicanAccount;
import com.github.automeican.dao.entity.MeicanAccountDishCheck;
import com.github.automeican.dao.entity.MeicanBooking;
import com.github.automeican.dao.service.IMeicanAccountDishCheckService;
import com.github.automeican.dao.service.IMeicanAccountService;
import com.github.automeican.dao.service.IMeicanBookingService;
import com.github.automeican.remote.MeicanClient;
import com.github.automeican.service.MeicanAccountManagerService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@RequiredArgsConstructor
@Slf4j
@Service
public class DishService {

    private final IMeicanBookingService meicanBookingService;
    private final MeicanClient meicanClient;


    @Tool(description = "查询最近点餐记录")
    public JsonResult<List<MeicanBooking>> recentOrder(@ToolParam(description = "查询最近多少天") int days) {
        if (days < 1) {
            return JsonResult.get(HttpReturnEnums.ParaError, null, "参数错误");
        }
        LocalDate now = LocalDate.now();
        String orderDate = now.minusDays(days).toString();
        String account = AccountContextHolder.getAccount();
        return JsonResult.get(meicanBookingService.list(Wrappers.<MeicanBooking>lambdaQuery()
                .eq(MeicanBooking::getAccountName, account)
                .ge(MeicanBooking::getOrderDate, orderDate)
                .orderByDesc(MeicanBooking::getOrderDate)));
    }

    @Tool(description = "查询指定日期的美餐菜品")
    public JsonResult<List<String>> dishList(@ToolParam(description = "查询日期,格式：yyyy-MM-dd") String date) {
        String account = AccountContextHolder.getAccount();
        return JsonResult.get(meicanClient.currentDishList(account,date));
    }

    @Tool(description = "点餐预定")
    public JsonResult<Boolean> addOrder(@ToolParam(description = "预定日期,格式：yyyy-MM-dd") String orderDate,
                                        @ToolParam(description = "预定菜品") String orderDish) {
        meicanBookingService.remove(Wrappers.<MeicanBooking>lambdaQuery()
                .eq(MeicanBooking::getAccountName,AccountContextHolder.getAccount())
                .eq(MeicanBooking::getOrderDate,orderDate)
        );
        String account = AccountContextHolder.getAccount();
        MeicanBooking meicanBooking = new MeicanBooking();
        meicanBooking.setAccountName(account);
        meicanBooking.setOrderDate(orderDate);
        meicanBooking.setOrderDish(orderDish);
        meicanBooking.setOrderStatus(TaskStatus.INIT.name());
        meicanBooking.setCreateDate(new Date());
        meicanBooking.setUpdateDate(new Date());
        final boolean save = meicanBookingService.save(meicanBooking);
        if (orderDate.equals(LocalDate.now().toString())) {
            try {
                meicanClient.executeTask(meicanBooking);
                meicanBooking.setOrderStatus(TaskStatus.SUCCESS.name());
                meicanBooking.setUpdateDate(new Date());
                meicanBookingService.updateById(meicanBooking);
            } catch (Exception e) {
                log.error("执行异常", e);
                meicanBooking.setOrderStatus(TaskStatus.FAIL.name());
                meicanBooking.setUpdateDate(new Date());
                meicanBooking.setErrorMsg(e.getMessage());
                meicanBookingService.updateById(meicanBooking);
                return JsonResult.get(HttpReturnEnums.SystemError, false, e.getMessage());
            }
        }
        return JsonResult.get(save);
    }

    @Tool(description = "取消预定")
    public JsonResult<Boolean> cancelOrder(@ToolParam(description = "预定日期,格式：yyyy-MM-dd") String orderDate) {
        String account = AccountContextHolder.getAccount();
        MeicanBooking meicanBooking = meicanBookingService.getOne(Wrappers.<MeicanBooking>lambdaQuery()
                .eq(MeicanBooking::getAccountName,account)
                .eq(MeicanBooking::getOrderDate,orderDate)
        );
        if (meicanBooking != null) {
            meicanBookingService.removeById(meicanBooking);
        }
        return JsonResult.get(true);
    }

}
