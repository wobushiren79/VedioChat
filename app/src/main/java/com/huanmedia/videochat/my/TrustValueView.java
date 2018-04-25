package com.huanmedia.videochat.my;

import com.huanmedia.videochat.repository.entity.TrustValueEntity;

import mvp.view.LoadDataView;

/**
 * Create by Administrator
 * time: 2018/3/5.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

interface TrustValueView extends LoadDataView{
    void showTrueData(TrustValueEntity trustValueEntity);
}
