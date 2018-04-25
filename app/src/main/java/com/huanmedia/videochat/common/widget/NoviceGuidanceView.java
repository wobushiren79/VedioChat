package com.huanmedia.videochat.common.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseLinearLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import mvp.data.store.DataKeeper;
import mvp.data.store.glide.GlideApp;
import mvp.data.store.glide.GlideUtils;

public class NoviceGuidanceView extends BaseLinearLayout implements View.OnClickListener {

    private FrameLayout mFLLayout;
    private FrameLayout mFLCancel;
    private ImageView mIVContent;

    int[] mResList;
    int mImgPosition = 0;

    @GuidanceType
    int guidanceType;

    int[] findTypeData = {
            R.drawable.icon_novice_guidance_find_1,
            R.drawable.icon_novice_guidance_find_2
    };

    int[] friendTypeData = {
            R.drawable.icon_novice_guidance_friend_1
    };

    int[] myTypeData = {
            R.drawable.icon_novice_guidance_my_1
    };

    int[] matchTypeData = {
            R.drawable.icon_novice_guidance_match_1,
            R.drawable.icon_novice_guidance_match_2,
            R.drawable.icon_novice_guidance_match_3
    };

    @Retention(RetentionPolicy.SOURCE)
    public @interface GuidanceType {
        int FIND = 1, FRIEND = 2, MY = 3, MATCH = 4;
    }

    public NoviceGuidanceView(Context context) {
        super(context);
    }

    public NoviceGuidanceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_novice_guidance_view;
    }


    @Override
    protected void initView(View baseView) {
        mFLCancel = getView(R.id.fl_cancel);
        mIVContent = getView(R.id.iv_content);
        mFLLayout = getView(R.id.fl_layout);

        mFLCancel.setOnClickListener(this);
        mIVContent.setOnClickListener(this);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mFLLayout.setLayoutParams(layoutParams);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View v) {
        if (v == mFLCancel) {
            setNoFirst(guidanceType);
        } else if (v == mIVContent) {
            contentDeal();
        }
    }

    /**
     * 设置数据
     *
     * @param guidanceType
     */
    public void setShowData(@GuidanceType int guidanceType) {
        this.guidanceType = guidanceType;
        String keyData = "";
        switch (guidanceType) {
            case GuidanceType.FIND:
                keyData = "NoviceGuidanceFind";
                this.mResList = findTypeData;
                break;
            case GuidanceType.FRIEND:
                keyData = "NoviceGuidanceFriend";
                this.mResList = friendTypeData;
                break;
            case GuidanceType.MY:
                keyData = "NoviceGuidanceMy";
                this.mResList = myTypeData;
                break;
            case GuidanceType.MATCH:
                keyData = "NoviceGuidanceMatch";
                this.mResList = matchTypeData;
                break;
        }
        boolean isFirst = new DataKeeper(getContext(), DataKeeper.DEFULTFILE).get(keyData, true);
        if (isFirst) {
            this.setVisibility(VISIBLE);
        } else {
            this.setVisibility(INVISIBLE);
        }

        if (this.mResList.length > 0) {
            if (this.mResList.length == 1) {
                mFLCancel.setVisibility(GONE);
            } else {
                mFLCancel.setVisibility(VISIBLE);
            }
            GlideApp.with(getContext()).asBitmap().load(mResList[0]).into(mIVContent);
            mImgPosition = 0;
        }
    }

    /**
     * 设置不是第一次
     */
    public void setNoFirst(@GuidanceType int guidanceType) {
        String keyData = "";
        switch (guidanceType) {
            case GuidanceType.FIND:
                keyData = "NoviceGuidanceFind";
                break;
            case GuidanceType.FRIEND:
                keyData = "NoviceGuidanceFriend";
                break;
            case GuidanceType.MY:
                keyData = "NoviceGuidanceMy";
                break;
            case GuidanceType.MATCH:
                keyData = "NoviceGuidanceMatch";
                break;
        }
        new DataKeeper(getContext(), DataKeeper.DEFULTFILE).put(keyData, false);
        this.setVisibility(GONE);
    }

    /**
     * 图片点击处理
     */
    private void contentDeal() {
        if (mResList == null || mResList.length == 0) {
            this.setVisibility(GONE);
            return;
        }
        mImgPosition++;
        if (mImgPosition < mResList.length)
            GlideApp.with(getContext()).asBitmap().load(mResList[mImgPosition]).into(mIVContent);
//            GlideUtils.getInstance().loadBitmapNoAnim(getContext(), mResList[mImgPosition], mIVContent);
        else
            setNoFirst(guidanceType);
    }
}
