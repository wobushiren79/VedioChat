package com.huanmedia.videochat.discover.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.discover.adapter.ArtistPhotosAdapter;
import com.huanmedia.videochat.repository.entity.PhotosEntity;

import java.util.List;

import butterknife.BindView;

public class ArtistPhotosFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private ArtistPhotosAdapter mAdapter;
    private List<PhotosEntity> mListPhotosData;

    public static ArtistPhotosFragment newInstance() {
        ArtistPhotosFragment fragment = new ArtistPhotosFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_artist_photos;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
    }

    @Override
    protected void initData() {
        super.initData();
        mAdapter = new ArtistPhotosAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if (mListPhotosData != null)
            mAdapter.setData(mListPhotosData);
    }

    /**
     * 设置照片数据
     *
     * @param listPhotosData
     */
    public void setPhotosData(List<PhotosEntity> listPhotosData) {
        mListPhotosData = listPhotosData;
    }

}
