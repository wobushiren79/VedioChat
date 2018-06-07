package com.huanmedia.videochat.mvp.model.appointment;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.PageRequest;
import com.huanmedia.videochat.mvp.entity.results.AppointmentListResults;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

import java.util.List;

public class AppointmentListModelImpl extends BaseMVPModel implements IAppointmentListModel {

    @Override
    public void getAppointmentListForReadMan(Context context, PageRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().getAppointmentListForReadMan(context, params, new HttpResponseHandler<AppointmentListResults>() {
            @Override
            public void onSuccess(AppointmentListResults result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }

    @Override
    public void getAppointmentListForNormal(Context context, PageRequest params, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().getAppointmentListForNormal(context, params, new HttpResponseHandler<AppointmentListResults>() {
            @Override
            public void onSuccess(AppointmentListResults result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }

}
