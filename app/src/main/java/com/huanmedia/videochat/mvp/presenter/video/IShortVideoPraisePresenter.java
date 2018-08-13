package com.huanmedia.videochat.mvp.presenter.video;

public interface IShortVideoPraisePresenter {
    /**
     * 短视频点赞
     *
     * @param videoId
     */
    void shortVideoPraise(int videoId);


    /**
     * 短视频取消点赞
     * @param videoId
     */
    void shortVideoCancelPraise(int videoId);
}
