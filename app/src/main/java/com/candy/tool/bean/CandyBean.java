package com.candy.tool.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * File description
 *
 * @author gao
 * @date 2018/3/4
 */

public class CandyBean extends BmobObject {
    private String name;
    private String urlPrefix;
    private String description;
    private Integer priority = 3;
    private boolean canRecommend = true;
    private List<InviteUrl> inviteUrls;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public boolean isCanRecommend() {
        return canRecommend;
    }

    public void setCanRecommend(boolean canRecommend) {
        this.canRecommend = canRecommend;
    }

    public List<InviteUrl> getInviteUrls() {
        return inviteUrls;
    }

    public void setInviteUrls(List<InviteUrl> inviteUrls) {
        this.inviteUrls = inviteUrls;
    }
}
