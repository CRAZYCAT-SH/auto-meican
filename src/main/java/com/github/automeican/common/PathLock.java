package com.github.automeican.common;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PathLock {
    // 使用ConcurrentHashMap存储路径对应的锁
    private final ConcurrentHashMap<String, Lock> lockMap = new ConcurrentHashMap<>();

    /**
     * 获取指定路径的锁
     * @param path 需要加锁的路径
     * @return 对应的Lock对象
     */
    public Lock getLock(String path) {
        // computeIfAbsent保证线程安全地获取或创建锁
        return lockMap.computeIfAbsent(path, k -> new ReentrantLock());
    }

    /**
     * 使用try-with-resources风格的锁获取方式
     * @param path 需要加锁的路径
     * @return 可自动释放的锁资源
     */
    public AutoCloseableLock lock(String path) {
        Lock lock = getLock(path);
        lock.lock();
        return new AutoCloseableLock(lock);
    }

    /**
     * 自动释放锁的帮助类
     */
    public static class AutoCloseableLock implements AutoCloseable {
        private final Lock lock;

        public AutoCloseableLock(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void close() {
            lock.unlock();
        }
    }
}
