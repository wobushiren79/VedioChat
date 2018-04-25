package com.huanmedia.videochat.wxapi;

import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;

import java.util.HashMap;
import java.util.Map;

import mvp.data.net.ApiException;
import mvp.presenter.Presenter;

/**
 * Create by Administrator
 * time: 2017/12/17.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class PayOrderPresenter extends Presenter<PayOrderView>{

    private final MainRepostiory mRepository;

    public PayOrderPresenter() {
        this.mRepository = new MainRepostiory();
    }
    public void payOrder(String ordernum,String paytype){
        getView().showLoading();
        Map<String, String> prams = new HashMap<>();
        prams.put("ordernum",ordernum);
        prams.put("paytype",paytype);
        addDisposable(mRepository.payorder(prams).subscribe(payOlderEntity -> {
            getView().doingPay(payOlderEntity);
        },throwable -> {
            if (!isNullView()){
                if (throwable instanceof ApiException)
                    getView().error(throwable.getMessage());
                else {
                    throwable.printStackTrace();
                }
            }
        },() -> {
            if (!isNullView())getView().hideLoading();
        }));
    }
}
