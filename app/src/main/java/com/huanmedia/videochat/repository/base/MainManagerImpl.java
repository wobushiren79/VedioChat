package com.huanmedia.videochat.repository.base;

import android.content.Context;

import com.huanmedia.videochat.common.manager.ResourceManager;
import com.huanmedia.videochat.mvp.entity.request.BusinessCardInfoRequest;
import com.huanmedia.videochat.mvp.entity.request.FileUpLoadRequest;
import com.huanmedia.videochat.mvp.entity.request.ShufflingAdsRequest;
import com.huanmedia.videochat.mvp.entity.request.TalkRoomListRequest;
import com.huanmedia.videochat.mvp.entity.request.UploadUserDataRequest;
import com.huanmedia.videochat.mvp.entity.request.UserVideoDataRequest;
import com.huanmedia.videochat.mvp.entity.results.BusinessCardInfoResults;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;
import com.huanmedia.videochat.mvp.entity.results.ShufflingAdsResults;
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
    public void getShufflingAds(Context context, ShufflingAdsRequest params, HttpResponseHandler<List<ShufflingAdsResults>> handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        requestPost(context, mApiService.ads(paramsMap), handler);
    }

    @Override
    public void getBusinessCardInfo(Context context, BusinessCardInfoRequest params, HttpResponseHandler<BusinessCardInfoResults> handler) {
        requestPost(context, mApiService.userinfoall(params.getUid()), handler, true);
    }


}
