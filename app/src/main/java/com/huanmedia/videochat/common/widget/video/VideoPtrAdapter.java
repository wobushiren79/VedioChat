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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.huanmedia.ilibray.utils.DevUtils;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.widget.dialog.BusinessCardDialog;
import com.huanmedia.videochat.discover.BusinessCardFragment;
import com.huanmedia.videochat.mvp.entity.results.ShortVideoResults;
import com.huanmedia.videochat.mvp.presenter.video.IShortVideoPraisePresenter;
import com.huanmedia.videochat.mvp.presenter.video.ShortVideoPraisePresenterImpl;
import com.huanmedia.videochat.mvp.view.video.IShortVideoPraiseView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shuyu.gsyvideoplayer.render.effect.BlackAndWhiteEffect;
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.math.BigDecimal;
import java.util.List;

import mvp.data.store.glide.GlideApp;
import mvp.data.store.glide.GlideUtils;

public class VideoPtrAdapter extends BaseRCAdapter<ShortVideoResults> implements IShortVideoPraiseView {


    private IShortVideoPraisePresenter mShortVideoPraisePresenter;
    private int mPlayPosition;

    public VideoPtrAdapter(Context context) {
        super(context, R.layout.layout_video_ptr_item);
        mPlayPosition = 0;
        mShortVideoPraisePresenter = new ShortVideoPraisePresenterImpl(this);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, ShortVideoResults shortVideoResults, int i) {
        LinearLayout llUserInfo = baseViewHolder.getView(R.id.ll_user_info);
        LinearLayout llUserInfoTag = baseViewHolder.getView(R.id.ll_user_tag);
        TextView tvUserLocation = baseViewHolder.getView(R.id.tv_user_location);
        TextView tvUserName = baseViewHolder.getView(R.id.tv_name);
        ImageView ivLove = baseViewHolder.getView(R.id.iv_love);
        RoundedImageView ivUserIcon = baseViewHolder.getView(R.id.iv_user_icon);
        TextView tvLoveNum = baseViewHolder.getView(R.id.iv_love_num);
        int praiseNum = mDatas.get(i).getPraise();

        if (shortVideoResults.getAccount_id() == 0) {
            //设置用户姓名
            tvUserName.setText("萌面官方");
            //设置用户地址
            tvUserLocation.setText("萌面官方研发团队");
            //设置头像
            Glide.with(mContext).load(R.mipmap.icon_logo).into(ivUserIcon);
            ivUserIcon.setOnClickListener(null);
        } else {
            //设置用户姓名
            if (shortVideoResults.getAccount_nickname() != null) {
                tvUserName.setText(shortVideoResults.getAccount_nickname());
            } else {
                tvUserName.setText("未知");
            }
            //设置用户地址
            tvUserLocation.setText("四川省成都市锦江区");
            //设置头像
            GlideApp.with(mContext).load(shortVideoResults.getAccount_face()).error(R.drawable.icon_headportrait).into(ivUserIcon);
            ivUserIcon.setOnClickListener(view -> {
                if (shortVideoResults.getAccount_isstarauth() == 1) {
                    ((BaseActivity) mContext).getNavigator().navDiscoverInfo((Activity) mContext, shortVideoResults.getAccount_id(), "0", BusinessCardFragment.ShowType.ReadMan);
                } else {
                    BusinessCardDialog dialog = new BusinessCardDialog(getContext());
                    dialog.setUid(shortVideoResults.getAccount_id());
                    dialog.setDistance("0");
                    dialog.show();
                }
            });
        }

        //设置点赞
        if (shortVideoResults.getIspraise() == 0) {
            Glide.with(mContext).load(R.drawable.icon_love_unclick).into(ivLove);
            ivLove.setOnClickListener(view -> {
                mShortVideoPraisePresenter.shortVideoPraise(shortVideoResults.getId());
                mDatas.get(i).setIspraise(1);
                mDatas.get(i).setPraise((praiseNum + 1));
                startLoveAnim(ivLove);
                tvLoveNum.setText(loveNumHandler(praiseNum + 1));
            });
        } else {
            Glide.with(mContext).load(R.drawable.icon_love_onclick).into(ivLove);
            ivLove.setOnClickListener(view -> {
                startLoveAnim(ivLove);
            });
        }

        tvLoveNum.setText(loveNumHandler(praiseNum));

        //设置用户标签
        llUserInfoTag.removeAllViews();
        if (shortVideoResults.getTags() != null && shortVideoResults.getTags().length() != 0) {
            llUserInfoTag.setVisibility(View.VISIBLE);
            List<String> listTag = DevUtils.StrToList(shortVideoResults.getTags(), ",");
            for (String itemTag : listTag) {
                createTag(itemTag, llUserInfoTag);
            }
        } else {
            llUserInfoTag.setVisibility(View.GONE);
        }

        //设置播放视频
        String videoUrl = shortVideoResults.getUrl();
        //设置视频缩略图
        ImageView ivThumb = new ImageView(mContext);
        if (shortVideoResults.getVideothumb() != null && shortVideoResults.getVideothumb().length() > 0) {
            Glide
                    .with(mContext)
                    .load(shortVideoResults.getVideothumb())
                    .into(ivThumb);

        } else {
            Glide
                    .with(mContext)
                    .setDefaultRequestOptions(
                            new RequestOptions()
                                    .frame(1000000))
                    .load(videoUrl)
                    .into(ivThumb);
        }

        VideoPtrItemPlayer videoPlayer = baseViewHolder.getView(R.id.detail_player);
        videoPlayer.setUp(videoUrl, true, "");
        videoPlayer.setLooping(true);
        videoPlayer.setThumbImageView(ivThumb);
        if (i == mPlayPosition) {
            videoPlayer.startPlayLogic();
        }
    }


    /**
     * 处理点赞数 上W则显示W
     *
     * @param loveNum
     * @return
     */
    private String loveNumHandler(int loveNum) {
        if (loveNum > 9999) {
            float temp = (float) loveNum / 10000f;
            int scale = 1;//设置位数
            int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
            BigDecimal bd = new BigDecimal((double) temp);
            bd = bd.setScale(scale, roundingMode);
            temp = bd.floatValue();
            return temp + "w";
        } else {
            return loveNum + "";
        }
    }

    /**
     * 点赞动画
     *
     * @param view
     */
    private void startLoveAnim(ImageView view) {
        Glide.with(mContext).load(R.drawable.icon_love_onclick).into(view);
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
     * @param position
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

    @Override
    public void shortVideoPraiseSuccess() {

    }

    @Override
    public void shortVideoPraiseFail(String msg) {

    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastLongInCenter(mContext, toast);
    }

    public void setPlayPosition(int playPosition) {
        this.mPlayPosition = playPosition;
    }
}
