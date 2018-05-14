package com.huanmedia.videochat.repository.base;


import android.content.Context;

import com.huanmedia.videochat.mvp.entity.request.FileUpLoadRequest;
import com.huanmedia.videochat.mvp.entity.request.ShufflingAdsRequest;
import com.huanmedia.videochat.mvp.entity.request.TalkRoomListRequest;
import com.huanmedia.videochat.mvp.entity.request.UploadUserDataRequest;
import com.huanmedia.videochat.mvp.entity.request.UserVideoDataRequest;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;
import com.huanmedia.videochat.mvp.entity.results.ShufflingAdsResults;
import com.huanmedia.videochat.mvp.entity.results.TalkRoomListResults;
import com.huanmedia.videochat.mvp.entity.results.UserVideoDataResults;

import java.util.List;

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


    /**
     * 删除上传视频信息
     * @param context
     * @param params
     * @param handler
     */
    void userVideoDelete(Context context, UserVideoDataRequest params, HttpResponseHandler<Object> handler);


    /**
     * 获取轮播广告信息
     * @param context
     * @param params
     * @param listData
     */
    void getShufflingAds(Context context, ShufflingAdsRequest params,HttpResponseHandler<List<ShufflingAdsResults>> listData);
}
