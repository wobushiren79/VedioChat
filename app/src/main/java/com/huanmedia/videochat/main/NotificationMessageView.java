package com.huanmedia.videochat.main;

import com.huanmedia.videochat.main.mode.SystemMessage;

import java.util.List;

import mvp.view.LoadDataView;

/**
 * Create by Administrator
 * time: 2018/1/3.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

interface NotificationMessageView extends LoadDataView{
    /**
     * 显示分页数据
     * @param systemMessages
     */
    void showData(List<SystemMessage> systemMessages);
}
