package com.huanmedia.videochat.wxapi;

import com.huanmedia.videochat.repository.entity.PayOlderEntity;

import mvp.view.BaseView;

/**
 * Create by Administrator
 * time: 2017/12/17.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

interface PayOrderView extends BaseView{
    /**
     * 调用第三方支付
     * @param payOlderEntity
     */
    void doingPay(PayOlderEntity payOlderEntity);

    void showLoading();

    void error(String message);

    void hideLoading();
}
