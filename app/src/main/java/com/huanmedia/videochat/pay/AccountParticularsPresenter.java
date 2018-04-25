package com.huanmedia.videochat.pay;

import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;

import mvp.presenter.Presenter;

/**
 * Create by Administrator
 * time: 2017/12/26.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class AccountParticularsPresenter extends Presenter<AccountParticularsView>{

    private final MainRepostiory mMainRepostiory;

    public AccountParticularsPresenter() {
        mMainRepostiory=new MainRepostiory();
    }

    /**
     * @param positon 0 标识选择可提现细明 1标识钻细明
     * @param page
     * @param data
     */
    public void loadData(int positon, int page, String data) {
        addDisposable(mMainRepostiory.getBill(positon==0?2:1,page,data).subscribe(
                cashListEntity -> {
                    getView().showDataForType(positon,cashListEntity);
                }
                ,
                throwable -> {
                    if (!isNullView()){
                        getView().showHint(positon,1000,getGeneralErrorStr(throwable));
                    }
                }
        ));

    }
}
