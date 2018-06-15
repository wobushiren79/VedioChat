package com.huanmedia.videochat.common.widget.video;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applecoffee.devtools.base.adapter.BaseRCAdapter;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.discover.BusinessCardFragment;
import com.huanmedia.videochat.mvp.entity.results.ShortVideoResults;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shuyu.gsyvideoplayer.render.effect.BlackAndWhiteEffect;
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class VideoPtrAdapter extends BaseRCAdapter<ShortVideoResults> {


    private int mPlayPosition;

    public VideoPtrAdapter(Context context) {
        super(context, R.layout.layout_video_ptr_item);
        mPlayPosition = -1;
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, ShortVideoResults shortVideoResults, int i) {
        LinearLayout llUserInfo = baseViewHolder.getView(R.id.ll_user_info_location);
        LinearLayout llUserInfoTag = baseViewHolder.getView(R.id.ll_user_tag);
        TextView tvUserLocation = baseViewHolder.getView(R.id.tv_user_location);
        TextView tvUserName = baseViewHolder.getView(R.id.tv_name);
        ImageView ivLove = baseViewHolder.getView(R.id.iv_love);
        RoundedImageView ivUserIcon = baseViewHolder.getView(R.id.iv_user_icon);

        ivLove.setOnClickListener(view -> {
            startLoveAnim(ivLove);
        });
        ivUserIcon.setOnClickListener(view -> {
            ((BaseActivity)mContext).getNavigator().navDiscoverInfo((Activity) mContext,1,"123", BusinessCardFragment.ShowType.ReadMan);
        });

        tvUserName.setText("小玉娘");
        tvUserLocation.setText("四川省成都市锦江区");

        llUserInfoTag.removeAllViews();
        createTag("高颜值", llUserInfoTag);
        createTag("高颜值", llUserInfoTag);

        VideoPtrItemPlayer videoPlayer = baseViewHolder.getView(R.id.detail_player);
        String videoUrl = shortVideoResults.getVideourl();
        videoPlayer.setUp(videoUrl, true, shortVideoResults.getName());
        if (i == mPlayPosition) {
            videoPlayer.startPlayLogic();
        }
    }


    private void startLoveAnim(ImageView view) {
        view.setImageResource(R.drawable.icon_love_onclick);
        ScaleAnimation scaleAnimation = new ScaleAnimation
                (0, 1, 0, 1,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setInterpolator(new BounceInterpolator());
        view.startAnimation(scaleAnimation);
    }

    /**
     * 创建标签
     *
     * @param tag
     * @param llTag
     */
    private void createTag(String tag, LinearLayout llTag) {
        TextView tagTV = new TextView(mContext);
        tagTV.setText(tag);
        tagTV.setTextColor(mContext.getResources().getColor(R.color.white));
        tagTV.setBackgroundResource(R.drawable.base_bg_black_transparent_round);
        int dp8 = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_8dp);
        tagTV.setPadding(dp8 * 2, dp8, dp8 * 2, dp8);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, dp8 * 2, 0);
        tagTV.setLayoutParams(layoutParams);
        tagTV.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, dp8 * 4f);
        llTag.addView(tagTV);
    }

    /**
     * 播放视频
     *
     * @param position -1 时默认播放原视频
     */
    public void playVideo(int position) {
        if ((position + 1) > mDatas.size())
            return;
        else if (position < 0) {
            if (mDatas != null && mDatas.size() > 0) {
                if (mPlayPosition != -1) {

                } else {
                    mPlayPosition = 0;
                }
                notifyDataSetChanged();
            } else {

            }
        } else {
            mPlayPosition = position;
            notifyDataSetChanged();
        }
    }
}
