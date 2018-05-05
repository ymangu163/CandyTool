package com.tool.librecycle.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gao on 2017/4/10.
 * FreeNet 的SharePreference
 */

public class CommonSharePref {
    private SharedPreferences sharedPreferences = null;
    private static CommonSharePref mSharePref;

    /************************定义 KEY*****************************/
    private final String FREENET_SHAREPREFERENCE = "common_sharepreference";

    private final String KEY_CANDY_FIRST_TIME = "key_candy_first_time";
    private final String KEY_CANDY_LAST_TIME = "key_candy_last_time";
    private final String KEY_MARK_URL_TIME = "key_mark_url_time";
    private final String KEY_MASK_URL_CONTENT = "key_mask_url_content";
    private final String KEY_REFRESH_RESULT = "key_refresh_result";

    /************************结束定义 KEY*****************************/

    public static CommonSharePref getInstance(Context context) {
        if (mSharePref == null) {
            synchronized(CommonSharePref.class) {
                if (mSharePref == null) {
                    mSharePref = new CommonSharePref(context);
                }
            }
        }
        return mSharePref;
    }
    private CommonSharePref(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(FREENET_SHAREPREFERENCE, Context.MODE_PRIVATE);
    }

    private  void putBoolean(String keyString, boolean value) {
        sharedPreferences.edit().putBoolean(keyString, value).apply();
    }
    private boolean getBoolean(String keyString) {
        return sharedPreferences.getBoolean(keyString, false);
    }

    private boolean getBoolean(String keyString, boolean defValue) {
        return sharedPreferences.getBoolean(keyString, defValue);
    }

    private  void putString(String keyString, String value) {
        sharedPreferences.edit().putString(keyString, value).apply();
    }
    private String getString(String keyString) {
        return sharedPreferences.getString(keyString, "");
    }

    private  void putInt(String keyString, int value) {
        sharedPreferences.edit().putInt(keyString, value).apply();
    }
    private int getInt(String keyString) {
        return sharedPreferences.getInt(keyString, -1);
    }

    private int getInt(String keyString, int defaultValue) {
        return sharedPreferences.getInt(keyString, defaultValue);
    }

    private  void putLong(String keyString, long value) {
        sharedPreferences.edit().putLong(keyString, value).apply();
    }
    private long getLong(String keyString) {
        return sharedPreferences.getLong(keyString, -1);
    }

    private long getLong(String keyString, long defValue) {
        return sharedPreferences.getLong(keyString, defValue);
    }
    /**----------------------------------------------------------------------------------------- **/

    public void setCandyFirstTime(String value) {
        putString(KEY_CANDY_FIRST_TIME, value);
    }

    public String getCandyFirstTime() {
        return getString(KEY_CANDY_FIRST_TIME);
    }

    public void setCandyLastTime(String value) {
        putString(KEY_CANDY_LAST_TIME, value);
    }

    public String getCandyLastTime() {
        return getString(KEY_CANDY_LAST_TIME);
    }

    public long getMaskUrlTime() {
        return getLong(KEY_MARK_URL_TIME, 0);
    }

    public void setMaskUrlTime(long value) {
        putLong(KEY_MARK_URL_TIME, value);
    }

    public String getMaskUrlContent() {
        return getString(KEY_MASK_URL_CONTENT);
    }

    public void setMaskUrlContent(String value) {
         putString(KEY_MASK_URL_CONTENT, value);
    }

    public String getRefreshResult() {
        return getString(KEY_REFRESH_RESULT);
    }

    public void setRefreshResult(String value) {
         putString(KEY_REFRESH_RESULT, value);
    }
}
