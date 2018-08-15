package com.huanmedia.videochat.mvp.model.photo;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.PhotoListRequest;
import com.huanmedia.videochat.mvp.entity.request.ShortVideoListRequest;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.entity.PhotosEntity;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

import java.util.List;

public class PhotoListModelImpl extends BaseMVPModel implements IPhotoListModel {

    @Override
    public void getPhotoList(Context context, PhotoListRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().getPhotoList(context, params, new HttpResponseHandler<List<PhotosEntity>>() {
            @Override
            public void onSuccess(List<PhotosEntity> result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }

}
