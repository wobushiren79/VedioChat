package com.huanmedia.videochat.mvp.presenter.audio;


import com.huanmedia.videochat.mvp.entity.request.AudioInfoRequest;

public interface IAudioRecordPresenter {
    /**
     * 开始录制音频
     */
    void startRecord();

    /**
     * 停止录制
     */
    void stopRecord();

    /**
     * 获取录取的音频数据
     * @return
     */
    AudioInfoRequest getAudioInfo();
}
