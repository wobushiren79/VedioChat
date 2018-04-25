package com.huanmedia.videochat.common;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public abstract class BaseLinearLayout extends LinearLayout {
    protected View mView;

    public BaseLinearLayout(Context context) {
        this(context, null);
    }

    public BaseLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mView = LayoutInflater.from(context).inflate(getLayoutId(), null);
        addView(mView);
        initView(mView);
        initData();
    }

    protected abstract int getLayoutId();

    protected abstract void initView(View baseView);

    protected abstract void initData();

    public <T extends View> T getView(int viewId){
       return mView.findViewById(viewId);
    }
}
