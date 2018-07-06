package com.huanmedia.videochat.mvp.view.video;

import com.huanmedia.videochat.common.widget.ptr.PtrLayout;
import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.ShortVideoListResults;
import com.huanmedia.videochat.mvp.entity.results.ShortVideoResults;

import java.util.List;

public interface IShortVideoListView extends BaseMVPView {
    /**
     * 获取小视频列表成功
     *
     * @param data
     */
    void getShortVideoListSuccess(ShortVideoListResults data);


    /**
     * 获取小视频列表失败
     *
     * @param msg
     */
    void getShortVideoListFail(String msg);

    /**
     * 获取短视频下拉
     *
     * @return
     */
    PtrLayout getPtrLayoutForShortVideo();

    /**
     * 获取短视频页数
     *
     * @return
     */
    int getPageForShortVideo();

    /**
     * 获取短视频每页数量
     *
     * @return
     */
    int getPageSizeForShortVideo();

    /**
     * 获取短视频是否强制刷新
     *
     * @return
     */
    int getForceForShortVideo();
}
