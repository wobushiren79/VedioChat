package com.huanmedia.videochat.repository.entity;

import java.util.List;

/**
 * Create by Administrator
 * time: 2017/12/28.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class OccupationsEntity {

    private List<ItemMenuEntity> occupation;

    public List<ItemMenuEntity> getOccupation() {
        return occupation;
    }

    public void setOccupation(List<ItemMenuEntity> occupation) {
        this.occupation = occupation;
    }
}
