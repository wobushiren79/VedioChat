package com.huanmedia.videochat.chat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huanmedia.ilibray.utils.GsonUtils;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.service.socket.WMessage;
import com.huanmedia.videochat.common.service.socket.WSonMessageListener;
import com.huanmedia.videochat.common.service.socket.WebSocketManager;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ChatMessage implements WSonMessageListener {

    private Gson mGson = GsonUtils.getDefGsonBulder().create();
    private CallBack mCallBack;

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    @Override
    public void onMessage(WMessage message) {
        if (!message.getType().equals("ChatMessage") || mCallBack == null)
            return;
        Map<String, String> contents =
                mGson.fromJson(message.getBody().toString(), new TypeToken<TreeMap<String, String>>() {
                }.getType());
        switch (contents.get("Command")) {
            case "RefreshData":
                mCallBack.websocketRefreshData();
                break;
        }

    }

    @Override
    public void onError(Throwable ex) {

    }

    /**
     * 通知刷新最新数据
     * @param chatUserId
     */
    public void msgRefreshData(int chatUserId) {
        WMessage wMessage = new WMessage();
        wMessage.setFrom(UserManager.getInstance().getId() + "");
        wMessage.setTo(chatUserId + "");
        wMessage.setType("ChatMessage");
        Map<String, Object> map = new HashMap<>();
        map.put("Command", "RefreshData");
        wMessage.setBody(map);
        WebSocketManager.getInstance().sendMessage(wMessage);
    }

    public interface CallBack {
        void websocketRefreshData();
    }
}
