package com.huanmedia.videochat.mvp.model.appointment;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AppointmentRequest;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

public class AppointmentSubmitModelImpl extends BaseMVPModel implements IAppointmentSubmitModel {
    @Override
    public void submit(Context context, AppointmentRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().submitAppointment(context, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(Object result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }
}
