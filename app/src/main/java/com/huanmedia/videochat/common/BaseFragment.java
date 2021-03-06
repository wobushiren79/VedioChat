package com.huanmedia.videochat.common;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.manager.ResourceManager;
import com.huanmedia.videochat.common.navigation.Navigator;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ericYang on 2017/5/26.
 * Email:eric.yang@huanmedia.com
 * 基础Fragment配置
 */

public abstract class BaseFragment extends Fragment {

    Navigator navigator = ResourceManager.getInstance().getNavigator();
    //-------沉浸式处理
    /**
     * 是否对用户可见
     */
    protected boolean mIsVisible;
    /**
     * 是否加载完成
     * 当执行完onViewCreated方法后即为true
     */
    protected boolean mIsPrepare;

    /**
     * 是否加载完成
     * 当执行完onViewCreated方法后即为true
     */
    protected boolean mIsImmersion;

    protected ImmersionBar mImmersionBar;
    private Unbinder mUnbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null)
            mUnbinder.unbind();
    }

    protected abstract int getLayoutId();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        if (isImmersionBarEnabled())
            statusBarConfig();

        if (isLazyLoad()) {
            mIsPrepare = true;
            onLazyLoad();
        } else {
            initData();
        }

    }


    public Navigator getNavigator() {
        return navigator;
    }


    protected void initView(View view) {
    }

    protected void initData() {
    }


    /**
     * 找到activity的控件
     *
     * @param <T> the type parameter
     * @param id  the id
     * @return the t
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T findActivityViewById(@IdRes int id) {
        return (T) getActivity().findViewById(id);
    }

    /**
     * 查找Fragment中的控件
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findViewById(@IdRes int id) {
        if (getView() != null)
            return (T) getView().findViewById(id);
        else
            return null;
    }


    //----------------沉浸式状态栏设置----------------

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    /**
     * 用户可见时执行的操作
     */
    protected void onVisible() {
        onLazyLoad();
    }

    /**
     * 是否懒加载
     *
     * @return the boolean
     */
    protected boolean isLazyLoad() {
        return true;
    }

    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return false;
    }


    private void onLazyLoad() {
        if (mIsVisible && mIsPrepare) {
            mIsPrepare = false;
            initData();
        }
    }

    /**
     * 初始化沉浸式
     */
    protected void statusBarConfig() {
        mImmersionBar = ImmersionBar.with(this);
        if (getTitlebarView() != null) {
            mImmersionBar.titleBar(getTitlebarView());
        }
        mImmersionBar
                .keyboardEnable(true)
                .navigationBarWithKitkatEnable(false).statusBarDarkFont(true)
                .init();
    }

    protected View getTitlebarView() {
        return findViewById(R.id.toolbar_content);
    }

    /**
     * 用户不可见执行
     */
    protected void onInvisible() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mImmersionBar != null)
            mImmersionBar.init();
    }
}
