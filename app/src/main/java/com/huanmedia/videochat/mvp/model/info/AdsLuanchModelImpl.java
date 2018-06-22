package com.huanmedia.videochat.mvp.model.info;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AdsLuanchRequest;
import com.huanmedia.videochat.mvp.entity.results.AdsLuanchResults;
import com.huanmedia.videochat.mvp.entity.results.AdsShufflingResults;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

import java.util.List;

public class AdsLuanchModelImpl extends BaseMVPModel implements IAdsLuanchModel {
    @Override
    public void getAdsInfo(Context context, AdsLuanchRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().getLuanchAds(context, params, new HttpResponseHandler<AdsLuanchResults>() {
            @Override
            public void onSuccess(AdsLuanchResults result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }
}
