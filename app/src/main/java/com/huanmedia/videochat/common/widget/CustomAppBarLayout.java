package com.huanmedia.videochat.common.widget;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;

import com.huanmedia.videochat.common.adpater.AppBarStateChangeListener;

public class CustomAppBarLayout extends AppBarLayout {

    AppBarStateChangeListener.State appbarState;
    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;

    }


    public CustomAppBarLayout(Context context) {
        this(context, null);
    }

    public CustomAppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        appbarState = AppBarStateChangeListener.State.EXPANDED;
        this.addOnOffsetChangedListener(changeListener);
    }

    AppBarStateChangeListener changeListener = new AppBarStateChangeListener() {
        @Override
        public void onStateChanged(AppBarLayout appBarLayout, State state) {
            appbarState = state;
            if (callBack != null) {
                if (state == State.EXPANDED) {
                    //展开状态
                    callBack.onExpanded(appBarLayout);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    callBack.onCollapsed(appBarLayout);
                } else {
                    //中间状态
                    callBack.onIdle(appBarLayout);
                }
            }

        }
    };


    public AppBarStateChangeListener.State getAppbarState() {
        return appbarState;
    }

    public interface CallBack {
        void onExpanded(AppBarLayout appBarLayout);

        void onCollapsed(AppBarLayout appBarLayout);

        void onIdle(AppBarLayout appBarLayout);
    }

}
