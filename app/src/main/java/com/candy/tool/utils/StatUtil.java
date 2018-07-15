package com.candy.tool.utils;

import android.text.TextUtils;
import android.util.Log;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

/**
 * File description
 *
 * @author gao
 * @date 2018/7/15
 */

public class StatUtil {
    private static final boolean DEBUG = true;

    public static void onEvent(String eventId) {
        Answers.getInstance().logCustom(new CustomEvent(eventId));
        printEventDetails(eventId, "");
    }

    public static void onEvent(String eventId, String params) {
        Answers.getInstance().logCustom(new CustomEvent(eventId)
                .putCustomAttribute("value", params));
        printEventDetails(eventId, params);
    }

    private static void printEventDetails(String eventId, String params) {
        if (!DEBUG) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(eventId).append("  ");
        if (!TextUtils.isEmpty(params)) {
            builder.append("value = ")
                    .append(params);

        }
        Log.e("StatUtil", builder.toString());
    }
}
