package com.huanmedia.videochat.media.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseLinearLayout;

public class MediaPlayView extends BaseLinearLayout implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        View.OnClickListener {

    private String mVedioUrl;
    private RelativeLayout mRLLayout;
    private VideoView mVedioView;
    private ImageView mVedioIcon;

    public MediaPlayView(Context context, String vedioUrl) {
        super(context);
        this.mVedioUrl = vedioUrl;
        init();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.layout_media_play_item;
    }

    @Override
    protected void initView(View baseView) {
        mVedioView = getView(R.id.media_view);
        mVedioIcon = getView(R.id.medio_icon);
        mRLLayout = getView(R.id.media_rl_layout);
        mVedioView.setOnPreparedListener(this);
        mVedioView.setOnCompletionListener(this);
        mVedioView.setOnErrorListener(this);

        this.setBackgroundColor(getContext().getResources().getColor(R.color.base_black));

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRLLayout.setLayoutParams(layoutParams);
    }

    @Override
    protected void initData() {

    }

    private void init() {
        if (mVedioUrl == null)
            return;
        Uri uri = Uri.parse(mVedioUrl);
        mVedioView.setVideoURI(uri);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setLooping(true);
        mVedioView.setOnClickListener(this);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v == mVedioView) {
            changeVedioStatus();
        }
    }

    public void changeVedioStatus() {
        if (mVedioView == null)
            return;
        if (mVedioView.isPlaying()) {
            stopVideo();
        } else {
            mVedioView.start();
            mVedioIcon.setImageResource(R.drawable.icon_video_stop);
        }
    }

    /**
     * 暂停视频
     */
    public void stopVideo() {
        if (mVedioView != null && mVedioIcon != null) {
            mVedioView.pause();
            mVedioIcon.setImageResource(R.drawable.icon_video_play);
        }
    }
}
