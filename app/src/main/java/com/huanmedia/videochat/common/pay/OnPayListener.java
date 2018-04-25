package com.huanmedia.videochat.common.pay;


/**
 * Create by Administrator
 * time: 2017/12/17.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public interface OnPayListener {
    public void onSuccess(String msg);

    void onError(String msg);
}
