package com.h.gifutils.utils;

import java.util.concurrent.ThreadFactory;

public class NamedThreadFactory implements ThreadFactory {
    private static int threadNumber = 0;
    private String name;
    private int priority;

    public NamedThreadFactory(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    synchronized public Thread newThread(Runnable r) {
        return new Thread(r, name + "-" + String.valueOf(threadNumber++)) {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(priority);
                super.run();
            }
        };
    }
}
