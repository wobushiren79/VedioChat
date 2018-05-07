package com.huanmedia.videochat.media;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;

import butterknife.BindView;

public class MediaPlayActivity extends BaseActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    @BindView(R.id.media_video_view)
    VideoView mVideoView;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, MediaPlayActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_media_play;
    }

    @Override
    protected void initView() {
        super.initView();
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnCompletionListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        Uri uri = Uri.parse("http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4");
        mVideoView.setVideoURI(uri);
        mVideoView.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setLooping(true);
        mp.start();// 播放
    }
}
