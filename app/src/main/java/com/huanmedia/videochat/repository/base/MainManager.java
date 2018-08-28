package com.huanmedia.videochat.repository.base;


import android.content.Context;

import com.huanmedia.videochat.mvp.entity.request.AdsLuanchRequest;
import com.huanmedia.videochat.mvp.entity.request.AppointmentListOpRequest;
import com.huanmedia.videochat.mvp.entity.request.AppointmentRequest;
import com.huanmedia.videochat.mvp.entity.request.AppointmentSettingRequest;
import com.huanmedia.videochat.mvp.entity.request.ArtistsGroupShowRequest;
import com.huanmedia.videochat.mvp.entity.request.AudioFileRequest;
import com.huanmedia.videochat.mvp.entity.request.BusinessCardInfoRequest;
import com.huanmedia.videochat.mvp.entity.request.ChatListRequest;
import com.huanmedia.videochat.mvp.entity.request.ChatReadRequest;
import com.huanmedia.videochat.mvp.entity.request.ChatSendRequest;
import com.huanmedia.videochat.mvp.entity.request.FileManageRequest;
import com.huanmedia.videochat.mvp.entity.request.FileUpLoadRequest;
import com.huanmedia.videochat.mvp.entity.request.PageRequest;
import com.huanmedia.videochat.mvp.entity.request.AdsShufflingRequest;
import com.huanmedia.videochat.mvp.entity.request.PhotoListRequest;
import com.huanmedia.videochat.mvp.entity.request.RewardRequest;
import com.huanmedia.videochat.mvp.entity.request.ShortVideoListRequest;
import com.huanmedia.videochat.mvp.entity.request.ShortVideoPraiseRequest;
import com.huanmedia.videochat.mvp.entity.request.TalkRoomListRequest;
import com.huanmedia.videochat.mvp.entity.request.UploadUserDataRequest;
import com.huanmedia.videochat.mvp.entity.request.UserVideoDataRequest;
import com.huanmedia.videochat.mvp.entity.request.UserVideoListRequest;
import com.huanmedia.videochat.mvp.entity.results.AdsLuanchResults;
import com.huanmedia.videochat.mvp.entity.results.AppointmentDetailResults;
import com.huanmedia.videochat.mvp.entity.results.AppointmentListOpResults;
import com.huanmedia.videochat.mvp.entity.results.AppointmentListResults;
import com.huanmedia.videochat.mvp.entity.results.AppointmentSettingResults;
import com.huanmedia.videochat.mvp.entity.results.AppointmentUserInfoResults;
import com.huanmedia.videochat.mvp.entity.results.ArtistsGroupResults;
import com.huanmedia.videochat.mvp.entity.results.ArtistsGroupShowResults;
import com.huanmedia.videochat.mvp.entity.results.AudioFileResults;
import com.huanmedia.videochat.mvp.entity.results.BusinessCardInfoResults;
import com.huanmedia.videochat.mvp.entity.results.ChatListResults;
import com.huanmedia.videochat.mvp.entity.results.FileHotTagResults;
import com.huanmedia.videochat.mvp.entity.results.FileManageResults;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;
import com.huanmedia.videochat.mvp.entity.results.AdsShufflingResults;
import com.huanmedia.videochat.mvp.entity.results.RewardResults;
import com.huanmedia.videochat.mvp.entity.results.ShortVideoListResults;
import com.huanmedia.videochat.mvp.entity.results.TalkRoomListResults;
import com.huanmedia.videochat.repository.entity.BusinessCardUserTags;
import com.huanmedia.videochat.repository.entity.GiftEntity;
import com.huanmedia.videochat.repository.entity.PhotosEntity;
import com.huanmedia.videochat.repository.entity.VideoEntity;

import java.util.ArrayList;
import java.util.List;

public interface MainManager {

    /**
     * 通用地址接口
     *
     * @param context
     * @param params
     * @param handler
     */
    void commonUrl(Context context, String url, Object params, HttpResponseHandler handler);

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
    void ossInfo(Context context, FileUpLoadRequest params, HttpResponseHandler<FileUpLoadResults> handler, boolean isShowDialog);


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
     * 获取预约红人数据 新版
     *
     * @param context
     * @param params
     * @param handler
     */
    void getAppointmentUserInfoOp(Context context, AppointmentRequest params, HttpResponseHandler<AppointmentUserInfoResults> handler);

