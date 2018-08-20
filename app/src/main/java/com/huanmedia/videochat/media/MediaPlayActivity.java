package com.huanmedia.videochat.media;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.media.view.MediaPlayListLayout;
import com.huanmedia.videochat.media.view.MediaPlayView;
import com.huanmedia.videochat.media.view.MediaPlayView2;
import com.huanmedia.videochat.repository.entity.VideoEntity;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MediaPlayActivity extends BaseActivity implements View.OnClickListener, MediaPlayListLayout.CallBack {

    @BindView(R.id.media_vp)
    MediaPlayListLayout mMediaoVP;
    @BindView(R.id.media_tv_page)
    TextView mTVPage;
    @BindView(R.id.iv_exit)
    ImageView mIVExit;

    private List<VideoEntity> mListVedioData;

    public static Intent getCallingIntent(Context context, List<VideoEntity> vedios, int position) {
        Intent intent = new Intent(context, MediaPlayActivity.class);
        intent.putExtra("vedios", (ArrayList) vedios);
        intent.putExtra("position", position);
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
        mIVExit.setOnClickListener(this);
        mMediaoVP.setCallBack(this);
        ((RelativeLayout.LayoutParams) mIVExit.getLayoutParams()).topMargin += DisplayUtil.getStatusBarHeight(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mListVedioData = getIntent().getParcelableArrayListExtra("vedios");
        if (mListVedioData == null || mListVedioData.size() <= 0)
            return;
        int position = getIntent().getIntExtra("position", 0);
        mMediaoVP.setListData(mListVedioData);
        mMediaoVP.scrollToPosition(position);
        mMediaoVP.playVideo(position);
        mTVPage.setText((position + 1) + "/" + mListVedioData.size());
    }


    @Override
    public void onClick(View v) {
        if (v == mIVExit) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }

    @Override
    public void onPageSelected(int position) {
        if (mTVPage != null)
            mTVPage.setText((position + 1) + "/" + mListVedioData.size());
    }
}


