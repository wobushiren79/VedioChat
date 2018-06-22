package com.huanmedia.videochat.common.widget.video;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applecoffee.devtools.base.adapter.BaseRCAdapter;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.huanmedia.ilibray.utils.DevUtils;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.utils.UMengUtils;
import com.huanmedia.videochat.common.utils.VideoChatUtils;
import com.huanmedia.videochat.common.widget.dialog.BusinessCardDialog;
import com.huanmedia.videochat.discover.BusinessCardFragment;
import com.huanmedia.videochat.mvp.entity.results.ShortVideoResults;
import com.huanmedia.videochat.mvp.presenter.video.IShortVideoPraisePresenter;
import com.huanmedia.videochat.mvp.presenter.video.ShortVideoPraisePresenterImpl;
import com.huanmedia.videochat.mvp.view.video.IShortVideoPraiseView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYMediaPlayerListener;

import java.math.BigDecimal;
import java.util.List;

import mvp.data.store.glide.GlideApp;

public class VideoPtrAdapter extends BaseRCAdapter<ShortVideoResults> implements IShortVideoPraiseView {


    private IShortVideoPraisePresenter mShortVideoPraisePresenter;
    private VideoPtrLayout mPtrLayout;
    private int mPlayPosition;

    public VideoPtrAdapter(Context context, VideoPtrLayout mPtrLayout) {
        super(context, R.layout.layout_video_ptr_item);
        this.mPtrLayout = mPtrLayout;
        mPlayPosition = 0;
        mShortVideoPraisePresenter = new ShortVideoPraisePresenterImpl(this);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, ShortVideoResults shortVideoResults, int i) {
        RelativeLayout rlAnimLove = baseViewHolder.getView(R.id.rl_love);
        LinearLayout llLove = baseViewHolder.getView(R.id.ll_love);
        LinearLayout llUserInfo = baseViewHolder.getView(R.id.ll_user_info);
        LinearLayout llUserInfoTag = baseViewHolder.getView(R.id.ll_user_tag);
        LinearLayout llAppointment = baseViewHolder.getView(R.id.ll_appointment);
        LinearLayout llCall = baseViewHolder.getView(R.id.ll_call);
        TextView tvUserLocation = baseViewHolder.getView(R.id.tv_user_location);
        TextView tvUserName = baseViewHolder.getView(R.id.tv_name);
        TextView tvDesc = baseViewHolder.getView(R.id.tv_desc);
        ImageView ivLove = baseViewHolder.getView(R.id.iv_love);
        RoundedImageView ivUserIcon = baseViewHolder.getView(R.id.iv_user_icon);
        TextView tvLoveNum = baseViewHolder.getView(R.id.iv_love_num);

        rlAnimLove.removeAllViews();

        int accountId = shortVideoResults.getAccount_id();
        int accountStarCoin = shortVideoResults.getAccount_starcoin();
        int accountOnlineStatus = shortVideoResults.getAccount_onlinestatus();
        int praiseNum = shortVideoResults.getPraise();
        int isReadMan = shortVideoResults.getAccount_isstarauth();

        if (shortVideoResults.getAccount_id() == 0) {
            //设置头像
            Glide.with(mContext).load(R.mipmap.icon_logo).into(ivUserIcon);
            ivUserIcon.setOnClickListener(null);
        } else {
            //设置头像
            GlideApp.with(mContext)
                    .load(shortVideoResults.getAccount_face())
                    .placeholder(R.drawable.icon_headportrait)
                    .error(R.drawable.icon_headportrait)
                    .into(ivUserIcon);
            ivUserIcon.setOnClickListener(view -> {
                if (shortVideoResults.getAccount_isstarauth() == 1) {
                    ((BaseActivity) mContext).getNavigator().navDiscoverInfo((Activity) mContext, accountId, "0", BusinessCardFragment.ShowType.ReadMan);
                } else {
                    BusinessCardDialog dialog = new BusinessCardDialog(getContext());
                    dialog.setUid(shortVideoResults.getAccount_id());
                    dialog.setDistance("0");
                    dialog.show();
                }
            });
        }

        //设置视频描述
        if (shortVideoResults.getDescribe() == null || shortVideoResults.getDescribe().length() == 0) {
            tvDesc.setVisibility(View.GONE);
            tvDesc.setText("");
        } else {
            tvDesc.setVisibility(View.VISIBLE);
            tvDesc.setText(shortVideoResults.getDescribe());
        }
        //设置用户姓名
        if (shortVideoResults.getAccount_nickname() != null
                && shortVideoResults.getAccount_nickname().length() > 0) {
            tvUserName.setText(shortVideoResults.getAccount_nickname());
        } else {
            tvUserName.setText("萌友视频");
        }
        //设置用户地址
        tvUserLocation.setText("萌面官方研发团队");
        //设置点赞
        if (shortVideoResults.getIspraise() == 0) {
            Glide.with(mContext).load(R.drawable.icon_love_unclick).into(ivLove);
            llLove.setOnClickListener(view -> {
                mShortVideoPraisePresenter.shortVideoPraise(shortVideoResults.getId());
                mDatas.get(i).setIspraise(1);
                mDatas.get(i).setPraise((praiseNum + 1));
                tvLoveNum.setText(loveNumHandler(praiseNum + 1));
                Glide.with(mContext).load(R.drawable.icon_love_onclick).into(ivLove);
                startLoveAnim(ivLove);
            });
        } else {
            Glide.with(mContext).load(R.drawable.icon_love_onclick).into(ivLove);
            llLove.setOnClickListener(view -> {
                Glide.with(mContext).load(R.drawable.icon_love_onclick).into(ivLove);
                startLoveAnim(ivLove);
            });
        }
        tvLoveNum.setText(loveNumHandler(praiseNum));
        //预约按钮和发起视频
        if (isReadMan == 0) {
            llAppointment.setVisibility(View.GONE);
            llCall.setVisibility(View.GONE);
        } else {
            llAppointment.setVisibility(View.VISIBLE);
            llCall.setVisibility(View.VISIBLE);
        }
        llAppointment.setOnClickListener(view -> {
            ((BaseActivity) mContext).getNavigator().navtoAppointment((Activity) mContext, accountId);
        });
        llCall.setOnClickListener(view -> {
            VideoChatUtils.CheckCallVideo(
                    (Activity) mContext,
                    ((BaseActivity) mContext).getNavigator(),
                    true,
                    accountStarCoin,
                    1,
                    accountOnlineStatus,
                    accountId);
        });

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
        videoPlayer.getStartBT().setVisibility(View.GONE);
        videoPlayer.setUp(videoUrl, true, "");
        videoPlayer.setLooping(true);
        videoPlayer.setThumbImageView(ivThumb);
        videoPlayer.setCallBack(url -> {
            //视频播放失败自动
            if (i + 1 < mDatas.size()) {
                mPtrLayout.getRecyclerView().scrollToPosition(i + 1);
                boolean isBottom = false;
                if (i + 1 == mDatas.size() - 1) {
                    isBottom = true;
                }
                mPtrLayout.playPosition(i + 1, isBottom);
            }
        });
        videoPlayer.setOnVideoClickListener(new EmptyVideoPlayer.OnVideoClickListener() {
            @Override
            public void onDoubleTap(MotionEvent e) {
                if (shortVideoResults.getIspraise() == 0) {
                    mShortVideoPraisePresenter.shortVideoPraise(shortVideoResults.getId());
                    mDatas.get(i).setIspraise(1);
                    mDatas.get(i).setPraise((praiseNum + 1));
                    tvLoveNum.setText(loveNumHandler(praiseNum + 1));
                    Glide.with(mContext).load(R.drawable.icon_love_onclick).into(ivLove);
                }
                startDoubleLoveAnim(rlAnimLove, e);
            }

            @Override
            public void onSingleTap(MotionEvent e) {

            }
        });
        if (i == mPlayPosition) {
            videoPlayer.startPlayLogic();
            UMengUtils.ShortVideoPlay(mContext, shortVideoResults.getId());
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
        ScaleAnimation scaleAnimation = new ScaleAnimation
                (0, 1, 0, 1,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setInterpolator(new BounceInterpolator());
        view.startAnimation(scaleAnimation);
    }

    private void startDoubleLoveAnim(RelativeLayout rlLayout, MotionEvent e) {
        int loveSize = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_200dp);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(loveSize, loveSize);
        layoutParams.setMargins(
                (int) e.getRawX() - loveSize,
                (int) e.getRawY() - loveSize,
                0,
                0);
        ImageView ivLove = new ImageView(mContext);
        Glide.with(mContext).load(R.drawable.icon_love_onclick).into(ivLove);
        ivLove.setLayoutParams(layoutParams);
        rlLayout.addView(ivLove);
        AnimationSet animationSet = new AnimationSet(false);

        ScaleAnimation scaleAnimation = new ScaleAnimation
                (0, 1, 0, 1,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setInterpolator(new BounceInterpolator());

        TranslateAnimation translateAnimation = new TranslateAnimation
                (Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, -0.5f);
        translateAnimation.setDuration(500);
        translateAnimation.setStartOffset(500);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(500);
        alphaAnimation.setStartOffset(500);

        ScaleAnimation scaleAnimation2 = new ScaleAnimation
                (1, 1.3f, 1, 1.3f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation2.setDuration(500);
        scaleAnimation2.setStartOffset(500);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new android.os.Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        rlLayout.removeView(ivLove);
                    }
                });

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animationSet.setDuration(1000);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(scaleAnimation2);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        ivLove.startAnimation(animationSet);

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
        tagTV.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, dp8 * 3.5f);
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
