package com.huanmedia.videochat.discover;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.adpater.AppBarStateChangeListener;
import com.huanmedia.videochat.common.widget.CustomAppBarLayout;
import com.huanmedia.videochat.discover.fragment.ArtistDynamicFragment;
import com.huanmedia.videochat.discover.fragment.ArtistPhotosFragment;
import com.huanmedia.videochat.discover.fragment.ArtistUserInfoFragment;
import com.huanmedia.videochat.discover.fragment.ArtistVideoFragment;
import com.huanmedia.videochat.main2.adapter.MainPageFragmentAdapter;
import com.huanmedia.videochat.main2.datamodel.TabMode;
import com.huanmedia.videochat.main2.fragment.FriendFragment;
import com.huanmedia.videochat.main2.fragment.HomeFragment;
import com.huanmedia.videochat.main2.fragment.MyFragment;
import com.huanmedia.videochat.main2.fragment.VideoListFragment;
import com.huanmedia.videochat.mvp.entity.results.BusinessCardInfoResults;
import com.huanmedia.videochat.mvp.presenter.user.BusinessCardInfoPresenterlmpl;
import com.huanmedia.videochat.mvp.presenter.user.IBusinessCardInfoPresenter;
import com.huanmedia.videochat.mvp.view.user.IBusinessCardInfoView;
import com.huanmedia.videochat.repository.entity.PhotosEntity;
import com.huanmedia.videochat.repository.entity.VideoEntity;
import com.makeramen.roundedimageview.RoundedImageView;

import org.litepal.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import mvp.data.store.glide.GlideApp;
import mvp.data.store.glide.GlideUtils;

