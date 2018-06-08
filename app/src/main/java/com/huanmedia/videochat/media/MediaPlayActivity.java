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
import com.huanmedia.videochat.media.view.MediaPlayView;
import com.huanmedia.videochat.media.view.MediaPlayView2;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MediaPlayActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    @BindView(R.id.media_vp)
    ViewPager mMediaoVP;
    @BindView(R.id.media_tv_page)
    TextView mTVPage;
    @BindView(R.id.iv_exit)
    ImageView mIVExit;

    private MediaPlayAdapter mPlayAdapter;
    private List<String> mListVedioUrl;
    private List<Fragment> mListVedioView;

    public static Intent getCallingIntent(Context context, ArrayList<String> vedios, int position) {
        Intent intent = new Intent(context, MediaPlayActivity.class);
        intent.putStringArrayListExtra("vedios", vedios);
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
        ((RelativeLayout.LayoutParams) mIVExit.getLayoutParams()).topMargin += DisplayUtil.getStatusBarHeight(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mListVedioView = new ArrayList<>();
        mListVedioUrl = getIntent().getStringArrayListExtra("vedios");
        if (mListVedioUrl == null || mListVedioUrl.size() <= 0)
            return;
        for (int i = 0; i < mListVedioUrl.size(); i++) {
            if (mListVedioUrl.get(i) == null)
                continue;
            MediaPlayView2 itemView = new MediaPlayView2();
            itemView.setVedioUrl(mListVedioUrl.get(i));
            mListVedioView.add(itemView);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        mPlayAdapter = new MediaPlayAdapter(fragmentManager, mListVedioView);
        mMediaoVP.setAdapter(mPlayAdapter);
        mMediaoVP.addOnPageChangeListener(this);
        mMediaoVP.setOffscreenPageLimit(mListVedioView.size());
        mMediaoVP.setCurrentItem(getIntent().getIntExtra("position", 0));
//        ((MediaPlayView) mListVedioView.get(getIntent().getIntExtra("position", 0))).setFirst(true);
        mTVPage.setText((getIntent().getIntExtra("position", 0) + 1) + "/" + mListVedioUrl.size());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < mListVedioView.size(); i++) {
            MediaPlayView2 itemView = (MediaPlayView2) mListVedioView.get(i);
            if (position == i) {
                itemView.startVideo();
            }
        }
        mTVPage.setText((position + 1) + "/" + mListVedioUrl.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
}


