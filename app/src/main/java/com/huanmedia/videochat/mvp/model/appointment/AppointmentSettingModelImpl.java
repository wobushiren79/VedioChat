package com.huanmedia.videochat.mvp.model.appointment;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AppointmentRequest;
import com.huanmedia.videochat.mvp.entity.request.AppointmentSettingRequest;
import com.huanmedia.videochat.mvp.entity.results.AppointmentSettingResults;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

public class AppointmentSettingModelImpl extends BaseMVPModel implements IAppoinmentSettingModel {
    @Override
    public void getSettingInfo(Context context, AppointmentRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().getAppointmentSettingInfo(context, params, new HttpResponseHandler<AppointmentSettingResults>() {
            @Override
            public void onSuccess(AppointmentSettingResults result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }

    @Override
    public void submitSettingInfo(Context context, AppointmentSettingRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().setAppointmentSetting(context, params, new HttpResponseHandler() {
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
