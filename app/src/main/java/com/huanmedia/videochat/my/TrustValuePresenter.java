package com.huanmedia.videochat.my;

import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;

import mvp.presenter.Presenter;

/**
 * Create by Administrator
 * time: 2018/3/5.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class TrustValuePresenter extends Presenter<TrustValueView>{
    private final MainRepostiory mRepostiory;

    public TrustValuePresenter(){
        mRepostiory=new MainRepostiory();
    }
    public void loadTrueData(){
        getView().showLoading(null);
        addDisposable(mRepostiory.userfulltags().subscribe(
                trustValueEntity -> {
                    getView().hideLoading();
                    getView().showTrueData(trustValueEntity);
                }
                ,
                throwable -> {
                    if (!isNullView()){
                        getView().hideLoading();
                        getView().showError(HintDialog.HintType.WARN,getGeneralErrorStr(throwable));
                    }
                }
        ));
    }
}
