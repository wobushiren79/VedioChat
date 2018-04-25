package com.huanmedia.videochat.common.service.socket;

import org.java_websocket.handshake.ServerHandshake;

/**
 * Create by Administrator
 * time: 2017/11/28.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public interface WSConnListener {
    public void onOpen(ServerHandshake handshakedata);
    public void onClose(int code, String reason, boolean remote);
}
