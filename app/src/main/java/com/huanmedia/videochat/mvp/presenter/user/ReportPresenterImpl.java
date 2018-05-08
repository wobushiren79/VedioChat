package com.huanmedia.videochat.mvp.presenter.user;

import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ReportRequest;
import com.huanmedia.videochat.mvp.model.user.ReportModelImpl;
import com.huanmedia.videochat.mvp.view.user.IReportView;

public class ReportPresenterImpl extends BaseMVPPresenter<IReportView, ReportModelImpl> implements IReportPresenter {

    public ReportPresenterImpl(IReportView mMvpView) {
        super(mMvpView,ReportModelImpl.class);
    }

    @Override
    public void report() {
        long reportUserId = mMvpView.getReportUserId();
        int reportType = mMvpView.getReportType();
        if (reportUserId == 0) {
            mMvpView.showToast("没有被举报人ID");
            return;
        }
        if (reportType == 0) {
            mMvpView.showToast("没有举报类型");
            return;
        }

        ReportRequest params = new ReportRequest();
        params.setOuid(reportUserId);
        params.setType(reportType);
        mMvpModel.submitEvaluation(params, new DataCallBack() {
            @Override
            public void getDataSuccess(Object data) {
                mMvpView.reportSuccess();
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.reportFail(msg);
            }
        });
    }
}
