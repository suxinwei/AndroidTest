package com.example.suxinwei.authcode;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * created by suxinwei at 2020-02-04
 * description:
 */
public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.e("sxw", "Application attachBaseContext 启动");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("sxw", "Application 启动");
    }
}
