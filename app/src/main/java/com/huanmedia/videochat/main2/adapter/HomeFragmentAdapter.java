package com.huanmedia.videochat.main2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Create by Administrator
 * time: 2018/2/7.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class HomeFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;

    public HomeFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    public void addFragments(List<Fragment> fragments) {
        mFragments.addAll(fragments);
        this.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
