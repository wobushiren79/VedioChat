package com.huanmedia.videochat.media.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseLinearLayout;

public class MediaPlayView extends BaseLinearLayout implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        View.OnClickListener {

    private String mVedioUrl;
    private VideoView mVedioView;
    private ImageView mVedioIcon;
    private MediaPlayer mMediaPlayer;

    public MediaPlayView(Context context, String vedioUrl) {
        super(context);
        this.mVedioUrl = vedioUrl;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.layout_media_play_item;
    }

    @Override
    protected void initView(View baseView) {
        mVedioView = getView(R.id.media_vp);
        mVedioIcon = getView(R.id.medio_icon);

        mVedioView.setOnPreparedListener(this);
        mVedioView.setOnCompletionListener(this);
        mVedioView.setOnErrorListener(this);
    }

    @Override
    protected void initData() {
        Uri uri = Uri.parse(mVedioUrl);
        mVedioView.setVideoURI(uri);
        mVedioView.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer = mp;
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
        if (mMediaPlayer == null)
            return;
        if (mVedioView.isPlaying()) {
            mMediaPlayer.stop();
            mVedioIcon.setImageResource(R.drawable.icon_video_play);
        } else {
            mMediaPlayer.start();
            mVedioIcon.setImageResource(R.drawable.icon_video_stop);
        }
    }
}
