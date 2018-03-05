package com.candy.tool.activity;

import android.app.Application;

import com.candy.tool.R;

import cn.bmob.v3.Bmob;

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

        Bmob.initialize(this, getString(R.string.bomb_key),"bmob");


    }
}
