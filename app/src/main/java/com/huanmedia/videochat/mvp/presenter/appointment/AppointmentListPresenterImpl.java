package com.huanmedia.videochat.mvp.presenter.appointment;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AppointmentListOpRequest;
import com.huanmedia.videochat.mvp.entity.request.PageRequest;
import com.huanmedia.videochat.mvp.entity.results.AppointmentDataOpResults;
import com.huanmedia.videochat.mvp.entity.results.AppointmentListOpResults;
import com.huanmedia.videochat.mvp.entity.results.AppointmentListResults;
import com.huanmedia.videochat.mvp.model.appointment.AppointmentListModelImpl;
import com.huanmedia.videochat.mvp.view.appointment.IAppointmentListOpView;
import com.huanmedia.videochat.mvp.view.appointment.IAppointmentListView;


public class AppointmentListPresenterImpl extends BaseMVPPresenter<BaseMVPView, AppointmentListModelImpl> implements IAppointmentListPresenter {

    public AppointmentListPresenterImpl(IAppointmentListView mMvpView) {
        super(mMvpView, AppointmentListModelImpl.class);
    }

    public AppointmentListPresenterImpl(IAppointmentListOpView mMvpView) {
        super(mMvpView, AppointmentListModelImpl.class);
    }

    @Override
    public void getAppointmentListForReadMan() {
        if (mMvpView.getContext() == null)
            return;
        if (mMvpView instanceof IAppointmentListView) {
            IAppointmentListView mMvpViewTemp = (IAppointmentListView) mMvpView;
            if (mMvpViewTemp.getPageForReadMan() == 0) {
                mMvpViewTemp.showToast("页数为0");
                return;
            }
            if (mMvpViewTemp.getPageSizeForReadMan() == 0) {
                mMvpViewTemp.showToast("每页数量为0");
                return;
            }
            PageRequest params = new PageRequest();
            params.setPage(mMvpViewTemp.getPageForReadMan());
            params.setPagesize(mMvpViewTemp.getPageForReadMan());
            if (mMvpViewTemp.getFiltrateYear() != 0) {
                params.setYear(mMvpViewTemp.getFiltrateYear());
            }
            if (mMvpViewTemp.getFiltrateMonth() != 0) {
                params.setMonth(mMvpViewTemp.getFiltrateMonth());
            }
            mMvpModel.getAppointmentListForReadMan(mMvpViewTemp.getContext(), params, new DataCallBack<AppointmentListResults>() {

                @Override
                public void getDataSuccess(AppointmentListResults data) {
                    mMvpViewTemp.getPtrLayoutForReadMan().setListDataResults(data, data.getItems());
                    mMvpViewTemp.getAppointmentListForReadManSuccess(data.getItems());
                }

                @Override
                public void getDataFail(String msg) {
                    mMvpViewTemp.getPtrLayoutForReadMan().setRefreshComplete();
                    mMvpViewTemp.getAppointmentListForReadManFail(msg);
                }
            });
        }

    }

    @Override
    public void getAppointmentListForNormal() {
        if (mMvpView.getContext() == null)
            return;
        if (mMvpView instanceof IAppointmentListView) {
            IAppointmentListView mMvpViewTemp = (IAppointmentListView) mMvpView;
            if (mMvpViewTemp.getPageForNormal() == 0) {
                mMvpViewTemp.showToast("页数为0");
                return;
            }
            if (mMvpViewTemp.getPageSizeForNormal() == 0) {
                mMvpViewTemp.showToast("每页数量为0");
                return;
            }
            PageRequest params = new PageRequest();
            params.setPage(mMvpViewTemp.getPageForNormal());
            params.setPagesize(mMvpViewTemp.getPageSizeForNormal());
            if (mMvpViewTemp.getFiltrateYear() != 0) {
                params.setYear(mMvpViewTemp.getFiltrateYear());
            }
            if (mMvpViewTemp.getFiltrateMonth() != 0) {
                params.setMonth(mMvpViewTemp.getFiltrateMonth());
            }
            mMvpModel.getAppointmentListForNormal(mMvpViewTemp.getContext(), params, new DataCallBack<AppointmentListResults>() {

                @Override
                public void getDataSuccess(AppointmentListResults data) {
                    mMvpViewTemp.getPtrLayoutForNormal().setListDataResults(data, data.getItems());
                    mMvpViewTemp.getAppointmentListForNormalSuccess(data.getItems());
                }

                @Override
                public void getDataFail(String msg) {
                    if (mMvpViewTemp.getPtrLayoutForNormal() == null)
                        return;
                    mMvpViewTemp.getPtrLayoutForNormal().setRefreshComplete();
                    mMvpViewTemp.getAppointmentListForNormalFail(msg);
                }
            });
        }

    }

    @Override
    public void getAppointmentListOp() {
        if (mMvpView.getContext() == null)
            return;
        if (mMvpView instanceof IAppointmentListOpView) {
            IAppointmentListOpView mMvpViewTemp = (IAppointmentListOpView) mMvpView;
            if (mMvpViewTemp.getAppointmentListStatus() == null || mMvpViewTemp.getAppointmentListStatus().length() == 0) {
                mMvpView.showToast("没有查询状态");
                return;
            }

            AppointmentListOpRequest params = new AppointmentListOpRequest();
            params.setPagesize(mMvpViewTemp.getAppointmentListPageSize());
            params.setPage(mMvpViewTemp.getAppointmentListPage());
            params.setAstatus(mMvpViewTemp.getAppointmentListStatus());

            if (mMvpViewTemp.getAppointmentListMonth() != 0)
                params.setMonth(mMvpViewTemp.getAppointmentListMonth());
            if (mMvpViewTemp.getAppointmentListMonth() != 0)
                params.setYear(mMvpViewTemp.getAppointmentListYear());

            mMvpModel.getAppointmentListOp(mMvpView.getContext(), params, new DataCallBack<AppointmentListOpResults>() {

                @Override
                public void getDataSuccess(AppointmentListOpResults data) {
                    mMvpViewTemp.getAppointmentListSuccess(data);
                    mMvpViewTemp.setAppointmentListData(data, data.getItems());
                }

                @Override
                public void getDataFail(String msg) {
                    mMvpViewTemp.getAppointmentListFail(msg);
                }
            });
        }
    }
}
