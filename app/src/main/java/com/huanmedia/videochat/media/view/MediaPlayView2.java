package com.huanmedia.videochat.media.view;

import android.view.View;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.common.widget.video.EmptyVideoPlayer;
import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import butterknife.BindView;

public class MediaPlayView2 extends BaseFragment {
    private String mVedioUrl;

    @BindView(R.id.video_play)
    EmptyVideoPlayer mVideoPlay;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_media_play_item_2;
    }

    @Override
    protected void initData() {
        mVideoPlay.setLooping(true);
        mVideoPlay.setUp(mVedioUrl, false, "");
        mVideoPlay.startPlayLogic();
    }

    public void setVedioUrl(String vedioUrl) {
        this.mVedioUrl = vedioUrl;
    }

    public void startVideo() {
        if (mVideoPlay != null){
            mVideoPlay.setLooping(true);
            mVideoPlay.setUp(mVedioUrl, false, "");
            mVideoPlay.startPlayLogic();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
