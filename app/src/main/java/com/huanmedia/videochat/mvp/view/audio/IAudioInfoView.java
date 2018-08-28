package com.huanmedia.videochat.mvp.view.audio;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.AudioFileResults;

public interface IAudioInfoView extends BaseMVPView {
    /**
     * 获取音频信息成功
     * @param results
     */
    void getAudioInfoSuccess(AudioFileResults results);

    /**
     * 获取音频信息失败
     * @param msg
     */
    void getAudioInfoFail(String msg);
}
