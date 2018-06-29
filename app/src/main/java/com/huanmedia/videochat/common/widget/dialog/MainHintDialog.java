package com.huanmedia.videochat.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.huanmedia.ilibray.utils.DevUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.navigation.Navigator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import mvp.data.store.glide.GlideApp;

public class MainHintDialog extends Dialog {

    private @MainHintType
    int mMainHintType;
    private String mContentStr;
    private String mTitleStr;
    private String mImageUrl;
    private String mJumpUrl;

    @Retention(RetentionPolicy.SOURCE)
    public @interface MainHintType {
        //普通类型
        int Normal = 0;
        //金币类型
        int Coin = 1;
    }

    public MainHintDialog(@NonNull Context context, @MainHintType int mMainHintType) {
        super(context, R.style.customDialog);
        this.mMainHintType = mMainHintType;
        this.mContentStr = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (mMainHintType) {
            case 1:
                setContentView(R.layout.dialog_main_hint_type_1);
                break;
            default:
                setContentView(R.layout.dialog_main_hint_type_0);
                break;
        }
        if (getWindow() != null) {
            getWindow().getAttributes().gravity = Gravity.CENTER;
            getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        initView();
        initData();
    }


    private void initView() {
        ImageView ivContent = findViewById(R.id.iv_content);
        ImageView ivCancel = findViewById(R.id.iv_cancel);
        switch (mMainHintType) {
            case 1:
                ImageView ivBackDebris = findViewById(R.id.iv_back_debris);
                ImageView ivBackLight = findViewById(R.id.iv_back_light);
                TextView tvContent = findViewById(R.id.tv_content);

                tvContent.setText(mContentStr);
                setBackLightAnim(ivBackLight);
                setBackDebris(ivBackDebris);
                setCoinAnim(ivContent);
                setContentTextAnim(tvContent);
                break;
            default:
                GlideApp.with(getContext()).load(mImageUrl).into(ivContent);
                ivContent.setOnClickListener(view -> {
                    if (mJumpUrl != null && mJumpUrl.length() > 0) {
                        Navigator navigator = new Navigator();
                        navigator.navtoWebActivity(DevUtils.scanForActivity(getContext()), mJumpUrl, null,true);
                        this.cancel();
                    }
                });
                break;
        }

        ivCancel.setOnClickListener(view -> {
            this.cancel();
        });
    }


    private void initData() {

    }

    /**
     * 设置正文内容
     *
     * @param contentText
     */
    public void setContentText(String contentText) {
        mContentStr = contentText;
    }


    /***
     * 设置标题文字
     * @param mTitleStr
     */
    public void setTitleStr(String mTitleStr) {
        this.mTitleStr = mTitleStr;
    }

    /**
     * 设置图片内容
     *
     * @param mImageUrl
     */
    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    /**
     * 设置跳转地址
     *
     * @param mJumpUrl
     */
    public void setJumpUrl(String mJumpUrl) {
        this.mJumpUrl = mJumpUrl;
    }

    /**
     * 设置背景光特效
     *
     * @param ivBackLight
     */
    private void setBackLightAnim(ImageView ivBackLight) {
        AnimationSet animationSet = new AnimationSet(true);
        RotateAnimation rotateAnimation = new RotateAnimation
                (0, 360,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(30000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        animationSet.addAnimation(rotateAnimation);
        animationSet.setInterpolator(new LinearInterpolator());
        ivBackLight.startAnimation(animationSet);
    }

    /**
     * 设置金币特效
     *
     * @param ivCoin
     */
    private void setCoinAnim(ImageView ivCoin) {
        ScaleAnimation scaleAnimation = new ScaleAnimation
                (0, 1, 0, 1,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setInterpolator(new BounceInterpolator());
        ivCoin.startAnimation(scaleAnimation);
    }

    /**
     * 设置碎片特效
     *
     * @param ivDebris
     */
    private void setBackDebris(ImageView ivDebris) {
        TranslateAnimation translateAnimation = new TranslateAnimation
                (Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0.2f);
        translateAnimation.setDuration(10000);
        translateAnimation.setRepeatCount(Animation.INFINITE);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        ivDebris.startAnimation(translateAnimation);
    }


    /**
     * 设置正文动画
     *
     * @param tvText
     */
    private void setContentTextAnim(TextView tvText) {
        ScaleAnimation scaleAnimation = new ScaleAnimation
                (0, 1, 0, 1,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setStartOffset(250);
        scaleAnimation.setInterpolator(new LinearInterpolator());
        scaleAnimation.setInterpolator(new BounceInterpolator());
        tvText.startAnimation(scaleAnimation);
    }
}
