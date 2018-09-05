package com.huanmedia.videochat.common.widget.artists;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applecoffee.devtools.base.layout.BaseFrameLayout;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BasePopupWindow;
import com.huanmedia.videochat.mvp.entity.results.ArtistsGroupShowResults;

import java.util.ArrayList;
import java.util.List;

import mvp.data.store.glide.GlideApp;
import mvp.data.store.glide.GlideUtils;

public class ArtistsGroupShowLayout extends BaseFrameLayout {

    private RelativeLayout mRLLayout;
    private ImageView mIVBackGround;
    private TextView mTVMask;

    private ArtistsGroupShowResults mBaseData;

    private List<ImageView> mListArtistsImage;

    public ArtistsGroupShowLayout(Context context) {
        this(context, null);
    }

    public ArtistsGroupShowLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView() {
        mRLLayout = findViewById(R.id.rl_layout);
        mIVBackGround = findViewById(R.id.iv_background);
        mTVMask = findViewById(R.id.tv_mask);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected int setLayoutId() {
        return R.layout.layout_artists_group_show;
    }

    /**
     * 设置数据
     *
     * @param data
     */
    public void setBaseData(ArtistsGroupShowResults data) {
        mBaseData = data;
    }

    /**
     * 设置背景图片
     *
     * @param url
     */
    public void setBackGround(String url) {
        GlideApp.with(getContext()).load(url).into(mIVBackGround);
    }

    /**
     * 设置红人列表
     *
     * @param listArtists
     */
    public void setArtistsList(List<ArtistsGroupShowResults.Items> listArtists) {
        if (mListArtistsImage != null) {
            for (ImageView itemView : mListArtistsImage) {
                mRLLayout.removeView(itemView);
            }
            mListArtistsImage.clear();
        } else
            mListArtistsImage = new ArrayList<>();

        for (int i = 0; i < listArtists.size(); i++) {
            addArtistsItem(listArtists.get(i));
        }
    }

    private void addArtistsItem(ArtistsGroupShowResults.Items itemData) {
        if (itemData == null)
            return;
        ImageView itemArtists = new ImageView(getContext());
        //设置图片大小
        setArtistsItemSizeAndLocation(itemData, itemArtists);
        //设置图层级别
        if (itemData.getImgz() != 0)
            itemArtists.setY((float) itemData.getImgz());
        //设置图片内容
        if (itemData.getImgurl() != null)
            GlideUtils.getInstance().loadBitmapNoAnim(getContext(), itemData.getImgurl(), itemArtists);
        //设置图片点击
        itemArtists.setOnClickListener(view -> {
            mTVMask.setVisibility(VISIBLE);
            for (ImageView itemView : mListArtistsImage) {
                if (itemView == itemArtists) {
                    itemView.setVisibility(VISIBLE);
                } else {
                    itemView.setVisibility(GONE);
                }
            }
            ArtistsItemPopupWindow popupWindow = new ArtistsItemPopupWindow(getContext());
            popupWindow.setData(itemData);
            BasePopupWindow.LayoutGravity layoutParams = new BasePopupWindow.LayoutGravity(BasePopupWindow.LayoutGravity.CENTER_VERT);
            if ((itemArtists.getX() + (itemArtists.getWidth() / 2f)) < mIVBackGround.getWidth() / 2f) {
                layoutParams.setHoriGravity(BasePopupWindow.LayoutGravity.TO_RIGHT);
            } else {
                layoutParams.setHoriGravity(BasePopupWindow.LayoutGravity.TO_LEFT);
            }
            layoutParams.setVertGravity(BasePopupWindow.LayoutGravity.CENTER_VERT);
            popupWindow.show(itemArtists, layoutParams, 0, 0);
            popupWindow.getPopupWindow().setOnDismissListener(() -> {
                mTVMask.setVisibility(GONE);
                for (ImageView itemView : mListArtistsImage) {
                    itemView.setVisibility(VISIBLE);
                }
            });
        });
        mRLLayout.addView(itemArtists);
        mListArtistsImage.add(itemArtists);
    }

    /**
     * 设置控件大小和位置
     *
     * @param itemData
     * @param view
     */
    private void setArtistsItemSizeAndLocation(ArtistsGroupShowResults.Items itemData, View view) {
        if (mBaseData == null || mBaseData.getBase() == null || itemData == null || view == null)
            return;
        if (mBaseData.getBase().getImgheight() == 0 || itemData.getImgheight() == 0)
            return;
        float imageRatio = (float) itemData.getImgheight() / (float) mBaseData.getBase().getImgheight();
        int viewH = (int) (imageRatio * (float) mIVBackGround.getHeight());

        float viewRatio = (float) itemData.getImgwidth() / (float) itemData.getImgheight();
        int viewW = (int) (viewRatio * viewH);

        float screenHRatio = (float) itemData.getImgy() / (float) mBaseData.getBase().getImgheight();
        int top = (int) (screenHRatio * (float) mIVBackGround.getHeight());
        float screenWRatio = (float) itemData.getImgx() / (float) mBaseData.getBase().getImgwidth();
        int left = (int) (screenWRatio * (float) mIVBackGround.getWidth());

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                (viewW, viewH);
        layoutParams.setMargins(left, top, 0, 0);
        view.setLayoutParams(layoutParams);
    }

}
