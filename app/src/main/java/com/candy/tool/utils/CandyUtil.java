package com.candy.tool.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Locale;

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

    public static String getJsonFromAssets(Context context, String name) {
        try {
            return readString(context.getAssets().open(name));
        } catch (Throwable e) {
            return null;
        }
    }

    private static String readString(InputStream is) throws IOException {

        InputStreamReader reader = new InputStreamReader(is, "UTF-8");
        StringBuilder out = new StringBuilder();

        try {
            int len = 0;
            char buffer[] = new char[8 * 1024];

            while ((len = reader.read(buffer, 0, buffer.length)) > 0) {
                out.append(buffer, 0, len);
            }
        } catch (EOFException e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }

        return out.toString();
    }

    public static boolean isSameDay(long time) {
        long timeMillis = System.currentTimeMillis();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String sp_time = sf.format(time);
        String current_time = sf.format(timeMillis);

        return sp_time.equals(current_time);
    }
}
