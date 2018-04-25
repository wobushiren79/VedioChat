package com.huanmedia.videochat.pay;

import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;
import com.huanmedia.videochat.repository.entity.UserAccountBoundEntity;

import mvp.presenter.Presenter;

/**
 * Create by Administrator
 * time: 2017/12/21.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class MyAccountPresenter extends Presenter<MyAccountView> {
    private final MainRepostiory mMainRepostiory;
    private UserAccountBoundEntity mUserAccount;

    public MyAccountPresenter() {
        mMainRepostiory = new MainRepostiory();
    }


    private boolean checkMoney(String moneyStr) {
        if (mUserAccount == null) {
            getView().showHint(HintDialog.HintType.WARN, "提现错误，未能获取到账户信息");
            return false;
        }
        if (mUserAccount.getIsBindPay() != 1) {
            getView().showHint(HintDialog.HintType.WARN, "未绑定提现账户");
            return false;
        }

        if (!Check.isEmpty(moneyStr)) {
            try {
                double money = Double.parseDouble(moneyStr);
                if (money <= UserManager.getInstance().getRealMoney(mUserAccount.getExchangecoin()).doubleValue()) {
                    if (money >= 200) {//money 必须是10的倍数 切大于200
                        if (money % 10 == 0) {//money 必须是10的倍数
                            return true;
                        } else {
                            getView().showHint(HintDialog.HintType.WARN, "提现数额必须是10的倍数");
                        }
                    } else {
                        getView().showHint(HintDialog.HintType.WARN, "最低提现额度为200");
                    }
                } else {
                    getView().showHint(HintDialog.HintType.WARN, "可提现余额不足");
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public void cashMoney(String moneystr) {
        if (checkMoney(moneystr)) {
            getView().showLoading(null);
            addDisposable(mMainRepostiory.userdocash((int)Math.floor(Integer.parseInt(moneystr) / UserManager.getInstance().getExchangeRate())).subscribe(
                    response -> {
                        getView().hideLoading();
                        getView().showHint(4, "系统将在5工作日内转入您的账户");
                    },
                    throwable -> {
                        if (!isNullView()) {
                            getView().hideLoading();
                            getView().showHint(HintDialog.HintType.WARN, getGeneralErrorStr(throwable));
                        }
                    }
            ));
        }
    }

    public UserAccountBoundEntity getUserAccount() {
        return mUserAccount;
    }

    public void getData() {
        getView().showLoading(null);
        addDisposable(mMainRepostiory.userAccountBoundInfo().subscribe(userAccountBoundEntity -> {
            this.mUserAccount = userAccountBoundEntity;
            getView().hideLoading();
            getView().showData(userAccountBoundEntity);
        }, throwable -> {
            if (!isNullView()) {
                getView().hideLoading();
                getView().showError(0, getGeneralErrorStr(throwable));
            }
        }));
    }
}
