package com.huanmedia.videochat.main2.fragment;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.common.widget.video.VideoPtrLayout;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import butterknife.BindView;

public class VideoListFragment extends BaseFragment {
    @BindView(R.id.ptr_layout)
    VideoPtrLayout mPtrLayout;

    public VideoListFragment() {
    }

    public static VideoListFragment newInstance() {
        VideoListFragment fragment = new VideoListFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video_list;
    }


    @Override
    protected void onVisible() {
        super.onVisible();
        mPtrLayout.playVideo();
    }


    @Override
    protected void onInvisible() {
        super.onInvisible();
        GSYVideoManager.onPause();
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
//        GSYVideoManager.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }
}
