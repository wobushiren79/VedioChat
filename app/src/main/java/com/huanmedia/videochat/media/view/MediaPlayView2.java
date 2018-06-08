package com.huanmedia.videochat.media.view;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import butterknife.BindView;

public class MediaPlayView2 extends BaseFragment {
    private String mVedioUrl;

    @BindView(R.id.video_play)
    StandardGSYVideoPlayer mVideoPlay;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_media_play_item_2;
    }

    @Override
    protected void initData() {
        mVideoPlay.setUp(mVedioUrl, true, "");
        mVideoPlay.startPlayLogic();
    }

    public void setVedioUrl(String vedioUrl) {
        this.mVedioUrl = vedioUrl;
    }

    public void startVideo() {
        mVideoPlay.startPlayLogic();
    }

    public void stopVideo() {
        mVideoPlay.onVideoPause();
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
