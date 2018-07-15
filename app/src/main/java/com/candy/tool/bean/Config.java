package com.candy.tool.bean;

import cn.bmob.v3.BmobObject;

/**
 * File description
 *
 * @author gao
 * @date 2018/7/14
 */

public class Config extends BmobObject {

    private int versionCode;
    private String faq;

    public int getVersionCode() {
        return versionCode;
    }

    public String getFaq() {
        return faq;
    }
}
