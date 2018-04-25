package com.huanmedia.videochat.common.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Create by Administrator
 * time: 2018/3/2.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class NoFocusRecycleView extends RecyclerView{
    public NoFocusRecycleView(Context context) {
        super(context);
    }

    public NoFocusRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoFocusRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false;
    }

}
