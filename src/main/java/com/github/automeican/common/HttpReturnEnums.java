package com.github.automeican.common;

/**
 * @ClassName HttpReturnEnums
 * @Description
 * @Author liyongbing
 * @Date 2022/9/23 16:23
 * @Version 1.0
 **/
public enum HttpReturnEnums implements IEnum {
    Success(200, "成功"),
    SystemError(500, "系统错误"),
    ParaError(511, "参数错误"),
    EncodeError(512, "参数加解密异常"),
    JSONParaError(514, "请求JSON数据体不合法"),
    SignError(523, "签名认证失败"),
    OrderByError(534, "排序字段不合法");

    private int value = 0;
    private String desc;

    private HttpReturnEnums(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int value() {
        return this.value;
    }

    public String desc() {
        return this.desc;
    }

    public static HttpReturnEnums getEnum(int index) {
        HttpReturnEnums[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            HttpReturnEnums c = var1[var3];
            if (c.value() == index) {
                return c;
            }
        }

        return null;
    }
}
