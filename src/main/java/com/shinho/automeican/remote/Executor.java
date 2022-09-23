package com.shinho.automeican.remote;

/**
 * @ClassName Executor
 * @Description 通用执行器
 * @Author liyongbing
 * @Date 2022/9/22 15:54
 * @Version 1.0
 **/
public interface Executor<T,D> {

    D execute(T param);

}
