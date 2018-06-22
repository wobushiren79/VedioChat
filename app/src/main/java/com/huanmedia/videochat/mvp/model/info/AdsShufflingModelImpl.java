package com.huanmedia.videochat.mvp.model.info;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AdsShufflingRequest;
import com.huanmedia.videochat.mvp.entity.results.AdsShufflingResults;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

import java.util.List;

public class AdsShufflingModelImpl extends BaseMVPModel implements IAdsShufflingModel {

    @Override
    public void getAdsInfo(Context context, AdsShufflingRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().getShufflingAds(context, params, new HttpResponseHandler<List<AdsShufflingResults>>() {
            @Override
            public void onSuccess(List<AdsShufflingResults> result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }
}
