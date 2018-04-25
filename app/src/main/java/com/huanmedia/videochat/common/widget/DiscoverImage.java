package com.huanmedia.videochat.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.huanmedia.ilibray.utils.DisplayUtil;
import com.makeramen.roundedimageview.RoundedImageView;
import com.orhanobut.logger.Logger;

/**
 * Create by Administrator
 * time: 2017/12/12.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class DiscoverImage extends RoundedImageView {
    private int downIndex;
    private float downX;
    private float downY;
    private int minChange = DisplayUtil.dip2px(getContext(), 8);
    private boolean mCanClick;
    private MotionEvent downEvent;

    public DiscoverImage(Context context) {
        super(context);
    }

    public DiscoverImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DiscoverImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                downEvent=event;
//                mCanClick = true;
//                downIndex = event.getActionIndex();
//                downX = event.getX();
//                downY = event.getY();
//               break;
//            case MotionEvent.ACTION_MOVE:
//                float moveX = Math.abs(downX - event.getX());
//                float moveY = Math.abs(downY - event.getY());
//                if (moveX > minChange || moveY > minChange) {
//                    mCanClick = false;
//                    event.setAction(MotionEvent.ACTION_DOWN);
//                    getRootView().onTouchEvent(event);
//                    Logger.i("onTouchEvent MX:%f MY:%f", moveX, moveY);
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                if (mCanClick) {
//                    performClick();
//                }
//                break;
//        }
//        return false;
//    }


}
