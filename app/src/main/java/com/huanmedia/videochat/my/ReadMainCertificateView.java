package com.huanmedia.videochat.my;

import mvp.view.LoadDataView;

/**
 * Create by Administrator
 * time: 2018/3/7.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

interface ReadMainCertificateView extends LoadDataView{
    /**
     * 进入下一步
     */
    void showNext(String msg,boolean isCompleteness);
}
