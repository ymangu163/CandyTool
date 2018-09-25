package com.candy.tool.bean;

import cn.bmob.v3.BmobObject;

/**
 * File description
 *
 * @author gao
 * @date 2018/9/3
 */

public class Upgrade extends BmobObject {
    private int versioncode;
    private String download;
    private String description;
    private boolean force;

    public int getVersioncode() {
        return versioncode;
    }

    public String getDownload() {
        return download;
    }

    public String getDescription() {
        return description;
    }

    public boolean isForce() {
        return force;
    }
}
