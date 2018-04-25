package com.huanmedia.videochat.pay;

import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;
import com.huanmedia.videochat.repository.entity.PayCoinTypeMode;

import java.util.HashMap;
import java.util.Map;

import mvp.presenter.Presenter;

/**
 * Create by Administrator
 * time: 2017/12/17.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class CoinPayPresenter extends Presenter<CoinPayView> {
    private final MainRepostiory mRepository;

    public CoinPayPresenter() {
        this.mRepository = new MainRepostiory();
    }

    public void getRechargeInfo() {

        addDisposable(mRepository.getRechargeInfo().subscribe(
                coinPayEntity ->
                        getView().showData(coinPayEntity)
                ,
                throwable -> {
                    if (!isNullView()) {
                        getView().showError(0, getGeneralErrorStr(throwable));
                    }
                }

        ));
    }

    @SuppressWarnings("unchecked")
    public void createOlder(PayCoinTypeMode payCoinTypeMode) {
        getView().showLoading(null);
        Map<String, String> map = new HashMap<>();
        map.put("totalprice", payCoinTypeMode.getPrice());
        map.put("coin", payCoinTypeMode.getCoin() + payCoinTypeMode.getExtra() + "");
        map.put("payid", payCoinTypeMode.getId());
        addDisposable(mRepository.createorder(map).subscribe(response -> {
            Map<String, Object> resustmap = (Map<String, Object>) response.getResult();
            getView().createOlderSuccess(payCoinTypeMode, resustmap.get("ordernum").toString());
        }, throwable -> {
            if (!isNullView()) {
                getView().showError(0, getGeneralErrorStr(throwable));
            }
        }, () -> {
            if (!isNullView()) getView().hideLoading();
        }));
    }
}
