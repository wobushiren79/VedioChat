package com.huanmedia.videochat.main2.fragment;

import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;

import mvp.presenter.Presenter;

/**
 * Create by Administrator
 * time: 2017/12/18.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class MyPresenter extends Presenter<MyView> {
    private final MainRepostiory mMainRepostiory;


    public MyPresenter() {
        mMainRepostiory = new MainRepostiory();
    }



}
