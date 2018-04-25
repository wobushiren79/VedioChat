package com.huanmedia.videochat.my;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;

import java.util.HashMap;
import java.util.Map;

import mvp.data.net.ApiException;
import mvp.presenter.Presenter;

/**
 * Create by Administrator
 * time: 2018/3/8.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class ReadMainPresenter extends Presenter<ReadMainView> {
    private final MainRepostiory mRepostiory;

    public ReadMainPresenter() {
        mRepostiory = new MainRepostiory();
    }

    public void getReadMainConfig() {
        getView().showLoading(getContext().getString(R.string.loading));
        addDisposable(mRepostiory.getuserwxqqcoin().subscribe(
                response -> {
                    getView().hideLoading();
                    getView().showReadMainConfig(response);
                }, throwable -> {
                    if (!isNullView()) {
                        getView().hideLoading();
                        if (throwable instanceof ApiException) {
                            if (((ApiException) throwable).getErrorCode() == -1) {
                                return;
                            }
                        }
                        getView().showError(0, getGeneralErrorStr(throwable));
                    }
                }
        ));
    }

    /**
     * 红人价格设置
     *
     * @param times      时间
     * @param coins      钻石数
     * @param coinstatus 是否开启 1开启0关闭
     */
    public void setReadMain(int times, int coins, int coinstatus) {
        getView().showLoading("红人信息配置中...");
        addDisposable(mRepostiory.starchange(times, coins, coinstatus).subscribe(response -> {
            getView().hideLoading();
            getView().showError(0, "红人信息配置成功");
            getView().setReadManChatPrice(coins + "");
        }, throwable -> {
            if (!isNullView()) {
                getView().hideLoading();
                getView().showError(0, getGeneralErrorStr(throwable));
            }
        }));
    }

    public void configPrice(String wx, String qq, int coin) {
        getView().showLoading("红人信息配置中...");
        Map<String, String> prams = new HashMap<>();
        if (wx != null) {
            prams.put("wx", wx);
        }
        if (qq != null) {
            prams.put("qq", qq);
        }
        if (coin != 0) {
            prams.put("coin", coin + "");
        }
        addDisposable(mRepostiory.userwxqqcoin(prams).subscribe(response -> {
                    getView().hideLoading();
                    getView().showError(0, "红人信息配置成功");
                },
                throwable -> {
                    if (!isNullView()) {
                        getView().hideLoading();
                        getView().showError(0, getGeneralErrorStr(throwable));
                    }
                }
        ));
    }

    public String[] getDiscoverDefaultMoney() {
        int price = 100;
        int size = 10;
        String[] prices = new String[size];

        for (int i = 1; i <= size; i++) {
            prices[i - 1] = (price * i) + "";
        }
        return prices;
    }

    public String[] getAccoundDefaultMoney() {
        return new String[]{"3000", "4000", "5000", "6000", "7000", "8000", "9000", "10000"};
    }
}
