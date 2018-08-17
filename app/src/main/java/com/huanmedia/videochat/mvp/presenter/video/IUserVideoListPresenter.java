package com.huanmedia.videochat.mvp.presenter.video;

public interface IUserVideoListPresenter {

    /**
     * 获取所有视频
     */
    void getAllVideo();

    /**
     * 获取公开视频
     */
    void getOpenVideo();

    /**
     * 获取隐私视频
     */
    void getSecretVideo();
}
