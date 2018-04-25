package com.huanmedia.videochat.pay;

import com.huanmedia.videochat.repository.entity.PayCoinTypeMode;

import java.util.List;

import mvp.view.LoadDataView;

/**
 * Create by Administrator
 * time: 2017/12/17.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

interface CoinPayView extends LoadDataView {

    void showData(List<PayCoinTypeMode> coinPayEntity);

    void createOlderSuccess(PayCoinTypeMode payCoinTypeMode, String olderNum);

}
