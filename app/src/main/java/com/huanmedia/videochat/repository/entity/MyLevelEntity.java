package com.huanmedia.videochat.repository.entity;

import java.io.Serializable;

/**
 * Create by Administrator
 * time: 2018/3/12.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class MyLevelEntity implements Serializable {

    /**
     * level : 5
     * experience : 158
     * privilege : 免费搜索次数4次
     */

    private int level;
    private int experience;
    private String privilege;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }
}
