package com.huanmedia.videochat.mvp.view.video;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.repository.entity.VideoEntity;

import java.util.List;

public interface IUserVideoListView extends BaseMVPView {
    /**
     * 获取用户视频列表成功
     * @param listData
     */
    void getUserVideoListSuccess(List<VideoEntity> listData);

    /**
     * 获取用户视频列表失败
     * @param msg
     */
    void getUserVideoListFail(String msg);
}
