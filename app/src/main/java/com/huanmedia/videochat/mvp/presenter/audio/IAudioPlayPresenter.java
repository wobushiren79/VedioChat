package com.huanmedia.videochat.mvp.presenter.audio;

import android.media.MediaPlayer;

public interface IAudioPlayPresenter {
    /**
     * 准备播放
     */
    MediaPlayer prePlay(String resPath);

    /**
     * 开始播放
     */
    MediaPlayer startPlay();

    /**
     * 暂停播放
     */
    void pausePlay();

    /**
     * 释放播放
     */
    void releasePlay();

    /**
     * 获取总播放时长
     *
     * @return
     */
    int getPlayTotalTime();
}
