package com.example.user.jiancan;

import android.app.Application;

import com.mob.MobSDK;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //MobTach初始化
        MobSDK.init(this);
        MobSDK.submitPolicyGrantResult(true, null);
    }
}
