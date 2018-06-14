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
//        ShortVideoResults results1 = new ShortVideoResults();
//        results1.setVideourl("https://mmianvideo.oss-cn-beijing.aliyuncs.com/prod_voides_20180529_123637_43344");
        ShortVideoResults results2 = new ShortVideoResults();
        results2.setVideourl("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
        ShortVideoResults results3 = new ShortVideoResults();
        results3.setVideourl("https://mmianvideo.oss-cn-beijing.aliyuncs.com/prod_voides_20180516_174846_49568");
        ShortVideoResults results4 = new ShortVideoResults();
        results4.setVideourl("https://mmianvideo.oss-cn-beijing.aliyuncs.com/prod_voides_20180522_151128_73677");
        ShortVideoResults results5 = new ShortVideoResults();
        results5.setVideourl("https://mmianvideo.oss-cn-beijing.aliyuncs.com/dev_voides_20180518_135645_91647");
//        mListData.add(results1);
        mListData.add(results2);
        mListData.add(results3);
        mListData.add(results4);
        mListData.add(results5);
        mPtrAdapter.setData(mListData);
    }

    /**
     * 播放视频
     *
     * @param position
     */
    public void playVideo(int position) {
        mPtrAdapter.playVideo(position);
    }

    public void playVideo() {
        mPtrAdapter.playVideo(-1);
    }

    @Override
    public void onPageRelease(boolean isNext, int position) {
        Log.e("this", "释放位置:" + position + " 下一页:" + isNext);

    }

    @Override
    public void onPageSelected(int position, boolean isBottom) {
        Log.e("this", "选中位置:" + position + " 是否是滑动到底部:" + isBottom);
        if (position != mCurrentPosition) {
            mPtrAdapter.playVideo(position);
        }
        mCurrentPosition = position;


    }

    @Override
    public void onLayoutComplete() {

    }
}
