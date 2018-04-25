package com.huanmedia.videochat.pay;

import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;

import mvp.presenter.Presenter;

/**
 * Create by Administrator
 * time: 2018/3/6.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class MyWalletPresenter extends Presenter<MyWalletView>{
    private final MainRepostiory mRepostiory;

    public MyWalletPresenter() {
        mRepostiory=new MainRepostiory();
    }

    /**
     * @param fragmentType 0 标识选择可提现细明 1标识钻细明
     * @param page
     * @param data
     */
    public void loadData(@MyWalletFragment.WallPageType int fragmentType, int page, String data) {
        getView().showLoading(null);
        addDisposable(mRepostiory.getBill((fragmentType)== MyWalletFragment.WallPageType.MCION?2:1,page,data).subscribe(
                cashListEntity -> {
                    getView().showDataForType(fragmentType,cashListEntity);
                    getView().hideLoading();
                }
                ,
                throwable -> {
                    if (!isNullView()){
                        getView().hideLoading();
                        getView().showHint(fragmentType,1000,getGeneralErrorStr(throwable));
                    }
                }
        ));

    }
}
