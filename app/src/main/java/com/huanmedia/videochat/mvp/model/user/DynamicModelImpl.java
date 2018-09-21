package com.huanmedia.videochat.mvp.model.user;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.DynamicListRequset;
import com.huanmedia.videochat.mvp.entity.results.DynamicListResults;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class DynamicModelImpl extends BaseMVPModel implements IDynamicModel {
    @Override
    public void getDynamicList(Context context, DynamicListRequset params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().getDynamicList(context, params, new HttpResponseHandler<DynamicListResults>() {
            @Override
            public void onSuccess(DynamicListResults result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }
}
