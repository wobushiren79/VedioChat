package com.huanmedia.videochat.common.widget.artists;

import android.content.Context;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BasePopupWindow;

public class ArtistsItemPopupWindow extends BasePopupWindow {

    public ArtistsItemPopupWindow(Context context) {
        super(context,
                R.layout.layout_artists_item_popup,
                context.getResources().getDimensionPixelOffset(R.dimen.dimen_200dp),
                context.getResources().getDimensionPixelOffset(R.dimen.dimen_200dp));
    }

    @Override
    protected void initView() {
        
    }

    @Override
    protected void initData() {

    }
}
