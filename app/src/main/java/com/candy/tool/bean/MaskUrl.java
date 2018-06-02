package com.candy.tool.bean;

import cn.bmob.v3.BmobObject;

/**
 * File description
 *
 * @author gao
 * @date 2018/4/26
 */

public class MaskUrl extends BmobObject {
    private String maskurl;
    private String recommendPreUrl;

    public String getMaskurl() {
        return maskurl;
    }

    public void setMaskurl(String maskurl) {
        this.maskurl = maskurl;
    }

    public String getRecommendPreUrl() {
        return recommendPreUrl;
    }
}
