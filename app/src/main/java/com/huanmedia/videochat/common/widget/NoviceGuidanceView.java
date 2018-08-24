package com.huanmedia.videochat.common.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseLinearLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import mvp.data.store.DataKeeper;
import mvp.data.store.glide.GlideApp;
import mvp.data.store.glide.GlideUtils;

public class NoviceGuidanceView extends BaseLinearLayout implements View.OnClickListener, NoviceGuidanceItemView.CallBack {

    private FrameLayout mFLLayout;
    private FrameLayout mFLCancel;
    private NoviceGuidanceItemView mViewContent;
    private TextView mTVCancel;

    int[] mResList;
    int mImgPosition = 0;

    @GuidanceType
    int guidanceType;

    /**
     * 发现 引导
     */
    int[] findTypeData = {
//            R.drawable.icon_novice_guidance_find_1,
//            R.drawable.icon_novice_guidance_find_2
            R.drawable.icon_novice_guidance_find_1_new,
            R.drawable.icon_novice_guidance_find_2_new
    };
    /**
     * 视频 引导
     */
    int[] videoTypeData = {
            R.drawable.icon_novice_guidance_video_1,
            R.drawable.icon_novice_guidance_video_2
    };

    /**
     * 萌友 引导
     */
    int[] friendTypeData = {
            R.drawable.icon_novice_guidance_friend_1
    };

    /**
     * 我 引导
     */
    int[] myTypeData = {
            R.drawable.icon_novice_guidance_my_1,
            R.drawable.icon_novice_guidance_my_2
    };

    /**
     * 匹配模式 引导
     */
    int[] matchTypeData = {
            R.drawable.icon_novice_guidance_match_1,
            R.drawable.icon_novice_guidance_match_2,
            R.drawable.icon_novice_guidance_match_3
    };

    /**
     * 红人类型 引导
     */
    int[] readTypeData = {
            R.drawable.icon_novice_guidance_redman_1,
            R.drawable.icon_novice_guidance_redman_2,
            R.drawable.icon_novice_guidance_redman_3,
            R.drawable.icon_novice_guidance_redman_4
    };

    @Override
    public void Next() {
        contentDeal();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface GuidanceType {
        int FIND = 1, FRIEND = 2, MY = 3, MATCH = 4, READMAN = 5, VIDEO = 6;
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
        mFLLayout = getView(R.id.fl_layout);
        mTVCancel = getView(R.id.tv_cancel);
        mViewContent = getView(R.id.view_content);

        mFLCancel.setOnClickListener(this);
        mViewContent.setCallBack(this);
        mTVCancel.setOnClickListener(this);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mFLLayout.setLayoutParams(layoutParams);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View v) {
        if (v == mFLCancel || v == mTVCancel) {
            setNoFirst(guidanceType);
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
            case GuidanceType.READMAN:
                keyData = "NoviceGuidanceReadMan";
                this.mResList = readTypeData;
                break;
            case GuidanceType.VIDEO:
                keyData = "NoviceGuidanceVideo";
                this.mResList = videoTypeData;
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
                mTVCancel.setVisibility(GONE);
//                mFLCancel.setVisibility(GONE);
            } else {
                mTVCancel.setVisibility(VISIBLE);
//                mFLCancel.setVisibility(VISIBLE);
            }
            if (guidanceType == GuidanceType.FIND)
                mViewContent.setData(mResList[0], NoviceGuidanceItemView.GuidanceType.Read);
            else
                mViewContent.setData(mResList[0], NoviceGuidanceItemView.GuidanceType.Normal);
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
            case GuidanceType.READMAN:
                keyData = "NoviceGuidanceReadMan";
                break;
            case GuidanceType.VIDEO:
                keyData = "NoviceGuidanceVideo";
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
            if (mResList[mImgPosition] == R.drawable.icon_novice_guidance_find_2_new) {
                mViewContent.setData(mResList[mImgPosition], NoviceGuidanceItemView.GuidanceType.Appointment);
            } else {
                mViewContent.setData(mResList[mImgPosition], NoviceGuidanceItemView.GuidanceType.Normal);
            }
//            GlideUtils.getInstance().loadBitmapNoAnim(getContext(), mResList[mImgPosition], mIVContent);
        else
            setNoFirst(guidanceType);
    }
}
