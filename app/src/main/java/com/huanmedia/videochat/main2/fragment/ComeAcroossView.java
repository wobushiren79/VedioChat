package com.huanmedia.videochat.main2.fragment;

import com.huanmedia.videochat.repository.entity.ChatPeopleEntity;

import mvp.view.LoadDataView;

/**
 * Create by Administrator
 * time: 2017/12/18.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

interface ComeAcroossView extends LoadDataView{
    void showDatas(ChatPeopleEntity chatPeopleEntity);
    void loadMoreError();
    void svComputeScroll();
}
