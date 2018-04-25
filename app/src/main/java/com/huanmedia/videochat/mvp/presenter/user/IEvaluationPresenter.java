package com.huanmedia.videochat.mvp.presenter.user;

public interface IEvaluationPresenter {
    void getSystemTag();

    void setSelectTag(int position,String text);

    void submitEvaluation(int submitStyle);
}
