package com.huanmedia.videochat.mvp.model.appointment;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AppointmentRequest;
import com.huanmedia.videochat.mvp.entity.results.AppointmentDetailResults;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

public class AppointmentDetailModeImpl extends BaseMVPModel implements IAppointmentDetailModel {

    @Override
    public void getDetails(Context context, AppointmentRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().getAppointmentDetail(context, params, new HttpResponseHandler<AppointmentDetailResults>() {
            @Override
            public void onSuccess(AppointmentDetailResults result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }
}
