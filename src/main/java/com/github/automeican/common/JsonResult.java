package com.github.automeican.common;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @ClassName JsonResult
 * @Description
 * @Author liyongbing
 * @Date 2022/9/23 16:21
 * @Version 1.0
 **/
public class JsonResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(
            value = "状态码",
            notes = "200是成功,其他都是失败"
    )
    private int code;
    @ApiModelProperty("状态码描述信息")
    private String msg;
    @ApiModelProperty("具体的业务数据模型")
    private T data;

    public JsonResult() {
        this.code = HttpReturnEnums.Success.value();
        this.msg = HttpReturnEnums.Success.desc();
    }

    public JsonResult(T data) {
        this.code = HttpReturnEnums.Success.value();
        this.msg = HttpReturnEnums.Success.desc();
        this.data = data;
    }

    public JsonResult(IEnum e) {
        this.code = HttpReturnEnums.Success.value();
        this.msg = HttpReturnEnums.Success.desc();
        this.setCode(e.value());
        this.setMsg(e.desc());
    }

    public JsonResult(IEnum e, T data) {
        this.code = HttpReturnEnums.Success.value();
        this.msg = HttpReturnEnums.Success.desc();
        this.setCode(e.value());
        this.setMsg(e.desc());
        this.setData(data);
    }

    public JsonResult(IEnum e, T data, String message) {
        this.code = HttpReturnEnums.Success.value();
        this.msg = HttpReturnEnums.Success.desc();
        this.setCode(e.value());
        this.setMsg(message);
        this.setData(data);
    }

    public int getCode() {
        return this.code;
    }

    public JsonResult<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return this.msg;
    }

    public JsonResult<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return this.data;
    }

    public JsonResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public static <T> JsonResult<T> ok() {
        return get((IEnum)HttpReturnEnums.Success);
    }

    public static <T> JsonResult<T> get(T data) {
        return new JsonResult<>(data);
    }

    public static <T> JsonResult<T> get(IEnum e) {
        return new JsonResult<>(e);
    }

    public static <T> JsonResult<T> get(IEnum e, T data) {
        return new JsonResult<>(e, data);
    }

    public static <T> JsonResult<T> get(IEnum e, T data, String message) {
        return new JsonResult<>(e, data, message);
    }

    public String toString() {
        return JSON.toJSONString(this);
    }
}
