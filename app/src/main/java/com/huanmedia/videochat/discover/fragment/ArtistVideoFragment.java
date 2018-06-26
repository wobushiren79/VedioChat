package com.huanmedia.videochat.discover.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.discover.adapter.ArtistVideoAdapter;
import com.huanmedia.videochat.repository.entity.VideoEntity;

import java.util.List;

import butterknife.BindView;

public class ArtistVideoFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private List<VideoEntity> mListVideoData;
    private ArtistVideoAdapter mAdapter;

    public static ArtistVideoFragment newInstance() {
        ArtistVideoFragment fragment = new ArtistVideoFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_artist_video;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
    }

    @Override
    protected void initData() {
        super.initData();
        mAdapter=new ArtistVideoAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if (mListVideoData != null)
            mAdapter.setData(mListVideoData);
    }

    /**
     * 设置照片数据
     *
     * @param listPhotosData
     */
    public void setVideoData(List<VideoEntity> listPhotosData) {
        mListVideoData = listPhotosData;
    }
}
