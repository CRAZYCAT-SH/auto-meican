package com.shinho.automeicai.common;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @ClassName CacheManager
 * @Description 简单惰性过期缓存实现
 * @Author liyongbing
 * @Date 2022/9/22 16:49
 * @Version 1.0
 **/
public class CacheManager<K,V> {
    private final ConcurrentMap<K,Node<V>> cache = new ConcurrentHashMap<>(500);

    public Node<V> put(K key, V value, Date expire){
        return cache.put(key, Node.<V>builder().data(value).expire(expire).build());
    }

    public V get(K key){
        Node<V> node = cache.get(key);
        if (node != null) {
            if (node.getExpire().after(new Date())) {
                return node.getData();
            }else {
                cache.remove(key);
            }
        }
        return null;
    }


    @Builder
    @Data
    public static class Node<T> {
        private T data;
        private Date expire;
    }
}
