package com.candy.tool.activity;

import android.app.Application;

import com.candy.tool.R;
import com.crashlytics.android.Crashlytics;

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
        Fabric.with(this, new Crashlytics());

        Bmob.initialize(this, getString(R.string.bomb_key),"bmob");


    }
}
