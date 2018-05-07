package com.huanmedia.videochat.video.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huanmedia.videochat.R;

public class TimerTextView extends LinearLayout {
    private View mView;
    private TextView mTimeText;

    private int timeAnimMax = 20;//倒计时动画时间
    private boolean isAnimStart = false;//是否开始动画

    public TimerTextView(Context context) {
        this(context, null);
    }

    public TimerTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mView = View.inflate(getContext(), R.layout.layout_timer_text_view, this);
        mTimeText = mView.findViewById(R.id.view_time_text);
    }

    /**
     * 设置倒计时文字
     *
     * @param time
     */
    public void setTimeDownText(int time) {
        mTimeText.setText(time + "s");
        if (time <= timeAnimMax) {
            if (!isAnimStart) {
                mTimeText.startAnimation(timeAnima());
                mTimeText.setTextColor(Color.RED);
            }

        } else {
            isAnimStart = false;
            mTimeText.clearAnimation();
            mTimeText.setTextColor(Color.WHITE);
        }
    }

    /**
     * 设置计时文字
     *
     * @param time
     */
    public void setTimeUpText(String time) {
        mTimeText.setText(time);
        mTimeText.clearAnimation();
        mTimeText.setTextColor(Color.WHITE);
    }


    /**
     * 倒计时动画
     *
     * @return
     */
    private AnimationSet timeAnima() {
        isAnimStart = true;
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation
                (1, 2.5f, 1, 2.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.6f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        scaleAnimation.setRepeatMode(Animation.REVERSE);

        animationSet.addAnimation(scaleAnimation);
        return animationSet;
    }
}
