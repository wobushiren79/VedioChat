package com.huanmedia.videochat.common.service.socket;

/**
 * Create by Administrator
 * time: 2017/11/28.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public interface WSonMessageListener {
    public void onMessage(WMessage message);

    void onError(Throwable ex);
}
