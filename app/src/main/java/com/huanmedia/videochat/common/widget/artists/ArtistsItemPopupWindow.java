package com.huanmedia.videochat.common.widget.artists;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.BasePopupWindow;
import com.huanmedia.videochat.mvp.entity.results.ArtistsGroupShowResults;
import com.huanmedia.videochat.repository.entity.BusinessCardEntity;

import mvp.data.store.glide.GlideUtils;

public class ArtistsItemPopupWindow extends BasePopupWindow implements View.OnClickListener {
    private ImageView mIVHeard;
    private TextView mTVName;
    private TextView mTVMore;
    private TextView mTVGroupName;
    private TextView mTVAge;
    private TextView mTVHigh;
    private TextView mTVStar;
    private TextView mTVLike;
    private ArtistsGroupShowResults.Items mData;

    public ArtistsItemPopupWindow(Context context) {
        super(context,
                R.layout.layout_artists_item_popup,
                context.getResources().getDimensionPixelOffset(R.dimen.dimen_300dp),
                context.getResources().getDimensionPixelOffset(R.dimen.dimen_580dp));
    }

    @Override
    protected void initView() {
        mIVHeard = findViewById(R.id.iv_heard);
        mTVName = findViewById(R.id.tv_name);
        mTVMore = findViewById(R.id.tv_more);
        mTVGroupName = findViewById(R.id.tv_group_name);
        mTVAge = findViewById(R.id.tv_age);
        mTVHigh = findViewById(R.id.tv_high);
        mTVStar = findViewById(R.id.tv_star);
        mTVLike = findViewById(R.id.tv_like);

        LinearGradient mLinearGradient = new LinearGradient
                (0, 0, 0, mTVName.getPaint().getTextSize(),
                        Color.parseColor("#ff1659"), getContext().getResources().getColor(R.color.color_ff7eaa),
                        Shader.TileMode.CLAMP);
        mTVName.getPaint().setShader(mLinearGradient);
        mTVMore.setOnClickListener(this);


    }

    @Override
    protected void initData() {

    }

    public void setData(ArtistsGroupShowResults.Items data) {
        this.mData = data;
        GlideUtils.getInstance().loadBitmapNoAnim(getContext(), data.getFaceimgurl(), mIVHeard);

        BusinessCardEntity.BaseInfo baseInfo = mData.getInfo();
        if (baseInfo == null)
            return;
        if (baseInfo.getNickname() != null)
            mTVName.setText(baseInfo.getNickname());
        if (baseInfo.getGroupName() != null)
            mTVGroupName.setText(baseInfo.getGroupName());
        if (baseInfo.getAge() != 0)
            mTVAge.setText(baseInfo.getAge() + "");
        if (baseInfo.getHeight() != 0)
            mTVHigh.setText(baseInfo.getHeight() + "");
        if (baseInfo.getStarsign() != null)
            mTVStar.setText(baseInfo.getStarsign() + "");
        if (baseInfo.getHobby() != null) {
            String like = "";
            for (String item : baseInfo.getHobby()) {
                like += (" " + item);
            }
            mTVLike.setText(like);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == mTVMore) {
            ((BaseActivity) getContext()).getNavigator().navtoArtist((Activity) getContext(), mData.getBinduid());
        }
    }
}
