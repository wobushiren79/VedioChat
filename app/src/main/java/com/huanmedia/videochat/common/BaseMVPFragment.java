package com.huanmedia.videochat.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import mvp.presenter.Presenter;
import mvp.presenter.PresenterBulide;
import mvp.view.BaseView;

/**
 * Created by ericYang on 2017/5/26.
 * Email:eric.yang@huanmedia.com
 * 基础Fragment配置
 */

public abstract class BaseMVPFragment<P extends Presenter> extends BaseFragment {
    private P mBasePresenter;
    //-------沉浸式处理


    public P getBasePresenter() {
        return mBasePresenter;
    }

    @SuppressWarnings("unchecked")
    public void setBasePresenter() {
        try {
            mBasePresenter = PresenterBulide.newPresenterInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (mBasePresenter!=null){
                mBasePresenter.setView((BaseView) this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setBasePresenter();
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onDetach() {
        if (mBasePresenter != null) {
            mBasePresenter.destroy();
        }
        super.onDetach();

    }
}
