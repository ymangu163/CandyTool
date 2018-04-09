package com.candy.tool.activity;

import android.app.Application;

import com.candy.tool.BuildConfig;
import com.candy.tool.R;
import com.crashlytics.android.Crashlytics;
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

    @Override
    public void onCreate() {
        super.onCreate();
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();
        Fabric.with(this, crashlyticsKit);

        Bmob.initialize(this, getString(R.string.bomb_key),"bmob");


    }
}
