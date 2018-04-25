package com.huanmedia.videochat.my;

import com.huanmedia.ilibray.utils.AppValidationMgr;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;

import mvp.presenter.Presenter;

/**
 * Create by Administrator
 * time: 2018/1/3.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class BoundIDCardPresenter extends Presenter<BoundIDCardView>{
    private final MainRepostiory mMainRepostiory;

    public BoundIDCardPresenter() {
        mMainRepostiory=new MainRepostiory();
    }

    /**
     * 绑定用户
     * @param type 绑定类型 1微信 2支付宝
     * @param user_id 绑定账户ID
     */
    public void upUserBoundData(int type, String user_id,String name,String idcard) {
        if (type==0){
            getView().showError(0, "错误的账户类型");
        }else if (Check.isEmpty(user_id)){
            getView().showError(0, "错误的用户标识");
        }else if (!AppValidationMgr.isRealName(name)){
            getView().showError(0, "请输入真实姓名");
        }else if (!AppValidationMgr.isIDCard(idcard)){
            getView().showError(0, "请输入18位有效的身份证号码");
        }else {
            getView().showLoading(null);
            addDisposable(mMainRepostiory.boundUser(type,user_id,name,idcard).subscribe(response -> {
                getView().hideLoading();
                getView().boundSuccess();
            },throwable -> {
                if (!isNullView()){
                    getView().hideLoading();
                    getView().showError(0, getGeneralErrorStr(throwable));
                }
            }));
        }

    }
}
