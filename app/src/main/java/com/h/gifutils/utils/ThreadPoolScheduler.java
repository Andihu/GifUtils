package com.h.gifutils.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

public class ThreadPoolScheduler implements Scheduler {
    private LazyBlockQueue queue;
    private ExecutorService executor;

    /**
     * 构造函数
     * @param name 线程名称
     * @param count 线程数量
     */
    public ThreadPoolScheduler(String name, int count) {
        this(name, count, count);
    }

    /**
     * 构造函数
     * @param name 线程名称
     * @param coreSize 核心线程数量
     * @param count 线程数量
     */
    public ThreadPoolScheduler(String name, int coreSize, int count) {
        this(name, THREAD_PRIORITY_BACKGROUND, coreSize, count);
    }

    /**
     * 构造函数
     * @param name 线程名称
     * @param priority 线程优先级
     * @param coreSize 核心线程数量
     * @param count 线程数量
     */
    public ThreadPoolScheduler(String name, int priority, int coreSize, int count) {
        queue = new LazyBlockQueue();
        long keepAliveTime = coreSize == count ? 0L : 60L;
        executor = new ThreadPoolExecutor(coreSize, count, keepAliveTime, TimeUnit.SECONDS, queue, new NamedThreadFactory(name, priority), queue);
    }

    /**
     * 设置IN Queue模式
     * @param mode 先入先出 LazyBlockQueue.MODE_FIFO，先入后出 LazyBlockQueue.MODE_FILO
     */
    public void setMode(int mode) {
        queue.setMode(mode);
    }

    @Override
    public void execute(Runnable runnable) {
        executor.execute((runnable instanceof SafeRunnable) ? runnable : new SafeRunnable(runnable));
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        return executor.submit(callable instanceof SafeCallable ? callable : new SafeCallable<>(callable));
    }

    @Override
    public void execute(final Runnable runnable, long delay) {
        Dispatcher.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                execute(runnable);
            }
        }, delay);
    }
}