    /**
     * 提交预约红人数据
     *
     * @param context
     * @param params
     * @param handler
     */
    void submitAppointment(Context context, AppointmentRequest params, HttpResponseHandler handler);

    /**
     * 提交预约红人数据 新版
     *
     * @param context
     * @param params
     * @param handler
     */
    void submitAppointmentOp(Context context, AppointmentRequest params, HttpResponseHandler handler);

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
     * 获取预约列表数据  新版
     *
     * @param context
     * @param params
     * @param handler
     */
    void getAppointmentListOp(Context context, AppointmentListOpRequest params, HttpResponseHandler<AppointmentListOpResults> handler);

    /**
     * 确认或取消预约单
     *
     * @param context
     * @param params
     * @param handler
     */
    void confirmAppointment(Context context, AppointmentRequest params, HttpResponseHandler handler);

    /**
     * 确认或取消预约单
     *
     * @param context
     * @param params
     * @param handler
     */
    void confirmAppointmentOp(Context context, AppointmentRequest params, HttpResponseHandler handler);

    /**
     * 预约单详情
     *
     * @param context
     * @param params
     * @param handler
     */
    void getAppointmentDetail(Context context, AppointmentRequest params, HttpResponseHandler<AppointmentDetailResults> handler);

    /**
     * 完成预约单
     *
     * @param context
     * @param params
     * @param handler
     */
    void completeAppointment(Context context, AppointmentRequest params, HttpResponseHandler handler);

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

    /**
     * 礼物列表
     *
     * @param context
     * @param handler
     */
    void getGiftList(Context context, HttpResponseHandler<List<ArrayList<GiftEntity>>> handler);

    /**
     * 奖励
     *
     * @param context
     * @param params
     * @param handler
     */
    void reward(Context context, RewardRequest params, HttpResponseHandler<RewardResults> handler);


    /**
     * 艺人组合列表
     *
     * @param context
     * @param handler
     */
    void artistsGroupList(Context context, HttpResponseHandler<List<ArtistsGroupResults>> handler);

    /**
     * 艺人展示
     *
     * @param context
     * @param params
     * @param handler
     */
    void artistsGroupShow(Context context, ArtistsGroupShowRequest params, HttpResponseHandler<ArtistsGroupShowResults> handler);

    /**
     * 聊天列表
     *
     * @param context
     * @param params
     * @param handler
     */
    void getChatList(Context context, ChatListRequest params, HttpResponseHandler<ChatListResults> handler, boolean hasDialog);

    /**
     * 阅读消息
     *
     * @param context
     * @param params
     * @param handler
     */
    void readChat(Context context, ChatReadRequest params, HttpResponseHandler handler);

    /**
     * 预约投诉
     *
     * @param context
     * @param aid
     * @param handler
     */
    void appointmentComplain(Context context, int aid, HttpResponseHandler handler);


    /**
     * 获取照片列表
     *
     * @param context
     * @param params
     * @param handler
     */
    void getPhotoList(Context context, PhotoListRequest params, HttpResponseHandler<List<PhotosEntity>> handler);


    /**
     * 获取文件热门标签
     *
     * @param context
     * @param handler
     */
    void getFileHotTagList(Context context, HttpResponseHandler<List<FileHotTagResults>> handler);

    /**
     * 获取用户短视频列表
     *
     * @param context
     * @param params
     * @param handler
     */
    void getUserVideoList(Context context, UserVideoListRequest params, HttpResponseHandler<List<VideoEntity>> handler);


    /**
     * 检测是否拥有此文件
     *
     * @param context
     * @param params
     * @param handler
     */
    void checkHasFile(Context context, FileManageRequest params, HttpResponseHandler<FileManageResults> handler);


    /**
     * 支付此文件
     *
     * @param context
     * @param params
     * @param handler
     */
    void payFile(Context context, FileManageRequest params, HttpResponseHandler<FileManageResults> handler);

    /**
     * 增加用户音频
     *
     * @param context
     * @param params
     * @param handler
     */
    void addUserAudio(Context context, AudioFileRequest params, HttpResponseHandler handler);

    /**
     * 删除用户音频
     *
     * @param context
     * @param params
     * @param handler
     */
    void deleteUserAudio(Context context, AudioFileRequest params, HttpResponseHandler handler);

    /**
     * 获取用户音频
     *
     * @param context
     * @param params
     * @param handler
     */
    void getUserAudio(Context context, AudioFileRequest params, HttpResponseHandler<AudioFileResults> handler);
}
