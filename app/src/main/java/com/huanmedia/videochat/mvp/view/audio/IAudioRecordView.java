package com.huanmedia.videochat.mvp.view.audio;

import com.huanmedia.videochat.mvp.base.BaseMVPView;

public interface IAudioRecordView extends BaseMVPView {
    /**
     * 录音开始
     */
    void audioRecordStart();

    /**
     * 录音时间
     */
    void audioRecordDuration(int duration,byte[] db);

    /**
     * 录音结束
     */
    void audioRecordStop();

    /**
     * 录音错误
     *
     * @param msg
     */
    void audioRecordError(String msg);
}
