package com.huanmedia.videochat.common.widget.video;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.huanmedia.videochat.common.widget.ptr.PtrLayout;
import com.huanmedia.videochat.mvp.entity.results.ShortVideoResults;

import java.util.ArrayList;
import java.util.List;

public class VideoPtrLayout extends PtrLayout implements ViewPagerLayoutManager.OnViewPagerListener {

    private ViewPagerLayoutManager mLayoutManager;
    private VideoPtrAdapter mPtrAdapter;
    private int mCurrentPosition = 0;
    private PageCallBack mCallBack;


    public VideoPtrLayout(Context context) {
        this(context, null);
    }

    public VideoPtrLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mLayoutManager = new ViewPagerLayoutManager(context);
        mPtrAdapter = new VideoPtrAdapter(context, this);


        mLayoutManager.setOnViewPagerListener(this);
        setLayoutManager(mLayoutManager);
        setAdapter(mPtrAdapter);
    }

    public void setPageCallBack(PageCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public void notifyDataSetChanged() {
        mPtrAdapter.notifyDataSetChanged();
    }


    /**
     * 设置当前播放
     *
     * @param mCurrentPosition
     */
    public void setPlayPosition(int mCurrentPosition) {
        this.mCurrentPosition = mCurrentPosition;
        mPtrAdapter.setPlayPosition(mCurrentPosition);
    }

    @Override
    public void onPageRelease(boolean isNext, int position) {

    }

    @Override
    public void onPageSelected(int position, boolean isBottom) {
        playPosition(position, isBottom);
    }

    /**
     * 播放第几个视频
     *
     * @param position
     * @param isBottom
     */
    public void playPosition(int position, boolean isBottom) {
        if (position != mCurrentPosition) {
            mPtrAdapter.playVideo(position);
        }
        mCurrentPosition = position;

        if (mCallBack != null) {
            mCallBack.onPageSelected(position, isBottom);
            if (mPtrAdapter.getData().size() - position <= 5) {
                mCallBack.almostBottom();
            }
        }
    }


    @Override
    public void onLayoutComplete() {

    }


    public interface PageCallBack {
        /**
         * 翻页
         *
         * @param position
         * @param isBottom
         */
        void onPageSelected(int position, boolean isBottom);

        /**
         * 快到底部
         */
        void almostBottom();
    }
}
