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

    private List<ShortVideoResults> mListData;

    public VideoPtrLayout(Context context) {
        this(context, null);
    }

    public VideoPtrLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mLayoutManager = new ViewPagerLayoutManager(context);
        mPtrAdapter = new VideoPtrAdapter(context);


        mLayoutManager.setOnViewPagerListener(this);
        setLayoutManager(mLayoutManager);
        setAdapter(mPtrAdapter);
        startLoadData();
    }

    public void startLoadData() {
        mListData = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ShortVideoResults results = new ShortVideoResults();
            results.setName(i + "test");
            mListData.add(results);
        }
        mPtrAdapter.setData(mListData);
        mPtrAdapter.playVideo(0);
    }

    @Override
    public void onPageRelease(boolean isNext, int position) {
        Log.e("this", "释放位置:" + position + " 下一页:" + isNext);

    }

    @Override
    public void onPageSelected(int position, boolean isBottom) {
        Log.e("this", "选中位置:" + position + " 是否是滑动到底部:" + isBottom);
        mPtrAdapter.playVideo(position);
    }

    @Override
    public void onLayoutComplete() {

    }
}
