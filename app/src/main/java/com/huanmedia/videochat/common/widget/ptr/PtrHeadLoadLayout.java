package com.huanmedia.videochat.common.widget.ptr;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.applecoffee.devtools.custom.layout.ptr.CustomPtrLoadView;
import com.huanmedia.videochat.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class PtrHeadLoadLayout extends CustomPtrLoadView {

    private ImageView mIVLoad;
    private TextView mTVTitle;
    private AnimationDrawable mAnimDB;

    public PtrHeadLoadLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PtrHeadLoadLayout(Context context) {
        this(context, null);
    }

    @Override
    protected void initView() {
        mTVTitle = getView(R.id.tv_title);
        mIVLoad = getView(R.id.iv_anim);
        mAnimDB = (AnimationDrawable) getResources().getDrawable(R.drawable.anim_ptr_loading_style_1);
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
        Log.v("this", "ptr:onUIRefreshBegin");
        if (mAnimDB != null) {
            mIVLoad.setImageDrawable(mAnimDB);
            mAnimDB.start();
        }

    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame, boolean isHeader) {
        Log.v("this", "ptr:onUIRefreshComplete");
        if (mAnimDB != null)
            mAnimDB.stop();
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        Log.v("this", "ptr:onUIPositionChange");
        Log.v("this", "ptr:" + ptrIndicator.getCurrentPosY());
//
//        if (ptrIndicator.getCurrentPosY() < 150) {
//            mIVLoad.setImageDrawable(mAnimDB.getFrame(0));
//        } else if (ptrIndicator.getCurrentPosY() > 150) {
//            mIVLoad.setImageDrawable(mAnimDB.getFrame(1));
//        }

    }
}
