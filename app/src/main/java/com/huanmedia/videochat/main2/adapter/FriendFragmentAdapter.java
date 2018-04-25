package com.huanmedia.videochat.main2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Create by Administrator
 * time: 2018/2/7.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class FriendFragmentAdapter extends FragmentPagerAdapter {
    private final Fragment[] mFragments;

    public FriendFragmentAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        this.mFragments =fragments;
    }

    @Override
    public Fragment getItem(int position) {
            return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
