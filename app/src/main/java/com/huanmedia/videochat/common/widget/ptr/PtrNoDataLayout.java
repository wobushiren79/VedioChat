package com.huanmedia.videochat.common.widget.ptr;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseLinearLayout;

public class PtrNoDataLayout extends BaseLinearLayout {
    public PtrNoDataLayout(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_ptr_nodata;
    }

    @Override
    protected void initView(View baseView) {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(layoutParams);
        mView.setLayoutParams(layoutParams);
    }

    @Override
    protected void initData() {

    }
}
