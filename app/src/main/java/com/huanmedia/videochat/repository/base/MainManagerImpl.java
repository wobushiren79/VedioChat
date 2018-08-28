package com.huanmedia.videochat.repository.base;

import android.content.Context;

import com.huanmedia.videochat.common.manager.ResourceManager;
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
import com.huanmedia.videochat.repository.entity.GiftEntity;
import com.huanmedia.videochat.repository.entity.PhotosEntity;
import com.huanmedia.videochat.repository.entity.VideoEntity;
import com.huanmedia.videochat.repository.net.RemoteApiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainManagerImpl extends BaseManagerImpl implements MainManager {
    private static MainManagerImpl manager;
    private RemoteApiService mApiService;

    public MainManagerImpl() {
        super();
        mApiService = ResourceManager.getInstance().getDefaultApiService();
    }

    public static MainManagerImpl getInstance() {
        if (manager == null) {
            synchronized (MainManagerImpl.class) {
                if (manager == null) {
                    manager = new MainManagerImpl();
                }
            }
        }
        return manager;
    }

    @Override
    public void commonUrl(Context context, String url, Object params, HttpResponseHandler handler) {
        Map<String, Object> paramsMap;
        if (params != null) {
            paramsMap = (Map<String, Object>) params;
        } else {
            paramsMap = new HashMap<>();
        }
        requestPost(context, mApiService.common(url, paramsMap), handler);
    }

    @Override
    public void uploadUserData(Context context, UploadUserDataRequest params, HttpResponseHandler handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.uploaduserdata(paramsMap), handler);
    }

    @Override
    public void talkRoomList(Context context, TalkRoomListRequest params, HttpResponseHandler<TalkRoomListResults> handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.talkroomlist(paramsMap), handler);
    }

    @Override
    public void ossInfo(Context context, FileUpLoadRequest params, HttpResponseHandler<FileUpLoadResults> handler, boolean isShowDialog) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.ossinfo(paramsMap), handler, isShowDialog);
    }

    @Override
    public void userVideoDelete(Context context, UserVideoDataRequest params, HttpResponseHandler<Object> handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.ossdel(paramsMap), handler, true);
    }

    @Override
    public void getShufflingAds(Context context, AdsShufflingRequest params, HttpResponseHandler<List<AdsShufflingResults>> handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.ads(paramsMap), handler);
    }

    @Override
    public void getLuanchAds(Context context, AdsLuanchRequest params, HttpResponseHandler<AdsLuanchResults> handler) {
        requestPost(context, mApiService.adsluanch(), handler);
    }

    @Override
    public void getBusinessCardInfo(Context context, BusinessCardInfoRequest params, HttpResponseHandler<BusinessCardInfoResults> handler) {
        requestPost(context, mApiService.userinfoall(params.getUid(), params.getPlevel()), handler, true);
    }

    @Override
    public void getAppointmentUserInfo(Context context, AppointmentRequest params, HttpResponseHandler<AppointmentUserInfoResults> handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.appointmentPageData(paramsMap), handler, true);
    }

    @Override
    public void getAppointmentUserInfoOp(Context context, AppointmentRequest params, HttpResponseHandler<AppointmentUserInfoResults> handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.appointmentPageDataV2(paramsMap), handler, true);
    }

    @Override
    public void submitAppointment(Context context, AppointmentRequest params, HttpResponseHandler handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.appointmentSubmit(paramsMap), handler, true);
    }

    @Override
    public void submitAppointmentOp(Context context, AppointmentRequest params, HttpResponseHandler handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.appointmentSubmitV2(paramsMap), handler, true);
    }

    @Override
    public void getAppointmentSettingInfo(Context context, AppointmentRequest params, HttpResponseHandler<AppointmentSettingResults> handler) {
        requestPost(context, mApiService.appointmentSettingInfo(), handler, true);
    }

    @Override
    public void setAppointmentSetting(Context context, AppointmentSettingRequest params, HttpResponseHandler handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.appointmentSettingSet(paramsMap), handler, true);
    }

    @Override
    public void getAppointmentListForReadMan(Context context, PageRequest params, HttpResponseHandler<AppointmentListResults> handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.appointmentListDataForReadMan(paramsMap), handler);
    }

    @Override
    public void getAppointmentListForNormal(Context context, PageRequest params, HttpResponseHandler<AppointmentListResults> handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.appointmentListDataForNormal(paramsMap), handler);
    }

    @Override
    public void getAppointmentListOp(Context context, AppointmentListOpRequest params, HttpResponseHandler<AppointmentListOpResults> handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.appointmentListDataOp(paramsMap), handler);
    }

    @Override
    public void confirmAppointment(Context context, AppointmentRequest params, HttpResponseHandler handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.appointmentConfirm(paramsMap), handler, true);
    }

    @Override
    public void confirmAppointmentOp(Context context, AppointmentRequest params, HttpResponseHandler handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.appointmentConfirmOp(paramsMap), handler, true);
    }

    @Override
    public void getAppointmentDetail(Context context, AppointmentRequest params, HttpResponseHandler<AppointmentDetailResults> handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.appointmentDetail(paramsMap), handler, true);
    }

    @Override
    public void completeAppointment(Context context, AppointmentRequest params, HttpResponseHandler handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.completeAppointment(paramsMap), handler, true);
    }

    @Override
    public void shortVideoList(Context context, ShortVideoListRequest params, HttpResponseHandler<ShortVideoListResults> handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.shortVideoList(paramsMap), handler);
    }

    @Override
    public void shortVideoPraise(Context context, ShortVideoPraiseRequest params, HttpResponseHandler handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.shortVideoPraise(paramsMap), handler);
    }

    @Override
    public void getGiftList(Context context, HttpResponseHandler<List<ArrayList<GiftEntity>>> handler) {
        requestPost(context, mApiService.giftlist(), handler);
    }

    @Override
    public void reward(Context context, RewardRequest params, HttpResponseHandler<RewardResults> handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.reward(paramsMap), handler);
    }

    @Override
    public void artistsGroupList(Context context, HttpResponseHandler<List<ArtistsGroupResults>> handler) {
        requestPost(context, mApiService.artistsGroupList(), handler);
    }

    @Override
    public void artistsGroupShow(Context context, ArtistsGroupShowRequest params, HttpResponseHandler<ArtistsGroupShowResults> handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.artistsGroupShow(paramsMap), handler);
    }

    @Override
    public void getChatList(Context context, ChatListRequest params, HttpResponseHandler<ChatListResults> handler, boolean hasDialog) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.chatList(paramsMap), handler, hasDialog);
    }

    @Override
    public void readChat(Context context, ChatReadRequest params, HttpResponseHandler handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.readChat(paramsMap), handler);
    }

    @Override
    public void appointmentComplain(Context context, int aid, HttpResponseHandler handler) {
        requestPost(context, mApiService.appointmentComplain(aid), handler);
    }

    @Override
    public void getPhotoList(Context context, PhotoListRequest params, HttpResponseHandler<List<PhotosEntity>> handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.userPhotosList(paramsMap), handler);
    }

    @Override
    public void getFileHotTagList(Context context, HttpResponseHandler<List<FileHotTagResults>> handler) {
        requestPost(context, mApiService.fileHotTagList(), handler);
    }

    @Override
    public void getUserVideoList(Context context, UserVideoListRequest params, HttpResponseHandler<List<VideoEntity>> handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.userVideoList(paramsMap), handler);
    }

    @Override
    public void checkHasFile(Context context, FileManageRequest params, HttpResponseHandler<FileManageResults> handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.checkHasFile(paramsMap), handler, true);
    }

    @Override
    public void payFile(Context context, FileManageRequest params, HttpResponseHandler<FileManageResults> handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.payFile(paramsMap), handler, true);
    }

    @Override
    public void addUserAudio(Context context, AudioFileRequest params, HttpResponseHandler handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.addUserAudio(paramsMap), handler, true);
    }

    @Override
    public void deleteUserAudio(Context context, AudioFileRequest params, HttpResponseHandler handler) {
        requestPost(context, mApiService.deleteUserAudio(), handler, true);
    }

    @Override
    public void getUserAudio(Context context, AudioFileRequest params, HttpResponseHandler<AudioFileResults> handler) {
        requestPost(context, mApiService.getUserAudio(), handler);
    }


}
