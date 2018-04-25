package com.huanmedia.videochat.pay;

import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;

import mvp.presenter.Presenter;

/**
 * Create by Administrator
 * time: 2017/12/25.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class AccountBoundPressenter extends Presenter<AccountBoundView> {
    private final MainRepostiory mMainRestiory;

    public AccountBoundPressenter() {
        this.mMainRestiory =new MainRepostiory();
    }

    /**
     * @param type 1为微信，2为支付宝
     */
    @SuppressWarnings("unchecked")
    public void boundUser(int type){
        getView().showLoading(null);
            addDisposable(mMainRestiory.userbeforeBindData(type).subscribe(
                    response -> {
                        getView().hideLoading();
                        getView().beforBindData(response.getResult());
                    },throwable -> {
                        if (!isNullView()){
                            getView().hideLoading();
                            getView().showError(0, getGeneralErrorStr(throwable));
                        }
                    }
            ));
    }


    public void unBoundUser(int type) {
        getView().showLoading(null);
        addDisposable(mMainRestiory.unboundUser().subscribe(response -> {
            getView().hideLoading();
            getView().unBoundSuccess();
        },throwable -> {
            if (!isNullView()){
                getView().hideLoading();
                getView().showError(0, getGeneralErrorStr(throwable));
            }
        }));
    }
}
