package com.huanmedia.videochat.video.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.main2.weight.ConditionEntity.VideoType;
import com.huanmedia.videochat.repository.entity.VideoChatEntity;

import io.reactivex.disposables.Disposable;
import mvp.data.store.DataKeeper;

/**
 * Create by Administrator
 * time: 2017/12/13.
 * Email:eric.yang@huanmedia.com
 * version:底部按钮控制器
 */

public class CallButtomBtns extends RelativeLayout implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private @VideoType
    int mType;
    private ImageView mIvBeauty;
    private CheckBox mCbAttention;
    //    private ImageView mIvMaskWo;
    private ImageView mIvMaskTa;
    private ImageView mIvAddTime;
    private CheckBox mIvHint;
    private ImageView mIvHintToast;
    private View mIvGift;
    private MatchListener mMatchListener;
    private RedmanListener mRedmanListener;
    private boolean isCheckFavorited;
    private ImageView mIvMask;
    private FrameLayout mCloseBtn;
    private View callingView;
    private int mNumberCount = 3;
    private Disposable sub;
    private ImageView mCloseIV;


    public CallButtomBtns(Context context) {
        this(context, null);
    }

    public CallButtomBtns(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CallButtomBtns(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(VideoType.NONE);
    }

    /**
     * 设置是否展示关注
     */
    public void setShowAttention(int visibility) {
        mCbAttention.setVisibility(visibility);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CallButtomBtns(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setEnabledMask(boolean enabled) {
        if (mIvMask != null) {
            mIvMask.setEnabled(enabled);
        }
    }

    public boolean maskEnable() {
        if (mIvMask != null) {
            return mIvMask.isEnabled();
        }
        return false;
    }

    public void inflate(@VideoType int type) {
        this.mType = type;
        initView();
    }

    public void initButionStatus(VideoChatEntity conditionEntity) {
        if (conditionEntity == null) return;
        mCbAttention.setOnCheckedChangeListener(null);
        this.isCheckFavorited = false;
        this.isCheckFavorited = conditionEntity.getFavorited() == 1;
        mCbAttention.setChecked(this.isCheckFavorited);
        mCbAttention.setOnCheckedChangeListener(this);
        if (conditionEntity.get_location_videoType() == VideoType.MATCH) {
            if (conditionEntity.getTomask() == 0) {
                mIvMaskTa.setOnClickListener(null);
                mIvMaskTa.setEnabled(false);
            } else {
                mIvMaskTa.setEnabled(true);
                mIvMaskTa.setOnClickListener(this);
            }
        }

    }


    public void setMatchListener(MatchListener matchListener) {
        mMatchListener = matchListener;
    }

    public void setRedmanListener(RedmanListener redmanListener) {
        mRedmanListener = redmanListener;
    }

    private void initView() {
        if (callingView == null) {
            callingView = LayoutInflater.from(getContext()).inflate(R.layout.video_calling_control, this);
            mCloseBtn = findViewById(R.id.video_call_fl_close);
            mCloseIV = findViewById(R.id.video_call_iv_close);
            mIvGift = findViewById(R.id.video_call_iv_gift);
            mIvMaskTa = findViewById(R.id.video_call_iv_ta);
            mIvMask = findViewById(R.id.video_call_iv_mask);
            mCbAttention = findViewById(R.id.video_call_cb_attention);
            mIvBeauty = findViewById(R.id.video_call_iv_beauty);
            mIvAddTime = findViewById(R.id.video_call_iv_addtime);
            mIvHint = findViewById(R.id.video_call_iv_hint_button);
            mIvHintToast = findViewById(R.id.video_call_iv_hint_toast);
        }
        mIvHint.setVisibility(VISIBLE);
        mCloseBtn.setVisibility(VISIBLE);
        mIvMask.setVisibility(VISIBLE);
        mIvMaskTa.setVisibility(VISIBLE);
        mIvGift.setVisibility(VISIBLE);
        mIvBeauty.setVisibility(VISIBLE);
        mCbAttention.setVisibility(VISIBLE);
        mIvBeauty.setVisibility(VISIBLE);

        mCloseBtn.setOnClickListener(this);
        switch (mType) {
            case VideoType.MATCH:
                //美颜
                mIvBeauty.setOnClickListener(this);
                //面具
                mIvMask.setOnClickListener(this);
                //不需要不可关注
                mCbAttention.setVisibility(GONE);
                //礼物
                mIvGift.setOnClickListener(this);
                //添加时长提示
                mIvAddTime.setOnClickListener(this);
                //视频遮罩
                mIvHint.setVisibility(GONE);
                break;
            case VideoType.REDMAN:
            case VideoType.APPOINTMENT:
                mIvMaskTa.setVisibility(GONE);//红人不需要揭面
                //面具
                mIvMask.setOnClickListener(this);
                //关注
                mCbAttention.setChecked(isCheckFavorited);
                mCbAttention.setOnCheckedChangeListener(this);
                //礼物
                mIvGift.setOnClickListener(this);
                //美颜
                mIvBeauty.setOnClickListener(this);
                //视频遮罩
                mIvHint.setOnClickListener(this);
                videoHintToast();
                break;
            case VideoType.NONE://只有关闭按钮可用
                mIvMaskTa.setVisibility(GONE);
                mIvGift.setVisibility(GONE);
                mCbAttention.setVisibility(GONE);
                mIvGift.setVisibility(GONE);
                mIvMask.setVisibility(GONE);
                mIvBeauty.setVisibility(GONE);
                mIvHint.setVisibility(GONE);
                break;
        }
    }

    private void videoHintToast() {
        DataKeeper dataKeeper = new DataKeeper(getContext(), DataKeeper.DEFULTFILE);
        boolean isFirst = dataKeeper.get("VideoHintShow", true);
        if (isFirst) {
            mIvHintToast.setVisibility(VISIBLE);
            AnimationSet animatorSet = new AnimationSet(true);
            ScaleAnimation scaleAnimation = new ScaleAnimation
                    (1, 1.2f, 1, 1.2f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setRepeatCount(8);
            scaleAnimation.setDuration(1000);
            scaleAnimation.setRepeatMode(Animation.REVERSE);
            animatorSet.addAnimation(scaleAnimation);
            animatorSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mIvHintToast.setVisibility(GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mIvHintToast.startAnimation(animatorSet);
            dataKeeper.put("VideoHintShow", false);
        } else {
            mIvHintToast.setVisibility(GONE);
        }

    }

    /**
     * 设置视频开启按钮状态
     *
     * @param visibility
     */
    public void setVideoHintVisibility(int visibility) {
        mIvHint.setVisibility(visibility);
    }

    public void showNumberCountDown() {
        if (sub != null && !sub.isDisposed()) {
            return;
        }
        mCloseBtn.setEnabled(false);
        sub = RxCountDown.countdown(mNumberCount).subscribe(
                integer -> {
                    mCloseIV.getDrawable().setLevel(integer);
                    if (integer == 0) {
                        mCloseBtn.setEnabled(true);
                    }
                }
        );
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (sub != null)
            sub.dispose();
    }

    @Override
    public void onClick(View v) {
        switch (mType) {
            case VideoType.MATCH:
                if (mMatchListener == null) return;
                switch (v.getId()) {
                    case R.id.video_call_iv_mask:
                        mMatchListener.onMaskWo(v);
                        break;
                    case R.id.video_call_iv_ta:
                        mMatchListener.onMaskTa(v);
                        break;
                    case R.id.video_call_iv_beauty:
                        mMatchListener.onBeauty();
                        break;
                    case R.id.video_call_iv_gift:
                        mMatchListener.onGift(v);
                        break;
                    case R.id.video_call_fl_close:
                        mMatchListener.close();
                        break;
                    case R.id.video_call_iv_addtime:
                        mIvAddTime.setVisibility(GONE);
                        mMatchListener.onGift(v);
                        break;
                }

                break;
            case VideoType.REDMAN:
            case VideoType.APPOINTMENT:
                if (mRedmanListener == null) break;
                switch (v.getId()) {
                    case R.id.video_call_iv_mask:
                        mRedmanListener.onMaskWo(v);
                        break;
                    case R.id.video_call_iv_beauty:
                        mRedmanListener.onBeauty();
                        break;
                    case R.id.video_call_iv_gift:
                        mRedmanListener.onGift(v);
                        break;
                    case R.id.video_call_fl_close:
                        mRedmanListener.close();
                        break;
                    case R.id.video_call_iv_hint_button:
                        mRedmanListener.onVideoHint();
                        break;
                }
                break;
            case VideoType.NONE:
                if (mRedmanListener != null) {
                    mRedmanListener.close();
                }
                if (mMatchListener != null) {
                    mMatchListener.close();
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (mType) {
            case VideoType.MATCH:
                if (mMatchListener != null) {
                    mMatchListener.onBeauty();
                }
                break;
            case VideoType.REDMAN:
            case VideoType.APPOINTMENT:
                if (mRedmanListener != null) {
                    mRedmanListener.onAttention(isChecked);
                }
                break;
        }
    }

    public void setAddTimeShow(int visibility) {
        mIvAddTime.setVisibility(visibility);
    }

    public interface MatchListener {
        void onMaskTa(View view);

        void onMaskWo(View view);

        void onGift(View view);

        void onBeauty();

        void close();
    }

    public interface RedmanListener {
        void onAttention(boolean isChecked);

        void onGift(View view);

        void onBeauty();

        void onMaskWo(View view);

        void onVideoHint();

        void close();
    }


}
