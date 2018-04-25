package com.huanmedia.videochat.my;

import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;

import mvp.data.net.ApiException;
import mvp.presenter.Presenter;

/**
 * Create by Administrator
 * time: 2018/3/7.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class ReadMainCertificatePresenter extends Presenter<ReadMainCertificateView>{
    private final MainRepostiory mRepostiory;

    public ReadMainCertificatePresenter(){
        mRepostiory=new MainRepostiory();
    }
    public void checkCompleteness(){
        getView().showLoading(null);
        addDisposable(mRepostiory.starcintegrity().subscribe(
                response -> {
                    getView().hideLoading();
                    getView().showNext(null,true);
                },throwable -> {
                    if (!isNullView()) {
                        getView().hideLoading();
                        if (throwable instanceof ApiException){
                            if (((ApiException) throwable).getErrorCode()==-1006){
                                getView().showNext(throwable.getMessage(),false);
                                return;
                            }
                        }
                        getView().showError(0, getGeneralErrorStr(throwable));
                    }
                }
        ));
    }
}
