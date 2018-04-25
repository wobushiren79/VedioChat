package com.huanmedia.videochat.pay;

import com.huanmedia.videochat.repository.entity.BillDetialEntity;

import mvp.view.LoadDataView;

/**
 * Create by Administrator
 * time: 2018/3/6.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

interface MyWalletView extends LoadDataView{
    void showDataForType(@MyWalletFragment.WallPageType int type, BillDetialEntity cashListEntity);

    void showHint(@MyWalletFragment.WallPageType int type, int hintType, String generalErrorStr);
}
