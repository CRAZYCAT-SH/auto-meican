package com.github.automeican.common;

import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 固定窗口限流器
 */
@Component
public class FixedWindowRateLimiter {
    public static final String KEY = "fixedWindowRateLimiter:";

    private static final CacheManager<String, Integer> LIMIT_CACHE = new CacheManager<>(100);
    private static final PathLock PATH_LOCK = new PathLock();

    /**
     * @param path       限流路径
     * @param limit      请求限制数量
     * @param windowSize 窗口大小（单位：S）
     * @return
     */
    public boolean triggerLimit(String path, Long limit, Long windowSize) {
        try (PathLock.AutoCloseableLock ignored = PATH_LOCK.lock(path)) {
            Integer count = LIMIT_CACHE.get(path);
            if (count == null) {
                count = 0;
            }
            if (count >= limit) {
                return true;
            }
            LIMIT_CACHE.put(path, count + 1, new Date(System.currentTimeMillis() + windowSize * 1000));
        }
        return false;
    }

}
