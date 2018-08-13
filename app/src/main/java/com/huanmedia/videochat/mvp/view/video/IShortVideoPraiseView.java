package com.huanmedia.videochat.mvp.view.video;

import com.huanmedia.videochat.mvp.base.BaseMVPView;

public interface IShortVideoPraiseView extends BaseMVPView {

    /**
     * 短视频点赞成功
     */
    void shortVideoPraiseSuccess();

    /**
     * 短视频点赞失败
     * @param msg
     */
    void shortVideoPraiseFail(String msg);

    /**
     * 短视频取消点赞成功
     */
    void shortVideoCancelPraiseSuccess();

    /**
     * 短视频取消点赞失败
     * @param msg
     */
    void shortVideoCanelPraiseFail(String msg);


}
