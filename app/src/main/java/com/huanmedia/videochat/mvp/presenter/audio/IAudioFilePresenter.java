package com.huanmedia.videochat.mvp.presenter.audio;

public interface IAudioFilePresenter {
    /**
     * 增加音频文件
     * @param audioUrl
     */
    void addAudioFile(String audioUrl);

    /**
     * 删除音频文件
     */
    void deleteAudioFile();
}
