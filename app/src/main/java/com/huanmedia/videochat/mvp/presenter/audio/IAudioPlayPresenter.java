package com.huanmedia.videochat.mvp.presenter.audio;

import android.media.MediaPlayer;

public interface IAudioPlayPresenter {
    /**
     * 开始播放
     */
    MediaPlayer startPlay(String resPath);

    /**
     * 暂停播放
     */
    void pausePlay();

    /**
     * 释放播放
     */
    void releasePlay();
}
