package com.huanmedia.videochat.main2.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.main2.adapter.HomeFragmentAdapter;
import com.huanmedia.videochat.mvp.entity.results.ArtistsGroupResults;
import com.huanmedia.videochat.mvp.presenter.info.ArtistsGroupListPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.info.IArtistsGroupListPresenter;
import com.huanmedia.videochat.mvp.view.info.IArtistsGroupListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements MatchFragment.MatchInteractionListener, ViewPager.OnPageChangeListener, IArtistsGroupListView {

    //    @BindView(R.id.toolbar)
//    Toolbar mToolbar;
    @BindView(R.id.home_toolbar_fl)
    FrameLayout mToolbar;
    private HomeInterActionListener mListener;
    @BindView(R.id.main_home_slidingTabLayout)
    SlidingTabLayout mMainHomeSlidingTableLayout;
    private int mCurrentMainTabSelect = -1;
    @BindView(R.id.main_home_vp_mainpage)
    ViewPager mMainHomeVpMainpage;
    private OnTabSelectListener mOnTabSelectListener;
    private IArtistsGroupListPresenter mArtistsGroupListPresenter;
    private int mToolbarHight;
    private List<Fragment> mFragments;

    MatchFragment matchFragment;
    DiscoverFragment discoverFragment;
    HomeFragmentAdapter adapter;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeInterActionListener) {
            mListener = (HomeInterActionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @Override
    protected void statusBarConfig() {
        super.statusBarConfig();
        mToolbar.getLayoutParams().height += DisplayUtil.getStatusBarHeight(getContext());
        mToolbarHight = mToolbar.getLayoutParams().height;
    }

    @Override
    protected void initView(View view) {
        matchFragment = MatchFragment.newInstance();
        matchFragment.setListener(this);
        discoverFragment = DiscoverFragment.newInstance();
        mFragments = new ArrayList<>();
        mFragments.add(matchFragment);
        mFragments.add(discoverFragment);
        adapter = new HomeFragmentAdapter(getFragmentManager(), mFragments);

        mMainHomeVpMainpage.setOffscreenPageLimit(2);
        mMainHomeVpMainpage.setAdapter(adapter);
        mMainHomeVpMainpage.addOnPageChangeListener(this);

        mMainHomeSlidingTableLayout.setViewPager(mMainHomeVpMainpage, new String[]{"偶遇", "约聊"});
        mMainHomeSlidingTableLayout.setOnTabSelectListener(mOnTabSelectListener);

        mOnTabSelectListener = new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (mCurrentMainTabSelect != -1) {
                    TextView olderView = mMainHomeSlidingTableLayout.getTitleView(mCurrentMainTabSelect);
                    olderView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.dimen_36dp));
                    olderView.setTypeface(Typeface.DEFAULT);
                }
                TextView textView = mMainHomeSlidingTableLayout.getTitleView(position);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.dimen_40dp));
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                mCurrentMainTabSelect = position;
                if (mListener != null) {
                    if (position == 0) {
                        mListener.enableMainPageSliding(false);
                    } else {
                        mListener.enableMainPageSliding(true);
                    }
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        };
        mMainHomeSlidingTableLayout.setCurrentTab(1);

        mArtistsGroupListPresenter = new ArtistsGroupListPresenterImpl(this);
        mArtistsGroupListPresenter.getArtistsGroupList();
    }


    private void pageChange(float positionOffset) {
        ViewGroup.LayoutParams lp = mToolbar.getLayoutParams();
        boolean change = (int) (mToolbarHight * (positionOffset)) != lp.height;
        if (change) {
            mMainHomeSlidingTableLayout.setAlpha(positionOffset);
            lp.height = (int) (mToolbarHight * (positionOffset));
            mToolbar.setLayoutParams(lp);
            RelativeLayout.LayoutParams vplp = (RelativeLayout.LayoutParams) mMainHomeVpMainpage.getLayoutParams();
            vplp.setMargins(0,
                    (int) (getResources().getDimensionPixelOffset(R.dimen.dimen_108dp) * positionOffset),
                    0,
                    0);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home2;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public int getCurrentItem() {
        if (mMainHomeVpMainpage != null) {
            return mMainHomeVpMainpage.getCurrentItem();
        } else {
            return -1;
        }
    }

    public void setCurrentItem(int currentItem) {//暴露方法设置该Fragment中的ViewPage
        if (mMainHomeVpMainpage != null) {
            mMainHomeVpMainpage.setCurrentItem(currentItem, true);
        }
    }

    @Override
    public void matchBack() {//匹配界面返回按钮点击
        setCurrentItem(1);
    }

    @Override
    public void getArtistsGroupListSuccess(List<ArtistsGroupResults> listData) {

        String[] listTitle = new String[listData.size() + 2];

        listTitle[0] = "偶遇";
        listTitle[1] = "约聊";

        for (int i = 0; i < listData.size(); i++) {
            ArtistsGroupFragment itemFragment = new ArtistsGroupFragment();
            itemFragment.setGroupId(listData.get(i).getId());
            mFragments.add(itemFragment);
            listTitle[i + 2] = listData.get(i).getName();
        }
        adapter.notifyDataSetChanged();
        mMainHomeVpMainpage.setOffscreenPageLimit(listData.size() + 2);

        mMainHomeSlidingTableLayout.setViewPager(mMainHomeVpMainpage, listTitle);
        mMainHomeSlidingTableLayout.notifyDataSetChanged();
//        if (listData.size() != 0)
//            mMainHomeSlidingTableLayout.setCurrentTab(2);
    }

    @Override
    public void getArtistsGroupListFail(String msg) {

    }

    @Override
    public void showArtistsGroupList(List<ArtistsGroupResults> listData) {

    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShortInCenter(getContext(), toast);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mListener != null) {
            if (positionOffset != 0.0f) {
                if (position == 0) {//开始滑动关闭本地视频传输
                    ((MatchFragment) mFragments.get(0)).stopCarmer();
                }
                if (position == 0) {
                    mListener.onPageChange(positionOffset);
                    pageChange(positionOffset);
                }
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {//滑动停止如果是匹配界面打开视频流
            ((MatchFragment) mFragments.get(0)).startCarmer();
        }
        if (mCurrentMainTabSelect != position) {
            mOnTabSelectListener.onTabSelect(position);
        } else {
            mOnTabSelectListener.onTabReselect(position);
        }
        mListener.onPageSelected(position);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (position > 1) {
            mToolbar.setBackgroundResource(R.color.transparent);
            layoutParams.setMargins(0, 0, 0, 0);
        } else {
            mToolbar.setBackgroundResource(R.color.white);
            layoutParams.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.dimen_108dp) + DisplayUtil.getStatusBarHeight(getContext()), 0, 0);
        }
        mMainHomeVpMainpage.setLayoutParams(layoutParams);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                if (mMainHomeVpMainpage.getCurrentItem() == 0) {
                    mListener.onPageChange(0);
                    pageChange(0);
                } else {
                    mListener.onPageChange(1);
                    pageChange(1);
                }
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                break;
        }
    }

    public interface HomeInterActionListener {
        void onPageChange(float offset);

        void enableMainPageSliding(boolean isEnable);

        void onPageSelected(int position);
    }
}
