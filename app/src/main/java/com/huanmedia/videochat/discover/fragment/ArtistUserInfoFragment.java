package com.huanmedia.videochat.discover.fragment;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;

public class ArtistUserInfoFragment extends BaseFragment {
    public static ArtistUserInfoFragment newInstance() {
        ArtistUserInfoFragment fragment = new ArtistUserInfoFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_artist_userinfo;
    }
}
