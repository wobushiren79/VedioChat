package com.huanmedia.videochat.mvp.model.video;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ShortVideoListRequest;
import com.huanmedia.videochat.mvp.entity.results.ShortVideoListResults;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

public class ShortVideoListModelImpl extends BaseMVPModel implements IShortVideoListModel {

    @Override
    public void getShortVideoList(Context context, ShortVideoListRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().shortVideoList(context, params, new HttpResponseHandler<ShortVideoListResults>() {
            @Override
            public void onSuccess(ShortVideoListResults result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }

}
