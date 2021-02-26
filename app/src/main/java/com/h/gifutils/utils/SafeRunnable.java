package com.h.gifutils.utils;

import android.util.Log;


public class SafeRunnable implements Runnable {
    private Runnable runnable;

    public SafeRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void run() {
        // cache exception to avoid crash
        try {
            runnable.run();
        }
        catch (Throwable e) {
            Log.e("Scheduler", "call code exception: %s", e);
        }
    }
}
