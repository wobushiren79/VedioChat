package com.huanmedia.videochat.common.service.socket;

import android.util.Log;

import com.google.gson.Gson;
import com.huanmedia.ilibray.utils.GsonUtils;
import com.orhanobut.logger.Logger;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.channels.NotYetConnectedException;
import java.util.Map;

/**
 * Create by Administrator
 * time: 2017/11/28.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class WSocketClent extends WebSocketClient {
    private WSocketLisenter mWSocketLisenter;
    private Gson mGson = GsonUtils.getDefGsonBulder().create();
    public WSocketClent(URI serverUri) {
        this(serverUri, null);
    }

    public WSocketClent(URI serverUri, Draft protocolDraft) {
        this(serverUri, protocolDraft, null, 10);
    }

    public WSocketClent(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);
    }

    public void send(WMessage message) throws NotYetConnectedException {
        send(mGson.toJson(message));
    }

    @Override
    public void send(String text) throws NotYetConnectedException {
        super.send(text);
        Logger.i("WSocketClent:\n%s:%s", "send", text);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Logger.i("WSocketClent:\n%s:%s", "onOpen:", handshakedata.toString());
        if (mWSocketLisenter != null) {
            mWSocketLisenter.onOpen(handshakedata);
        }
    }

    @Override
    public void onMessage(String message) {
        Logger.i("WSocketClent:\n%s:%s", "onMessage", message);
        WMessage oMsg = mGson.fromJson(message, WMessage.class);
        if (oMsg.getType().equals("ping")) {
            send("{\"type\":\"pong\"}");
        } else {
            if (mWSocketLisenter != null) {
                mWSocketLisenter.onMessage(oMsg);
            }
        }

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Logger.i("WSocketClent:\n%s:%d", "onClose", code);
        if (mWSocketLisenter != null) {
            mWSocketLisenter.onClose(code, reason, remote);
        }
    }

    @Override
    public void onError(Exception ex) {
        Logger.i("WSocketClent:\n%s", Log.getStackTraceString(ex));
        if (mWSocketLisenter != null) {
            mWSocketLisenter.onError(ex);
        }
    }

    public void setWSocketLisenter(WSocketLisenter WSocketLisenter) {
        mWSocketLisenter = WSocketLisenter;
    }

    public static interface WSocketLisenter {
        public void onOpen(ServerHandshake handshakedata);

        /**
         * UI线程调用
         *
         * @param message
         */
        public void onMessage(WMessage message);

        public void onClose(int code, String reason, boolean remote);

        /**
         * UI线程调用
         *
         * @param ex
         */
        public void onError(Exception ex);
    }
}
