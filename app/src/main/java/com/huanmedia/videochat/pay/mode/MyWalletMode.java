package com.huanmedia.videochat.pay.mode;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.huanmedia.videochat.repository.entity.BillDetialEntity;

/**
 * Create by Administrator
 * time: 2018/3/6.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class MyWalletMode extends SectionEntity<BillDetialEntity.ItemsEntity> {

    public MyWalletMode(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public MyWalletMode(BillDetialEntity.ItemsEntity billDetialEntity) {
        super(billDetialEntity);
    }

}
