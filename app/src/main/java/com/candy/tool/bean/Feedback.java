package com.candy.tool.bean;

import cn.bmob.v3.BmobObject;

/**
 * File description
 *
 * @author gao
 * @date 2018/4/26
 */

public class Feedback extends BmobObject {
    private String content;
    private String email;

    public void setContent(String content) {
        this.content = content;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
