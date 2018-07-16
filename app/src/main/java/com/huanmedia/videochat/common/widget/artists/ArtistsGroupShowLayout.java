package com.huanmedia.videochat.common.widget.artists;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.applecoffee.devtools.base.layout.BaseFrameLayout;
import com.huanmedia.videochat.R;

import mvp.data.store.glide.GlideUtils;

public class ArtistsGroupShowLayout extends BaseFrameLayout {

    private ImageView mIVBackGround;

    public ArtistsGroupShowLayout(Context context) {
        this(context, null);
    }

    public ArtistsGroupShowLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView() {
        mIVBackGround = findViewById(R.id.iv_background);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.layout_artists_group_show;
    }

    /**
     * 设置背景图片
     * @param url
     */
    public void setBackGround(String url) {
        GlideUtils.getInstance().loadBitmapNoAnim(getContext(), url, mIVBackGround);
    }
}
