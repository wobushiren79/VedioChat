package com.huanmedia.videochat.repository.base;

import android.content.Context;

import com.huanmedia.videochat.common.manager.ResourceManager;
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
import com.huanmedia.videochat.repository.net.RemoteApiService;

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
    public void ossInfo(Context context, FileUpLoadRequest params, HttpResponseHandler<FileUpLoadResults> handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.ossinfo(paramsMap), handler);
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
        requestPost(context, mApiService.userinfoall(params.getUid()), handler, true);
    }

    @Override
    public void getAppointmentUserInfo(Context context, AppointmentRequest params, HttpResponseHandler<AppointmentUserInfoResults> handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.appointmentPageData(paramsMap), handler, true);
    }

    @Override
    public void submitAppointment(Context context, AppointmentRequest params, HttpResponseHandler handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.appointmentSubmit(paramsMap), handler, true);
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
    public void confirmAppointment(Context context, AppointmentRequest params, HttpResponseHandler handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.appointmentConfirm(paramsMap), handler, true);
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
}
