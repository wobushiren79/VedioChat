package com.huanmedia.videochat.mvp.model.user;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ReportRequest;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;

public class ReportModelImpl extends BaseMVPModel implements IReportModel {
    private MainRepostiory mainRepostiory;

    public ReportModelImpl() {
        this.mainRepostiory = new MainRepostiory();
    }

    @Override
    public void submitEvaluation(ReportRequest params, DataCallBack callBack) {
        mainRepostiory.reportother(params.getOuid() + "", params.getType())
                .subscribe(response -> {
                            callBack.getDataSuccess(null);
                        },
                        throwable -> {
                            callBack.getDataFail(throwable.getMessage());
                        }
                );
    }
}
