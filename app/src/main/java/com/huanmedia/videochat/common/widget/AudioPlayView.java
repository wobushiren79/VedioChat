package com.huanmedia.videochat.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseLinearLayout;
import com.huanmedia.videochat.common.widget.dialog.GeneralDialog;
import com.huanmedia.videochat.mvp.entity.results.AudioFileResults;
import com.huanmedia.videochat.mvp.presenter.audio.AudioPlayPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.audio.IAudioPlayPresenter;
import com.huanmedia.videochat.mvp.view.audio.IAudioPlayView;

import butterknife.OnLongClick;

public class AudioPlayView extends BaseLinearLayout implements IAudioPlayView, View.OnClickListener {

    private TextView mTVAudioTime;
    private ImageView mIVHorn;
    private IAudioPlayPresenter mPlayPresenter;

    public AudioPlayView(Context context) {
        super(context);
    }

    public AudioPlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_audio_play;
    }

    @Override
    protected void initView(View baseView) {
        mTVAudioTime = getView(R.id.tv_audio_time);
        mIVHorn = getView(R.id.iv_horn);
        this.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mPlayPresenter = new AudioPlayPresenterImpl(this);
    }

    //----------播放处理------------
    @Override
    public void audioPlayComplete() {
        AnimationDrawable animationDrawable = (AnimationDrawable) mIVHorn.getDrawable();
        animationDrawable.stop();
        mIVHorn.setImageResource(R.drawable.icon_audio_horn_3);
    }

    @Override
    public void audioPlayDuration(int duration) {

    }

    @Override
    public void showToast(String toast) {

    }

    //----------点击--------------
    @Override
    public void onClick(View view) {
        mPlayPresenter.startPlay();
        mIVHorn.setImageResource(R.drawable.anim_audio_play);
        AnimationDrawable animationDrawable = (AnimationDrawable) mIVHorn.getDrawable();
        animationDrawable.start();
    }

    //-------------设置--------------
    public void setData(AudioFileResults results) {
        mPlayPresenter.prePlay(results.getAudiourl());
        mTVAudioTime.setText(results.getAudiotimes() + "''");
    }

}
