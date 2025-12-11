package com.github.automeican.api;

import com.github.automeican.common.HttpReturnEnums;
import com.github.automeican.common.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<JsonResult> handle(RuntimeException e) {
        log.error("运行异常", e);
        JsonResult result = JsonResult.get(HttpReturnEnums.SystemError, null, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonResult> handle(Exception e) {
        log.error("未知异常", e);
        JsonResult result = JsonResult.get(HttpReturnEnums.SystemError);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<JsonResult> handle(Throwable t) {
        log.error("未知系统异常", t);
        JsonResult result = JsonResult.get(HttpReturnEnums.SystemError);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }
}
