package com.huanmedia.videochat.mvp.model.user;


import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.SubmitEvaluationRequest;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;


public class EvaluationModelImpl extends BaseMVPModel implements IEvaluationModel {
    private MainRepostiory mainRepostiory;

    public EvaluationModelImpl() {
        this.mainRepostiory = new MainRepostiory();
    }

    @Override
    public void getSystemTag(DataCallBack callBack) {
        mainRepostiory.systag().subscribe(
                userinfoEntity -> {
                    callBack.getDataSuccess(userinfoEntity);
                },
                throwable -> {
                    callBack.getDataFail(throwable.getMessage());
                }
        );
    }

    @Override
    public void submitEvaluation(SubmitEvaluationRequest params, DataCallBack callBack) {
        mainRepostiory.submittag(params.getUid(), params.getTxt(), params.getTagids()).subscribe(
                userinfoEntity -> {
                    callBack.getDataSuccess(userinfoEntity);
                },
                throwable -> {
                    callBack.getDataFail(throwable.getMessage());
                });
    }
}
