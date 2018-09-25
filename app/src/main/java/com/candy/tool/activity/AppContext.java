package com.candy.tool.activity;

import android.app.Application;

import com.candy.tool.BuildConfig;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.core.CrashlyticsCore;

import cn.bmob.v3.Bmob;
import io.fabric.sdk.android.Fabric;

/**
 * File description
 *
 * @author gao
 * @date 2018/3/3
 */

public class AppContext extends Application {
    private static AppContext sAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();
        Fabric.with(this,  new Answers(), crashlyticsKit);

        Bmob.initialize(this, "ba902d8002bec5b59362195068e278c7");
        sAppContext = this;
    }

    public static AppContext getInstance() {
        return sAppContext;
    }
}
