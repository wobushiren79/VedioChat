package com.huanmedia.videochat.my;

import com.huanmedia.videochat.repository.entity.MyLevelEntity;

import mvp.view.LoadDataView;

/**
 * Create by Administrator
 * time: 2018/3/12.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

interface LevelView extends LoadDataView{
    void showLevelData(MyLevelEntity myLevelEntity);
}
