package com.huanmedia.videochat.mvp.presenter.appointment;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.PageRequest;
import com.huanmedia.videochat.mvp.entity.results.AppointmentListResults;
import com.huanmedia.videochat.mvp.model.appointment.AppointmentListModelImpl;
import com.huanmedia.videochat.mvp.view.appointment.IAppointmentListView;


public class AppointmentListPresenterImpl extends BaseMVPPresenter<IAppointmentListView, AppointmentListModelImpl> implements IAppointmentListPresenter {

    public AppointmentListPresenterImpl(IAppointmentListView mMvpView) {
        super(mMvpView, AppointmentListModelImpl.class);
    }

    @Override
    public void getAppointmentListForReadMan() {
        if (mMvpView.getContext() == null)
            return;
        if (mMvpView.getPageForReadMan() == 0) {
            mMvpView.showToast("页数为0");
            return;
        }
        if (mMvpView.getPageSizeForReadMan() == 0) {
            mMvpView.showToast("每页数量为0");
            return;
        }
        PageRequest params = new PageRequest();
        params.setPage(mMvpView.getPageForReadMan());
        params.setPagesize(mMvpView.getPageForReadMan());
        if (mMvpView.getFiltrateYear() != 0) {
            params.setYear(mMvpView.getFiltrateYear());
        }
        if (mMvpView.getFiltrateMonth() != 0) {
            params.setMonth(mMvpView.getFiltrateMonth());
        }
        mMvpModel.getAppointmentListForReadMan(mMvpView.getContext(), params, new DataCallBack<AppointmentListResults>() {

            @Override
            public void getDataSuccess(AppointmentListResults data) {
                mMvpView.getPtrLayoutForReadMan().setListDataResults(data, data.getItems());
                mMvpView.getAppointmentListForReadManSuccess(data.getItems());
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getPtrLayoutForReadMan().setRefreshComplete();
                mMvpView.getAppointmentListForReadManFail(msg);
            }
        });
    }

    @Override
    public void getAppointmentListForNormal() {
        if (mMvpView.getContext() == null)
            return;
        if (mMvpView.getPageForNormal() == 0) {
            mMvpView.showToast("页数为0");
            return;
        }
        if (mMvpView.getPageSizeForNormal() == 0) {
            mMvpView.showToast("每页数量为0");
            return;
        }
        PageRequest params = new PageRequest();
        params.setPage(mMvpView.getPageForNormal());
        params.setPagesize(mMvpView.getPageSizeForNormal());
        if (mMvpView.getFiltrateYear() != 0) {
            params.setYear(mMvpView.getFiltrateYear());
        }
        if (mMvpView.getFiltrateMonth() != 0) {
            params.setMonth(mMvpView.getFiltrateMonth());
        }
        mMvpModel.getAppointmentListForNormal(mMvpView.getContext(), params, new DataCallBack<AppointmentListResults>() {

            @Override
            public void getDataSuccess(AppointmentListResults data) {
                mMvpView.getPtrLayoutForNormal().setListDataResults(data, data.getItems());
                mMvpView.getAppointmentListForNormalSuccess(data.getItems());
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getPtrLayoutForNormal().setRefreshComplete();
                mMvpView.getAppointmentListForNormalFail(msg);
            }
        });
    }
}
