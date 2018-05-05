package com.candy.tool.utils;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by gao on 2017/8/24.
 */

public class GsonUtil {

    public static String getGsonString(Object object) {
        if (object == null) {
            return "";
        }
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static <T> T gson2Bean(String gsonString, Class<T> cls) {
        if (TextUtils.isEmpty(gsonString)) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(gsonString, cls);
    }

    public static <T> List<T> gson2List(String gsonString, Class<T> cls) {
        if (TextUtils.isEmpty(gsonString)) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(gsonString, new ListOfJson<T>(cls));
    }

}
