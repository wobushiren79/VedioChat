package com.huanmedia.videochat.mvp.view.audio;

import com.huanmedia.videochat.mvp.base.BaseMVPView;

public interface IAudioAddView extends BaseMVPView {
    /**
     * 增加音频文件成功
     */
    void addAudioSuccess();

    /**
     * 增加音频文件失败
     * @param msg
     */
    void addAudioFail(String msg);
}
