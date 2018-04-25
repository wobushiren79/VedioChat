package com.huanmedia.videochat.mvp.model.user;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.SubmitEvaluationRequest;

public interface IEvaluationModel {
    void getSystemTag(DataCallBack callBack);

    void submitEvaluation(SubmitEvaluationRequest params, DataCallBack callBack);
}
