package com.bqteam.appforlearn.app;

import android.app.Application;
import android.content.Context;

/**
 *
 * @author charles
 * @date 15/11/19
 * 全局获取context
 */

public class MyApplication extends Application {

    private static Context context;

    public MyApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

}
