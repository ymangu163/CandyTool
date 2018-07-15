package com.tool.librecycle.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * File description
 *
 * @author gao
 * @date 2018/3/6
 */

public class CommonUtil {

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "1.0";
        }
    }

    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            return -1;
        }
    }

}
