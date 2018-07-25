package com.huanmedia.videochat.mvp.view.chat;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.ChatListResults;
import com.huanmedia.videochat.mvp.entity.results.UserInfoResults;

import java.util.List;

public interface IChatListView extends BaseMVPView {
    /**
     * 获取聊天列表成功
     *
     * @param results
     */
    void getChatListSuccess(ChatListResults results);

    /**
     * 获取聊天列表失败
     *
     * @param msg
     */
    void getChatListFail(String msg);

    /**
     * 获取聊天对象ID
     *
     * @return
     */
    int getChatUserId();

    /**
     * 设置新的聊天信息数据
     *
     * @param listData
     */
    void setNewChatListData(List<ChatListResults.Item> listData);

    /**
     * 设置历史聊天信息
     *
     * @param listData
     */
    void setHistoryChatListData(List<ChatListResults.Item> listData);



    /**
     * 设置自己的数据
     */
    void setSelfData(UserInfoResults results);

    /**
     * 设置其他人的数据
     */
    void setOtherData(UserInfoResults results);
}
