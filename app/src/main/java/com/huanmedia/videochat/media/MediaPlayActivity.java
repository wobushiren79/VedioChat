package com.huanmedia.videochat.media;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.media.view.MediaPlayView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MediaPlayActivity extends BaseActivity {

    @BindView(R.id.media_vp)
    ViewPager mMediaoVP;

    private MediaPlayAdapter mPlayAdapter;
    private List<String> mListVedioUrl;
    private List<View> mListVedioView;

    public static Intent getCallingIntent(Context context, ArrayList<String> vedios) {
        Intent intent = new Intent(context, MediaPlayActivity.class);
        intent.putStringArrayListExtra("vedios", vedios);
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
    }

    @Override
    protected void initData() {
        super.initData();
        mListVedioView = new ArrayList<>();
        mListVedioUrl = getIntent().getStringArrayListExtra("vedios");
        if (mListVedioUrl == null || mListVedioUrl.size() <= 0)
            return;
        for (int i = 0; i < mListVedioUrl.size(); i++) {
            MediaPlayView itemView = new MediaPlayView(this, mListVedioUrl.get(i));
            mListVedioView.add(itemView);
        }
        mPlayAdapter = new MediaPlayAdapter(this, mListVedioView);
    }

}


