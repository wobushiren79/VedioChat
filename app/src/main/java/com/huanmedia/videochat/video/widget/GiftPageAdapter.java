package com.huanmedia.videochat.video.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

public  class  GiftPageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mData;

    public GiftPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mData = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mData.get(position);
    }


    @Override
    public int getCount() {
        return mData.size();
    }
}