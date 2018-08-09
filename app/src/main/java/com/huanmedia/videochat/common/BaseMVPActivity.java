package com.huanmedia.videochat.common;

import android.os.Bundle;

import mvp.presenter.Presenter;
import mvp.presenter.PresenterBulide;
import mvp.view.BaseView;


/**
 * Base {@link android.app.Activity} class for every Activity in this application.
 */
public abstract class BaseMVPActivity<P extends Presenter> extends BaseActivity {
    P mBasePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setPresenter();
        super.onCreate(savedInstanceState);

    }

    public P getBasePresenter() {
        return mBasePresenter;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getBasePresenter() != null) {//回收Presenter
            getBasePresenter().destroy();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getBasePresenter() != null) {
            getBasePresenter().resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getBasePresenter() != null) {
            getBasePresenter().pause();
        }
    }

    @SuppressWarnings("unchecked")
    public void setPresenter() {

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

}
