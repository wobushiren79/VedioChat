package com.huanmedia.videochat.pay;

import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;

import mvp.presenter.Presenter;

/**
 * Create by Administrator
 * time: 2017/12/21.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class ExchangePresenter extends Presenter<ExchangeView> {
    private final MainRepostiory mMainRepostiory;
//    private UserAccountBoundEntity mUserAccount;

    public ExchangePresenter() {
        mMainRepostiory = new MainRepostiory();
    }


    private boolean checkMoney(String moneyStr) {

        if (!Check.isEmpty(moneyStr)) {
            try {
                double coin = Double.parseDouble(moneyStr);
                if (coin <= UserManager.getInstance().getCurrentUser().getUserinfo().getExchangecoin()) {
                    if (coin != 0 && coin%100==0) {//money 必须是100的倍数
                            return true;
                    } else {
                        getView().showHint(HintDialog.HintType.WARN, "兑换数额≥100 (100的倍数)");
                    }
                } else {
                    getView().showHint(HintDialog.HintType.WARN, "可兑换余额不足");
                }
            } catch (Exception e) {
                return false;
            }
        }else {
            getView().showHint(HintDialog.HintType.WARN, "请输入需要兑换钻石数量");
        }
        return false;
    }

    public void cashMoney(String moneystr) {
        if (checkMoney(moneystr)) {
            getView().showLoading(null);
            addDisposable(mMainRepostiory.usercointorecharge(Integer.parseInt(moneystr))
                    .flatMap(response -> mMainRepostiory.myinfo())
                    .subscribe(
                    response -> {
                        UserManager.getInstance().getCurrentUser().setUserinfo(response);
                        UserManager.getInstance().saveUser();
                        getView().hideLoading();
                        getView().showHint(4, "兑换成功");
                    },
                    throwable -> {
                        if (!isNullView()) {
                            getView().hideLoading();
                            getView().showHint(10000, getGeneralErrorStr(throwable));
                        }
                    }
            ));
        }
    }

}
