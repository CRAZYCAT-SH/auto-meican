package com.shinho.automeican.utils;

import java.util.Map;
import java.util.TreeMap;

/**
 * @ClassName ConvertUtil
 * @Description
 * @Author liyongbing
 * @Date 2022/9/22 17:25
 * @Version 1.0
 **/
public class ConvertUtil {
    public static String convertParamMap2FormStr(Map<String, String> param) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            treeMap.put(entry.getKey(), entry.getValue());
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : treeMap.entrySet()) {
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }
}
