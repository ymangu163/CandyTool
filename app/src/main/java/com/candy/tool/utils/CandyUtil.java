package com.candy.tool.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * File description
 *
 * @author gao
 * @date 2018/4/26
 */

public class CandyUtil {


    public static void viewApp(Context context, String packageName) {
        if (context == null) {
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
            if (isGooglePlayInstalled(context)) {
                intent.setPackage("com.android.vending");
            }
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
            } catch (Exception e2) {

            }
        } catch (Exception e) {

        }
    }

     public static boolean isGooglePlayInstalled(Context context) {
        return isAppInstalled(context, "com.android.vending");
    }

     public static boolean isAppInstalled(Context context, String packageName) {
        try {
            return context.getPackageManager().getApplicationInfo(packageName, 0) != null;
        } catch (Throwable e) {
            return false;
        }
    }
}
