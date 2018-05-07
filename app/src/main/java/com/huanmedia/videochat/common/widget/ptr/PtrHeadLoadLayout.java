package com.huanmedia.videochat.common.widget.ptr;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.applecoffee.devtools.custom.layout.ptr.CustomPtrLoadView;
import com.huanmedia.videochat.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class PtrHeadLoadLayout extends CustomPtrLoadView {

    public PtrHeadLoadLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PtrHeadLoadLayout(Context context) {
        this(context, null);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.layout_ptr_head_load;
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame, boolean isHeader) {

    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

    }
}
