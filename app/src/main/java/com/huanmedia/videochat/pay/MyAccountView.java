package com.huanmedia.videochat.pay;

import com.huanmedia.videochat.repository.entity.UserAccountBoundEntity;

import mvp.view.LoadDataView;

/**
 * Create by Administrator
 * time: 2017/12/21.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

interface MyAccountView extends LoadDataView{
    public void showHint(int type, String hint);

    /**
     * 账户数据显示
     * @param userAccountBoundEntity
     */
    void showData(UserAccountBoundEntity userAccountBoundEntity);
}
