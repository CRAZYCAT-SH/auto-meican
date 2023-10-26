package com.github.automeican.common;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName CacheManager
 * @Description 简单惰性过期缓存实现
 * @Author liyongbing
 * @Date 2022/9/22 16:49
 * @Version 1.0
 **/
public class CacheManager<K, V> {
    private final Map<K, Node<V>> cache;

    public CacheManager() {
        this(500);
    }

    public CacheManager(int capacity) {
        this.cache = new LinkedHashMap<K, Node<V>>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > capacity;
            }
        };
    }

    public Node<V> put(K key, V value, Date expire) {
        return cache.put(key, Node.<V>builder().data(value).expire(expire).build());
    }

    public V get(K key) {
        Node<V> node = cache.get(key);
        if (node != null) {
            if (node.getExpire().after(new Date())) {
                return node.getData();
            } else {
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
