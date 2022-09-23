package com.shinho.automeican.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName IndexRestApi
 * @Description
 * @Author liyongbing
 * @Date 2022/9/22 15:30
 * @Version 1.0
 **/
@RestController
public class IndexRestApi {

    @GetMapping("/hello")
    public Object hello(){
        return "hello auto meicai";
    }



}
