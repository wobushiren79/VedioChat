package com.huanmedia.videochat.main.fragment;

import com.huanmedia.videochat.repository.entity.ChatPeopleEntity;

import mvp.view.BaseView;
import mvp.view.LoadDataView;

/**
 * Create by Administrator
 * time: 2017/12/18.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

interface ChatPeopleView extends LoadDataView{
    void showDatas(ChatPeopleEntity chatPeopleEntity);
}
