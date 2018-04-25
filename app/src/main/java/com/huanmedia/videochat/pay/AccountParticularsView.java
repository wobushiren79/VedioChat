package com.huanmedia.videochat.pay;

import com.huanmedia.videochat.repository.entity.BillDetialEntity;
import com.huanmedia.videochat.repository.entity.CashListEntity;

import mvp.view.LoadDataView;

/**
 * Create by Administrator
 * time: 2017/12/26.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

interface AccountParticularsView extends LoadDataView{
    /**
     * 更具类型显示数据
     * @param position 0 提现列表 1消费明细
     * @param billDetialEntity
     */
    void showDataForType(int position, BillDetialEntity billDetialEntity);

    void showHint(int fragmentType,int type, String generalErrorStr);
}
