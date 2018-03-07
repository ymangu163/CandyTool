package com.candy.tool.bean;

import java.io.Serializable;

/**
 * File description
 *
 * @author gao
 * @date 2018/3/6
 */

public class InviteUrl implements Serializable {
    private String urlContent;
    private int chance;

    public InviteUrl(String urlContent, int chance) {
        this.urlContent = urlContent;
        this.chance = chance;
    }

    public String getUrlContent() {
        return urlContent;
    }

    public void setUrlContent(String urlContent) {
        this.urlContent = urlContent;
    }

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }
}
