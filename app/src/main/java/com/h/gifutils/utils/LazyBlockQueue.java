package com.h.gifutils.utils;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class LazyBlockQueue extends LinkedBlockingDeque<Runnable> implements RejectedExecutionHandler {
    public static final int MODE_FIFO = 0;
    public static final int MODE_FILO = 1;
    private int mode = MODE_FIFO;

    @Override
    public boolean offer(Runnable t) {
        return false;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (mode == MODE_FIFO) {
            offerLast(r);
        }
        else {
            offerFirst(r);
        }
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
