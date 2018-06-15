package com.huanmedia.videochat.media.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.huanmedia.videochat.common.widget.video.ViewPagerLayoutManager;
import com.huanmedia.videochat.repository.entity.VideoEntity;

import java.util.List;

public class MediaPlayListLayout extends RecyclerView implements ViewPagerLayoutManager.OnViewPagerListener {

    private ViewPagerLayoutManager mLayoutManager;
    private MediaPlayListAdapter mPtrAdapter;
    private CallBack mCallBack;

    private int mCurrentPosition = 0;

    public MediaPlayListLayout(Context context) {
        this(context, null);
    }

    public MediaPlayListLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mLayoutManager = new ViewPagerLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mPtrAdapter = new MediaPlayListAdapter(context);


        mLayoutManager.setOnViewPagerListener(this);
        setLayoutManager(mLayoutManager);
        setAdapter(mPtrAdapter);
    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    public void playVideo(int position) {
        mCurrentPosition = position;
        mPtrAdapter.playVideo(position);
    }

    public void setListData(List<VideoEntity> listData) {
        mPtrAdapter.setData(listData);
    }

    @Override
    public void onPageRelease(boolean isNext, int position) {

    }

    @Override
    public void onPageSelected(int position, boolean isBottom) {
        if (position != mCurrentPosition) {
            mPtrAdapter.playVideo(position);
        }
        mCurrentPosition = position;
        if (mCallBack != null)
            mCallBack.onPageSelected(position);
    }

    @Override
    public void onLayoutComplete() {

    }

    public interface CallBack {
        void onPageSelected(int position);
    }
}
