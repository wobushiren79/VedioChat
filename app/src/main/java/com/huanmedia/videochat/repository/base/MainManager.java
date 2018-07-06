package com.huanmedia.videochat.repository.base;


import android.content.Context;

import com.huanmedia.videochat.mvp.entity.request.AdsLuanchRequest;
import com.huanmedia.videochat.mvp.entity.request.AppointmentRequest;
import com.huanmedia.videochat.mvp.entity.request.AppointmentSettingRequest;
import com.huanmedia.videochat.mvp.entity.request.BusinessCardInfoRequest;
import com.huanmedia.videochat.mvp.entity.request.FileUpLoadRequest;
import com.huanmedia.videochat.mvp.entity.request.PageRequest;
import com.huanmedia.videochat.mvp.entity.request.AdsShufflingRequest;
import com.huanmedia.videochat.mvp.entity.request.ShortVideoListRequest;
import com.huanmedia.videochat.mvp.entity.request.ShortVideoPraiseRequest;
import com.huanmedia.videochat.mvp.entity.request.TalkRoomListRequest;
import com.huanmedia.videochat.mvp.entity.request.UploadUserDataRequest;
import com.huanmedia.videochat.mvp.entity.request.UserVideoDataRequest;
import com.huanmedia.videochat.mvp.entity.results.AdsLuanchResults;
import com.huanmedia.videochat.mvp.entity.results.AppointmentListResults;
import com.huanmedia.videochat.mvp.entity.results.AppointmentSettingResults;
import com.huanmedia.videochat.mvp.entity.results.AppointmentUserInfoResults;
import com.huanmedia.videochat.mvp.entity.results.BusinessCardInfoResults;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;
import com.huanmedia.videochat.mvp.entity.results.AdsShufflingResults;
import com.huanmedia.videochat.mvp.entity.results.ShortVideoListResults;
import com.huanmedia.videochat.mvp.entity.results.TalkRoomListResults;

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
     *
     * @param context
     * @param params
     * @param handler
     */
    void userVideoDelete(Context context, UserVideoDataRequest params, HttpResponseHandler<Object> handler);


    /**
     * 获取轮播广告信息
     *
     * @param context
     * @param params
     * @param handler
     */
    void getShufflingAds(Context context, AdsShufflingRequest params, HttpResponseHandler<List<AdsShufflingResults>> handler);

    /**
     * 获取首页广告信息
     *
     * @param context
     * @param params
     * @param handler
     */
    void getLuanchAds(Context context, AdsLuanchRequest params, HttpResponseHandler<AdsLuanchResults> handler);


    /**
     * 获取个人名片信息
     *
     * @param context
     * @param params
     * @param handler
     */
    void getBusinessCardInfo(Context context, BusinessCardInfoRequest params, HttpResponseHandler<BusinessCardInfoResults> handler);

    /**
     * 获取预约红人数据
     *
     * @param context
     * @param params
     * @param handler
     */
    void getAppointmentUserInfo(Context context, AppointmentRequest params, HttpResponseHandler<AppointmentUserInfoResults> handler);

    /**
     * 提交预约红人数据
     *
     * @param context
     * @param params
     * @param handler
     */
    void submitAppointment(Context context, AppointmentRequest params, HttpResponseHandler handler);

    /**
     * 获取预约设置
     *
     * @param context
     * @param params
     * @param handler
     */
    void getAppointmentSettingInfo(Context context, AppointmentRequest params, HttpResponseHandler<AppointmentSettingResults> handler);

    /**
     * 设置预约设置
     *
     * @param context
     * @param params
     * @param handler
     */
    void setAppointmentSetting(Context context, AppointmentSettingRequest params, HttpResponseHandler handler);


    /**
     * 获取预约列表数据(for红人)
     *
     * @param context
     * @param params
     * @param handler
     */
    void getAppointmentListForReadMan(Context context, PageRequest params, HttpResponseHandler<AppointmentListResults> handler);

    /**
     * 获取预约列表数据(for红人)
     *
     * @param context
     * @param params
     * @param handler
     */
    void getAppointmentListForNormal(Context context, PageRequest params, HttpResponseHandler<AppointmentListResults> handler);

    /**
     * 确认或取消预约单
     *
     * @param context
     * @param params
     * @param handler
     */
    void confirmAppointment(Context context, AppointmentRequest params, HttpResponseHandler handler);


    /**
     * 短视频列表
     *
     * @param context
     * @param params
     * @param handler
     */
    void shortVideoList(Context context, ShortVideoListRequest params, HttpResponseHandler<ShortVideoListResults> handler);

    /**
     * 短视频列表
     *
     * @param context
     * @param params
     * @param handler
     */
    void shortVideoPraise(Context context, ShortVideoPraiseRequest params, HttpResponseHandler handler);

}
