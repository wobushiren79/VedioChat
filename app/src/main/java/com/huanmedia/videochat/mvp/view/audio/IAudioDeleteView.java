package com.huanmedia.videochat.mvp.view.audio;

        import com.huanmedia.videochat.mvp.base.BaseMVPView;

public interface IAudioDeleteView extends BaseMVPView {
    /**
     * 删除音频成功
     */
    void deleteAudioSuccess();

    /**
     * 删除音频失败
     * @param msg
     */
    void deleteAudioFail(String msg);
}
