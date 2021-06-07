package com.example.comicapp;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends Application {
    ExecutorService diskIOExecutor = Executors.newSingleThreadExecutor();

    private static class MainThreadExecutor implements Executor {
        private final Handler mainThreadExecutor = HandlerCompat.createAsync(Looper.getMainLooper());

        public void execute(Runnable runnable) {
            mainThreadExecutor.post(runnable);
        }

    }
}
