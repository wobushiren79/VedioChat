package com.huanmedia.videochat.common.service.socket;

import android.os.Handler;

import com.huanmedia.ilibray.utils.Installation;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.FApplication;
import com.huanmedia.videochat.common.manager.ActivitManager;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.repository.net.HostManager;
import com.huanmedia.videochat.video.CallingActivity;
import com.orhanobut.logger.Logger;

import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Websocket 管理类管理Websocket监听登录和用户绑定
 * 发送消息等
 * Create by Administrator
 * time: 2017/11/23.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class WebSocketManager {
    private volatile static WebSocketManager INSTANCE = null;
    private WSConfig mConfig;
    private WSocketClent mWSocketClent;
    private Set<WSConnListener> mWSConnListeners;
    private Set<WSonMessageListener> mWSonMessageListeners;
    private volatile boolean isDestory;
    private static Handler mHandler = new Handler(FApplication.getApplication().getMainLooper());


    private WebSocketManager() {
        this.mWSConnListeners = new HashSet<>();
        this.mWSonMessageListeners = new HashSet<>();
    }

    public static WebSocketManager getInstance() {
        if (INSTANCE == null) {
            synchronized (WebSocketManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WebSocketManager();
                }
            }
        }
        return INSTANCE;
    }

    public void addConnectionListener(WSConnListener connListener) {
        this.mWSConnListeners.add(connListener);
    }

    public void addMessageListener(WSonMessageListener connListener) {
        this.mWSonMessageListeners.add(connListener);
    }


    public synchronized void connection(WSConfig config) {
        if (config == null) {
            throw new RuntimeException("连接配置不能为空");
        }
        this.mConfig = config;

        if (mWSocketClent == null) {
            this.mWSocketClent = new WSocketClent(mConfig.getHost(), new Draft_6455(), null, mConfig.getConntimeOut());
            mWSocketClent.setWSocketLisenter(new WSocketClent.WSocketLisenter() {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    aroundOpen(handshakedata);
                }

                @Override
                public void onMessage(WMessage message) {
                    //强制下线
                    if (message.getType().equals("forceLogout")) {
                        forceLoginOut(message);
                        return;
                    }
                    //回执报文
                    if (message.getFlag() == 1) {
                        WMessage receiptMessage = new WMessage();
                        receiptMessage.setType("msgReceipt");
                        Map<String, Object> map = new HashMap<>();
                        map.put("sign", message.getSign());
                        receiptMessage.setBody(map);
                        sendMessage(receiptMessage);
                    }
                    mHandler.post(() -> aroundMessage(message));
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    aroundClose(code, reason, remote);
                }

                @Override
                public void onError(Exception ex) {
                    mHandler.post(() -> aroundError(ex));
                }
            });
            if (mWSocketClent != null)
                mWSocketClent.connect();
        }
    }

    /**
     * 强制下线
     */
    private void forceLoginOut(WMessage message) {
        destory();
        @SuppressWarnings("unchecked")
        Map<String, Object> jsonMap = message.getBody();

        UserManager.getInstance().outLogin(jsonMap);
        if (ActivitManager.getAppManager().currentActivity() instanceof CallingActivity) {
            ActivitManager.getAppManager().currentActivity().finish();
        } else {
            ((BaseActivity) ActivitManager.getAppManager().currentActivity()).showFouceExitDialog();
        }

    }

    /**
     * 绑定用户
     */
    public void bindUser() {
        WMessage message = new WMessage();
        message.setType("userBindUid");
        Map<String, Object> map = new HashMap<>();
        map.put("uid", UserManager.getInstance().getId() + "");
        map.put("deviceid", Installation.id(FApplication.getApplication()));
        if (UserManager.getInstance().getCurrentUser().getToken() != null)
            map.put("token", UserManager.getInstance().getCurrentUser().getToken());
        map.put("sysmsgmaxid", UserManager.getInstance().getSystemMsgMaxid());
        message.setBody(map);
        sendMessage(message);
    }


    /**
     * 断线重连
     */
    public void reConnection() {
        if (!UserManager.getInstance().islogin()) return;
        if (mWSocketClent == null) return;
        if (mWSocketClent.isConnecting())
            mWSocketClent.close(1000);
        mWSocketClent = null;
        //重新连接需要更新time参数和Sign参数
        connection(mConfig.newBulede().setParms(HostManager.defaultSocketPrams()).bulide());
    }


    public void sendMessage(WMessage message) {
        if (mWSocketClent != null && mWSocketClent.isOpen() && !mWSocketClent.isClosing()) {
            //添加固定参数配置
            message.setFrom(UserManager.getInstance().getId() + "");
            message.setSid(UserManager.getInstance().getsId());
            message.setTime(Long.parseLong(UserManager.getInstance().reviseSyncTime()));
            Map<String, String> signPrams = new HashMap<>();
            signPrams.put("time", message.getTime() + "");
            signPrams.put("token", UserManager.getInstance().getsId());
            signPrams.put("deviceid", Installation.id(FApplication.getApplication()));

            message.setSign(mConfig.getHandshakePrams(signPrams));

            try {
                mWSocketClent.send(message);
            } catch (Exception e) {
                aroundError(e);
            }
        } else {
            Logger.i("SocketManager:%s", "消息未发送");
        }
    }


    private void aroundError(Throwable ex) {
        if (!isDestory)
            for (WSonMessageListener mWSonMessageListener : mWSonMessageListeners) {
                mWSonMessageListener.onError(ex);
            }
    }

    private void aroundClose(int code, String reason, boolean remote) {
        if (!isDestory)
            for (WSConnListener connListener : mWSConnListeners) {
                connListener.onClose(code, reason, remote);
            }
    }

    private void aroundMessage(WMessage message) {
        if (!isDestory)
            try {
                for (WSonMessageListener mWSonMessageListener : mWSonMessageListeners) {
                    mWSonMessageListener.onMessage(message);
                }
            } catch (Exception e) {

            }
    }

    private void aroundOpen(ServerHandshake handshakedata) {
        if (!isDestory)
            for (WSConnListener connListener : mWSConnListeners) {
                connListener.onOpen(handshakedata);
            }
    }

    public void destory() {
        isDestory = true;
        if (mWSocketClent != null) {
            mWSocketClent.close(1000);
        }
        if (mWSonMessageListeners != null) {
            mWSonMessageListeners.clear();
            mWSonMessageListeners = null;
        }
        if (mWSConnListeners != null) {
            mWSConnListeners.clear();
            mWSConnListeners = null;
        }
        if (mConfig != null) {
            mConfig = null;
        }
        INSTANCE = null;
    }

    public void removeListener(WSonMessageListener wsListener) {
        if (wsListener != null && mWSonMessageListeners != null)
            mWSonMessageListeners.remove(wsListener);
    }

    public void removeListener(WSConnListener wsListener) {
        if (wsListener != null && mWSConnListeners != null)
            mWSConnListeners.remove(wsListener);
    }

    public synchronized WSocketClent getClient() {
        return mWSocketClent;
    }
}
