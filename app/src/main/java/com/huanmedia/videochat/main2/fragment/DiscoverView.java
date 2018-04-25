package com.huanmedia.videochat.main2.fragment;

import com.huanmedia.videochat.repository.entity.DiscoverPageEntity;

import mvp.view.LoadDataView;

/**
 * Create by Administrator
 * time: 2017/12/4.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

interface DiscoverView extends LoadDataView{
     void resultDiscoverData(int flag, DiscoverPageEntity discoverEntities);

    void loadDataError(int type,String msg);

    void svComputeScroll();
}
