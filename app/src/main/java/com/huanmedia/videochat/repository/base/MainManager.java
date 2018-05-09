package com.huanmedia.videochat.repository.base;


import android.content.Context;

import com.huanmedia.videochat.mvp.entity.request.FileUpLoadRequest;
import com.huanmedia.videochat.mvp.entity.request.TalkRoomListRequest;
import com.huanmedia.videochat.mvp.entity.request.UploadUserDataRequest;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;
import com.huanmedia.videochat.mvp.entity.results.TalkRoomListResults;

public interface MainManager {

    /**
     * 上传更新用户信息
     *
     * @param context
     * @param params
     * @param handler
     */
    void uploadUserData(Context context, UploadUserDataRequest params, HttpResponseHandler handler);


    /**
     * 获取洽谈房间列表
     *
     * @param context
     * @param params
     * @param handler
     */
    void talkRoomList(Context context, TalkRoomListRequest params, HttpResponseHandler<TalkRoomListResults> handler);


    /**
     * 阿里云上传信息
     *
     * @param context
     * @param params
     * @param handler
     */
    void ossInfo(Context context, FileUpLoadRequest params, HttpResponseHandler<FileUpLoadResults> handler);
}
