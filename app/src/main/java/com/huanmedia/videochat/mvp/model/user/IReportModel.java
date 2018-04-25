package com.huanmedia.videochat.mvp.model.user;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ReportRequest;

public interface IReportModel {
    void submitEvaluation(ReportRequest params, DataCallBack callBack);
}