public class ArtistActivity extends BaseActivity
        implements
        IBusinessCardInfoView,
        OnTabSelectListener,
        ViewPager.OnPageChangeListener,
        CustomAppBarLayout.CallBack {


    @BindView(R.id.appbar_layout)
    CustomAppBarLayout mAppBarLayout;
    @BindView(R.id.iv_user_back)
    ImageView mIVUserBack;
    @BindView(R.id.iv_more)
    ImageView mIVMore;
    @BindView(R.id.iv_exit)
    ImageView mIVExit;
    @BindView(R.id.iv_user_icon)
    RoundedImageView mIVUserIcon;
    @BindView(R.id.tab_layout)
    CommonTabLayout mTabLayout;
    @BindView(R.id.rl_user_info)
    RelativeLayout mRLUserInfo;
    @BindView(R.id.tv_name)
    TextView mTVName;
    @BindView(R.id.tv_team_name)
    TextView mTVTeamName;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.ll_bt_layout)
    LinearLayout mLLBTLayout;

    private int mUserId;
    private ArrayList<CustomTabEntity> mTabs;
    private BusinessCardInfoResults mUserData;
    private IBusinessCardInfoPresenter mBusinessCardInfoPresenter;
    private Fragment[] mFragments;
    private MainPageFragmentAdapter mAdapter;

    private Disposable mUserPicDis;

    public static Intent getCallingIntent(Context context, int uid) {
        Intent intent = new Intent(context, ArtistActivity.class);
        intent.putExtra("uid", uid);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_artist;
    }

    @Override
    protected void initView() {
        super.initView();
        CoordinatorLayout.LayoutParams appBarLayoutParams = new CoordinatorLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                DisplayUtil.getDisplayHeight(this) + getResources().getDimensionPixelOffset(R.dimen.dimen_136dp));
        mAppBarLayout.setLayoutParams(appBarLayoutParams);

        RelativeLayout.LayoutParams ivUserBackLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                DisplayUtil.getDisplayHeight(this) + getResources().getDimensionPixelOffset(R.dimen.dimen_68dp));
        mIVUserBack.setLayoutParams(ivUserBackLayoutParams);

        mAppBarLayout.setCallBack(this);
        startMoreAnim(mIVMore);
    }

    @Override
    protected void initData() {
        super.initData();
        mUserId = getIntent().getIntExtra("uid", 0);

        mTabs = new ArrayList<>();
        mTabs.add(new TabMode("资料", R.drawable.tab_art_info_sel, R.drawable.tab_art_info_nor));
        mTabs.add(new TabMode("动态", R.drawable.tab_art_dynamic_sel, R.drawable.tab_art_dynamic_nor));
        mTabs.add(new TabMode("相册", R.drawable.tab_art_pic_sel, R.drawable.tab_art_pic_nor));
        mTabs.add(new TabMode("视频", R.drawable.tab_art_video_sel, R.drawable.tab_art_video_nor));
        mTabLayout.setTabData(mTabs);


        mFragments = new Fragment[]{
                ArtistUserInfoFragment.newInstance(),
                ArtistDynamicFragment.newInstance(),
                ArtistPhotosFragment.newInstance(),
                ArtistVideoFragment.newInstance()};
        mAdapter = new MainPageFragmentAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(4);
        mTabLayout.setOnTabSelectListener(this);
        mBusinessCardInfoPresenter = new BusinessCardInfoPresenterlmpl(this);
        mBusinessCardInfoPresenter.getBusinessCardInfo(mUserId);
    }

    private void startMoreAnim(View view) {
        TranslateAnimation translateAnimation = new TranslateAnimation
                (Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0.5f);
        translateAnimation.setDuration(500);
        translateAnimation.setRepeatCount(Animation.INFINITE);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(translateAnimation);
    }

    @OnClick({R.id.rl_user_info, R.id.iv_exit})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.rl_user_info:
                mAppBarLayout.setExpanded(false, true);
                break;
            case R.id.iv_exit:
                finish();
                break;
        }
    }

    @Override
    public void getBusinessCardInfoSuccess(BusinessCardInfoResults results) {
        this.mUserData = results;
        setUserName(results.getBase().getNickname());
        setUserIcon(results.getBase().getUserphoto_thumb());
        setUserPic(results.getBase().getPhpots());
        setUserVideo(results.getBase().getVoides());
    }

    @Override
    public void getBusinessCardInfoFail(String msg) {
        showToast(msg);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastLongInCenter(getContext(), toast);
    }

    private int mShowPicPosition = 0;
    private Drawable oldPic;

    /**
     * 设置用户的照片
     */
    private void setUserPic(List<PhotosEntity> listPhotoData) {
        if (listPhotoData == null || listPhotoData.size() == 0)
            return;
        if (mUserPicDis != null)
            mUserPicDis.dispose();
        mShowPicPosition = 0;
        oldPic = getResources().getDrawable(R.color.base_black);
        mUserPicDis = RxCountDown.interval(0, 3).subscribe(totalTime -> {
            GlideApp
                    .with(this)
                    .asDrawable()
                    .load(listPhotoData.get(mShowPicPosition).getPhoto_thumb())
//                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                            TransitionDrawable transitionDrawable = new TransitionDrawable
                                    (new Drawable[]{oldPic, resource});
                            mIVUserBack.setImageDrawable(transitionDrawable);
                            transitionDrawable.startTransition(500);
                            oldPic = resource;
                        }
                    });

            mShowPicPosition++;
            if (mShowPicPosition >= listPhotoData.size()) {
                mShowPicPosition = 0;
            }
        });
        if (mFragments[2] instanceof ArtistPhotosFragment) {
            ((ArtistPhotosFragment) mFragments[2]).setPhotosData(listPhotoData);
        }
    }

    /**
     * 设置视频数据
     *
     * @param listData
     */
    private void setUserVideo(List<VideoEntity> listData) {
        if (listData == null || listData.size() == 0)
            return;
        if (mFragments[3] instanceof ArtistVideoFragment) {
            ((ArtistVideoFragment) mFragments[3]).setVideoData(listData);
        }
    }

    /**
     * 设置用户名称
     *
     * @param name
     */
    private void setUserName(String name) {
        mTVName.setText(name);
    }

    /**
     * 设置用户头像
     *
     * @param iconUrl
     */
    private void setUserIcon(String iconUrl) {
        GlideApp
                .with(this)
                .load(iconUrl)
                .placeholder(R.drawable.icon_headportrait)
                .into(mIVUserIcon);
    }

    /**
     * 设置团队名称
     *
     * @param teamName
     */
    public void setTeamName(String teamName) {
        mTVTeamName.setText(teamName);
    }

    @Override
    protected void onDestroy() {
        if (mUserPicDis != null)
            mUserPicDis.dispose();
        super.onDestroy();
    }

    @Override
    public void onTabSelect(int position) {
        mViewPager.setCurrentItem(position, true);
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTabLayout.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onExpanded(AppBarLayout appBarLayout) {
        mLLBTLayout.setVisibility(View.GONE);
    }

    @Override
    public void onCollapsed(AppBarLayout appBarLayout) {
        mLLBTLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onIdle(AppBarLayout appBarLayout) {
        mLLBTLayout.setVisibility(View.VISIBLE);
    }
}
