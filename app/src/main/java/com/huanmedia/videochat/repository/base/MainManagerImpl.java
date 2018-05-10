package com.huanmedia.videochat.repository.base;

import android.content.Context;

import com.huanmedia.videochat.common.manager.ResourceManager;
import com.huanmedia.videochat.mvp.entity.request.FileUpLoadRequest;
import com.huanmedia.videochat.mvp.entity.request.TalkRoomListRequest;
import com.huanmedia.videochat.mvp.entity.request.UploadUserDataRequest;
import com.huanmedia.videochat.mvp.entity.request.UserVideoUpLoadRequest;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;
import com.huanmedia.videochat.mvp.entity.results.TalkRoomListResults;
import com.huanmedia.videochat.mvp.entity.results.UserVideoUpLoadResults;
import com.huanmedia.videochat.repository.net.RemoteApiService;

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


}
