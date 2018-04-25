package com.huanmedia.videochat.my;

import java.util.Map;

import mvp.view.LoadDataView;

/**
 * Create by Administrator
 * time: 2018/3/8.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

interface ReadMainView extends LoadDataView {
    void showReadMainConfig(Map<String, String> result);

    void setReadManChatPrice(String price);
}
