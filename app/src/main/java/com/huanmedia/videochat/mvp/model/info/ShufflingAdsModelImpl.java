package com.huanmedia.videochat.mvp.model.info;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ShufflingAdsRequest;
import com.huanmedia.videochat.mvp.entity.results.ShufflingAdsResults;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

import java.util.List;

public class ShufflingAdsModelImpl extends BaseMVPModel implements IShufflingAdsModel {

    @Override
    public void getAdsInfo(Context context, ShufflingAdsRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().getShufflingAds(context, params, new HttpResponseHandler<List<ShufflingAdsResults>>() {
            @Override
            public void onSuccess(List<ShufflingAdsResults> result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }
}
