package com.github.automeican.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @ClassName CacheManager
 * @Description 大ID序列化为字符串，兼容js数字丢失精度
 * @Author liyongbing
 * @Date 2022/9/27 16:49
 * @Version 1.0
 **/
public class BigIdSerializer extends JsonSerializer<Long> {

    @Override
    public void serialize(Long value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (value != null) {
            jsonGenerator.writeString(value.toString());
        } else {
            jsonGenerator.writeString(value + "");
        }
    }


}
