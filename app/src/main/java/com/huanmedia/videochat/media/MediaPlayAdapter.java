package com.huanmedia.videochat.media;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.applecoffee.devtools.base.adapter.BaseViewPagerAdapter;

import java.util.List;

public class MediaPlayAdapter extends FragmentPagerAdapter {

    List<Fragment> mListFragment;

    public MediaPlayAdapter(FragmentManager fm, List<Fragment> mListFragment) {
        super(fm);
        this.mListFragment = mListFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return mListFragment.get(position);
    }

    @Override
    public int getCount() {
        return mListFragment.size();
    }
}
