package com.huanmedia.videochat.mvp.view.audio;

import com.huanmedia.videochat.mvp.base.BaseMVPView;

public interface IAudioPlayView extends BaseMVPView {
    /**
     * 音频播放完毕
     */
    void audioPlayComplete();

    void audioPlayDuration(int duration);
}
