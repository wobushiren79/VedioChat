package com.huanmedia.videochat.repository.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Create by Administrator
 * time: 2018/3/7.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class TrustValueEntity implements Serializable {

    /**
     * trustvalue : 98
     * items : {"good":[{"uid":3,"tagid":2,"tagcount":1,"type":"GOOD","tag":"大长腿"},{"uid":3,"tagid":4,"tagcount":1,"type":"GOOD","tag":"高颜值"}],"bad":[]}
     */

    private int trustvalue;
    private List<BusinessCardUserTags.TagEntity> good;
    private List<BusinessCardUserTags.TagEntity> bad;

    public List<BusinessCardUserTags.TagEntity> getGood() {
        return good;
    }

    public void setGood(List<BusinessCardUserTags.TagEntity> good) {
        this.good = good;
    }

    public List<BusinessCardUserTags.TagEntity> getBad() {
        return bad;
    }

    public void setBad(List<BusinessCardUserTags.TagEntity> bad) {
        this.bad = bad;
    }
    public int getTrustvalue() {
        return trustvalue;
    }

    public void setTrustvalue(int trustvalue) {
        this.trustvalue = trustvalue;
    }
}
