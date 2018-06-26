package com.huanmedia.videochat.discover.fragment;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;

public class ArtistDynamicFragment extends BaseFragment {

    public static ArtistDynamicFragment newInstance() {
        ArtistDynamicFragment fragment = new ArtistDynamicFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_artist_dynamic;
    }
}
