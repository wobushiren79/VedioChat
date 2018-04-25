package com.huanmedia.videochat.pay;

import mvp.view.BaseView;
import mvp.view.LoadDataView;

/**
 * Create by Administrator
 * time: 2017/12/25.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

interface AccountBoundView extends LoadDataView{
    /**
     * 用户绑定参数
     * @param result
     */
    void beforBindData(Object result);

    /**
     * 用户绑定成功
     * @param type 绑定类型
     */
    void boundSuccess(int type);

    /**
     * 账户解绑成功
     */
    void unBoundSuccess();
}
