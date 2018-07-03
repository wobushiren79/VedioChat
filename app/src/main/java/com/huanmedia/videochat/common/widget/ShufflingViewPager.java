package com.huanmedia.videochat.common.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.applecoffee.devtools.base.layout.BaseLinearLayout;
import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.navigation.Navigator;
import com.huanmedia.videochat.common.utils.UMengUtils;
import com.huanmedia.videochat.mvp.entity.results.AdsShufflingResults;
import com.huanmedia.videochat.mvp.presenter.info.IAdsShufflingPresenter;
import com.huanmedia.videochat.mvp.presenter.info.AdsShufflingPresenterImpl;
import com.huanmedia.videochat.mvp.view.info.IAdsShufflingView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import mvp.data.store.glide.GlideApp;

public class ShufflingViewPager extends BaseLinearLayout implements IAdsShufflingView {

    private ViewPager mVPContent;
    private RadioGroup mRGContent;

    private IAdsShufflingPresenter mShufflingAdsPresenter;
    private List<AdsShufflingResults> mListAdsData;
    private List<ImageView> mListView;
    private List<RadioButton> mListRBView;

    private Disposable mDisposable;
    private int mCurrentPage = 0;

    public ShufflingViewPager(Context context) {
        super(context);
    }

    public ShufflingViewPager(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView() {
        mVPContent = getView(R.id.vp_content);
        mRGContent = getView(R.id.rg_content);
    }

    @Override
    protected void initData() {
        mListView = new ArrayList<>();
        mListAdsData = new ArrayList<>();
        mListRBView = new ArrayList<>();
        mShufflingAdsPresenter = new AdsShufflingPresenterImpl(this);
        mShufflingAdsPresenter.getShufflingAdsInfo();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.layout_shuffling_viewpager;
    }


    @Override
    public void getShufflingAdsSuccess(List<AdsShufflingResults> listData) {
        this.mListAdsData = listData;
        setViewPagerData();
    }

    @Override
    public void getShufflingAdsFail(String mgs) {
        this.setVisibility(GONE);
    }

    @Override
    public int getShufflingAdsType() {
        return 1;
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShort(getContext(), toast);
    }

    private void setViewPagerData() {
        if (mListAdsData == null)
            return;
        mListView.clear();
        mListRBView.clear();
        mRGContent.removeAllViews();
        for (AdsShufflingResults itemData : mListAdsData) {
            ImageView itemView = new ImageView(getContext());
            itemView.setScaleType(ImageView.ScaleType.FIT_XY);
            itemView.setOnClickListener((view) -> {
                switch (itemData.getLinktype()) {
                    case 0:
                        break;
                    case 1:
                        shufflingIntent(itemData.getLinkurl());
                        break;
                    case 2:
                    case 3:
                        ((BaseActivity) getContext()).getNavigator().navtoWebActivity
                                ((Activity) getContext(), itemData.getLinkurl(), null,true);
                        break;
                }
                UMengUtils.AdsBanner(getContext(), itemData.getId() + "");
            });
            GlideApp.with(getContext()).asBitmap().load(itemData.getBimg()).placeholder(R.drawable.icon_shuffling_lodading).error(R.drawable.icon_shuffling_lodading).
                    into(itemView);
            mListView.add(itemView);

            RadioGroup.LayoutParams rbLayout = new RadioGroup.LayoutParams
                    (getResources().getDimensionPixelOffset(R.dimen.dimen_16dp),
                            getResources().getDimensionPixelOffset(R.dimen.dimen_4dp));
            rbLayout.leftMargin = getResources().getDimensionPixelOffset(R.dimen.dimen_6dp);
            rbLayout.rightMargin = getResources().getDimensionPixelOffset(R.dimen.dimen_6dp);
            RadioButton itemRB = new RadioButton(getContext());
            itemRB.setLayoutParams(rbLayout);
            itemRB.setBackgroundResource(R.drawable.rb_style_2);
            itemRB.setButtonDrawable(null);
            mRGContent.addView(itemRB);
            mListRBView.add(itemRB);
        }

        mVPContent.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                container.addView(mListView.get(position));
                return mListView.get(position);
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView(mListView.get(position));
            }

            @Override
            public int getCount() {
                return mListAdsData.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }
        });

        mVPContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RadioButton itemRB = mListRBView.get(position);
                itemRB.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (mListRBView != null && mListRBView.size() > 0) {
            mVPContent.setCurrentItem(mCurrentPage);
            mListRBView.get(mCurrentPage).setChecked(true);
        }

        mRGContent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < mListRBView.size(); i++) {
                    RadioButton rbItem = mListRBView.get(i);
                    if (rbItem.getId() == checkedId) {
                        mCurrentPage = i;
                        mVPContent.setCurrentItem(i);
                    }
                }

            }
        });
        startTime();
    }

    /**
     * 广告内联跳转
     *
     * @param intentUrl
     */
    private void shufflingIntent(String intentUrl) {
        if (intentUrl == null && intentUrl.length() == 0)
            return;
        Navigator navigator = ((BaseActivity) getContext()).getNavigator();
        switch (intentUrl) {
            case "RechargePage":
                //充值页
                navigator.navtoCoinPay((Activity) getContext(), null);
                break;
            case "FeedBackPage":
                //意见反馈
                navigator.navtoFeedBack((Activity) getContext());
                break;
            case "ReadManCertificate":
                //红人认证
                navigator.navtoReadMainCertificate((Activity) getContext());
                break;
        }
    }

    /**
     * 启动定时器
     */
    public void startTime() {
        mDisposable = RxCountDown.interval(5).subscribe(totalTime -> {
            mCurrentPage++;
            if (mCurrentPage == mListAdsData.size()) {
                mCurrentPage = 0;
            }
            mVPContent.setCurrentItem(mCurrentPage);
        });
    }


    /**
     * 关闭定时器
     */
    public void closeTimer() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
