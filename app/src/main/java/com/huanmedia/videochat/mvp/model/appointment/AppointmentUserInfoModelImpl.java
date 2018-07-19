package com.huanmedia.videochat.mvp.model.appointment;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AppointmentRequest;
import com.huanmedia.videochat.mvp.entity.results.AppointmentUserInfoResults;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

public class AppointmentUserInfoModelImpl extends BaseMVPModel implements IAppointmentUserInfoModel {

    @Override
    public void getAppointmentUserInfo(Context context, AppointmentRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().getAppointmentUserInfo(context, params, new HttpResponseHandler<AppointmentUserInfoResults>() {
            @Override
            public void onSuccess(AppointmentUserInfoResults result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }

    @Override
    public void getAppointmentUserInfoOp(Context context, AppointmentRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().getAppointmentUserInfoOp(context, params, new HttpResponseHandler<AppointmentUserInfoResults>() {
            @Override
            public void onSuccess(AppointmentUserInfoResults result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }
}
