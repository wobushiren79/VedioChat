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
import com.huanmedia.ilibray.utils.TimeUtils;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.mvp.entity.results.ShufflingAdsResults;
import com.huanmedia.videochat.mvp.presenter.info.IShufflingAdsPresenter;
import com.huanmedia.videochat.mvp.presenter.info.ShufflingAdsPresenterImpl;
import com.huanmedia.videochat.mvp.view.info.IShufflingAdsView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import mvp.data.store.glide.GlideUtils;

public class ShufflingViewPager extends BaseLinearLayout implements IShufflingAdsView {

    private ViewPager mVPContent;
    private RadioGroup mRGContent;

    private IShufflingAdsPresenter mShufflingAdsPresenter;
    private List<ShufflingAdsResults> mListAdsData;
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
        mShufflingAdsPresenter = new ShufflingAdsPresenterImpl(this);
        mShufflingAdsPresenter.getShufflingAdsInfo();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.layout_shuffling_viewpager;
    }


    @Override
    public void getShufflingAdsSuccess(List<ShufflingAdsResults> listData) {
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
        for (ShufflingAdsResults itemData : mListAdsData) {
            ImageView itemView = new ImageView(getContext());
            itemView.setScaleType(ImageView.ScaleType.FIT_XY);
            itemView.setOnClickListener((view) -> {
                switch (itemData.getLinktype()) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                    case 3:
                        ((BaseActivity) getContext()).getNavigator().navtoWebActiviyt
                                ((Activity) getContext(), itemData.getLinkurl(), null);
                        break;
                }

            });
            GlideUtils.getInstance().loadBitmapNoAnim(getContext(), itemData.getBimg(), itemView);
            mListView.add(itemView);

            RadioButton itemRB = new RadioButton(getContext());
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
                        mVPContent.setCurrentItem(i);
                    }
                }

            }
        });
        startTime();
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
