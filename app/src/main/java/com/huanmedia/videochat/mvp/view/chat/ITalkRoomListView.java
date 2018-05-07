package com.huanmedia.videochat.mvp.view.chat;

import com.huanmedia.videochat.common.widget.ptr.PtrLayout;
import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.TalkRoomListResults;

import java.util.List;

public interface ITalkRoomListView extends BaseMVPView {
    /**
     * 获取房间列表成功
     *
     * @param listData
     */
    void getTalkRoomListSuccess(List<TalkRoomListResults.Item> listData);


    /**
     * 获取房间列表失败
     *
     * @param msg
     */
    void getTalkRoomListFail(String msg);


    /**
     * 获取下拉刷新控件
     *
     * @return
     */
    PtrLayout getPtrLayout();
}
