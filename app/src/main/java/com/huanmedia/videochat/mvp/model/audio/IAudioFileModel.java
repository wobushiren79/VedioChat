package com.huanmedia.videochat.mvp.model.audio;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AudioFileRequest;

public interface IAudioFileModel {

    /**
     * 增加音频
     * @param context
     * @param params
     * @param callBack
     */
    void addAudio(Context context, AudioFileRequest params, DataCallBack callBack);

    /**
     * 删除音频
     * @param context
     * @param params
     * @param callBack
     */
    void deleteAudio(Context context, AudioFileRequest params, DataCallBack callBack);

    /**
     * 获取
     * @param context
     * @param params
     * @param callBack
     */
    void getAudio(Context context, AudioFileRequest params, DataCallBack callBack);
}
