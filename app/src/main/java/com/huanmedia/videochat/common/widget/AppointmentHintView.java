package com.huanmedia.videochat.common.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.applecoffee.devtools.base.layout.BaseLinearLayout;
import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.utils.UMengUtils;


import io.reactivex.disposables.Disposable;

public class AppointmentHintView extends BaseLinearLayout implements View.OnTouchListener {

    private TextView mTVTitle;
    private TextView mTVTime;
    private boolean hasAnim = false;
    private int lastX, lastY;
    private int mReadManId = 0;
    private int mNormalId = 0;

    private AnimationSet mAnimationSet;
    private Disposable mCountDownDis;
    private DisplayMetrics dm;

    public AppointmentHintView(Context context) {
        this(context, null);
    }

    public AppointmentHintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void initView() {
        mTVTime = mLayoutView.findViewById(R.id.tv_time);
        mTVTitle = mLayoutView.findViewById(R.id.tv_title);
        this.setOnTouchListener(this);
    }

    @Override
    protected void initData() {
        dm = getResources().getDisplayMetrics();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.layout_appointment_hint_view;
    }

    /***
     * 开始倒计时
     * @param second
     */
    public void startCountDown(int second, int fromId, int toId) {
        if (second <= 0)
            return;
        mReadManId = fromId;
        mNormalId = toId;
        this.setVisibility(VISIBLE);
        showViewAnim();
        if (mCountDownDis != null)
            mCountDownDis.dispose();
        mCountDownDis = RxCountDown.countdown(second).subscribe(integer -> {
            String timeStr = "";
            if (integer == 0) {
                hasAnim = false;
                this.clearAnimation();
                this.setVisibility(GONE);
            } else if (integer <= 30) {
                timeStr = integer + "s";
                finalCountDownAnim();
            } else {
                timeStr = (integer / 60) + 1 + "min";
            }
            mTVTime.setText(timeStr);
        });
    }

    /**
     * 最后60秒动画
     */
    private void finalCountDownAnim() {
        if (hasAnim)
            return;
        this.clearAnimation();
        mAnimationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation
                (1, 1.2f, 1, 1.2f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        scaleAnimation.setDuration(500);
        mAnimationSet.addAnimation(scaleAnimation);
        mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                hasAnim = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                hasAnim = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        this.startAnimation(scaleAnimation);
    }

    /**
     * 出现动画
     */
    private void showViewAnim() {
        if (hasAnim)
            return;
        this.clearAnimation();
        mAnimationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation
                (0, 1f, 0, 1f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        scaleAnimation.setDuration(1000);
        alphaAnimation.setDuration(1000);
        mAnimationSet.addAnimation(scaleAnimation);
        mAnimationSet.addAnimation(alphaAnimation);
        mAnimationSet.setRepeatCount(1);
        mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                hasAnim = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                hasAnim = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        this.startAnimation(scaleAnimation);
    }


    private int l;
    private int b;
    private int r;
    private int t;

    private int downX;
    private int downY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int ea = event.getAction();
        final int screenWidth = dm.widthPixels;
        final int screenHeight = dm.heightPixels;
        switch (ea) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();// 获取触摸事件触摸位置的原始X坐标
                lastY = (int) event.getRawY();
                downX = (int) event.getRawX();
                downY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                l = v.getLeft() + dx;
                b = v.getBottom() + dy;
                r = v.getRight() + dx;
                t = v.getTop() + dy;
                // 下面判断移动是否超出屏幕
                if (l < 0) {
                    l = 0;
                    r = l + v.getWidth();
                }
                if (t < 0) {
                    t = 0;
                    b = t + v.getHeight();
                }
                if (r > screenWidth) {
                    r = screenWidth;
                    l = r - v.getWidth();
                }
                if (b > screenHeight) {
                    b = screenHeight;
                    t = b - v.getHeight();
                }
                v.layout(l, t, r, b);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                v.postInvalidate();

                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) getLayoutParams();
                lp.setMargins(l, t, screenWidth - r, screenHeight - b);
                setLayoutParams(lp);
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(downX - (int) event.getRawX()) > 50 || Math.abs(downY - (int) event.getRawY()) > 50) {

                } else {
                    int tabPosition = 0;
                    if (UserManager.getInstance().getCurrentUser().getId() == mReadManId) {
                        tabPosition = 0;
                    } else if (UserManager.getInstance().getCurrentUser().getId() == mNormalId) {
                        tabPosition = 1;
                    }
                    UMengUtils.AppointmentHintClick(getContext());
                    ((BaseActivity) getContext()).getNavigator().navtoAppointmentList((Activity) getContext(), tabPosition);
                }
                break;
            default:
                break;
        }
        return true;
    }
}
