package com.huanmedia.videochat.mvp.base;

public class BaseMVPPresenter<T extends BaseMVPView, V extends BaseMVPModel> {
    public T mMvpView;
    public V mMvpModel;

    public BaseMVPPresenter(T mMvpView,Class<V> cls) {
        try {
            this.mMvpView = mMvpView;
            this.mMvpModel = cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
