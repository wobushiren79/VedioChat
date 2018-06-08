package com.huanmedia.videochat.mvp.model.user;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.BusinessCardInfoRequest;
import com.huanmedia.videochat.mvp.entity.results.BusinessCardInfoResults;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

public class BusinessCardInfoModelImpl extends BaseMVPModel implements IBusinessCardInfoModel {
    @Override
    public void getContactUnLockInfo(Context context, BusinessCardInfoRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().getBusinessCardInfo(context, params, new HttpResponseHandler<BusinessCardInfoResults>() {
            @Override
            public void onSuccess(BusinessCardInfoResults result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }
}
