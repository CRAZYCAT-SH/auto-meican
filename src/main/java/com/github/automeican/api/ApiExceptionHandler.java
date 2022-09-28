package com.github.automeican.api;

import com.github.automeican.common.HttpReturnEnums;
import com.github.automeican.common.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName ExceptionHandler
 * @Description
 * @Author liyongbing
 * @Date 2022/9/28 11:29
 * @Version 1.0
 **/
@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public JsonResult handle(RuntimeException e) {
        log.error("运行异常", e);
        return JsonResult.get(HttpReturnEnums.SystemError, null, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonResult handle(Exception e) {
        log.error("未知异常", e);
        return JsonResult.get(HttpReturnEnums.SystemError);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public JsonResult handle(Throwable t) {
        log.error("未知系统异常", t);
        return JsonResult.get(HttpReturnEnums.SystemError);
    }
}
