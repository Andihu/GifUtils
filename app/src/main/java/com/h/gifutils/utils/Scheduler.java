package com.h.gifutils.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface Scheduler {
    void execute(Runnable runnable);
    <T> Future<T> submit(Callable<T> callable);

    void execute(Runnable runnable, long delay);
}
