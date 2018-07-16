package com.huanmedia.videochat.mvp.model.info;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ArtistsGroupShowRequest;
import com.huanmedia.videochat.mvp.entity.results.ArtistsGroupResults;
import com.huanmedia.videochat.mvp.entity.results.ArtistsGroupShowResults;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

import java.util.List;

public class ArtistsGroupModelImpl extends BaseMVPModel implements IArtistsGroupModel {
    @Override
    public void getArtistsGroupList(Context context, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().artistsGroupList(context, new HttpResponseHandler<List<ArtistsGroupResults>>() {
            @Override
            public void onSuccess(List<ArtistsGroupResults> result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }

    @Override
    public void getArtistsGroupShow(Context context, ArtistsGroupShowRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().artistsGroupShow(context, params, new HttpResponseHandler<ArtistsGroupShowResults>() {
            @Override
            public void onSuccess(ArtistsGroupShowResults result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }
}
