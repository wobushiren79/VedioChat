package com.huanmedia.videochat.main2.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.main2.adapter.HomeFragmentAdapter;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements MatchFragment.MatchInteractionListener {

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
    private int mToolbarHight;
    private Fragment[] mFragments;

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
        mToolbarHight=mToolbar.getLayoutParams().height;
    }


    @Override
    protected void initView(View view) {
        MatchFragment matchFragment = MatchFragment.newInstance();
        matchFragment.setListener(this);
        mFragments = new Fragment[]{matchFragment,
                DiscoverFragment.newInstance()};
        HomeFragmentAdapter adapter = new HomeFragmentAdapter(getFragmentManager(), mFragments);
        mMainHomeVpMainpage.setOffscreenPageLimit(3);
        mMainHomeVpMainpage.setAdapter(adapter);
        mMainHomeSlidingTableLayout.setViewPager(mMainHomeVpMainpage, new String[]{"偶遇", "发现"});
        mMainHomeVpMainpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mListener != null) {
                    if (positionOffset!=0.0f){
                        if (position==0){//开始滑动关闭本地视频传输
                            ((MatchFragment)mFragments[0]).stopCarmer();
                        }
                        mListener.onPageChange(positionOffset);
                        pageChange(positionOffset);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position==0){//滑动停止如果是匹配界面打开视频流
                    ((MatchFragment)mFragments[0]).startCarmer();
                }
                if (mCurrentMainTabSelect != position) {
                    mOnTabSelectListener.onTabSelect(position);
                } else {
                    mOnTabSelectListener.onTabReselect(position);
                }
                mListener.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state){
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        break;
                        case ViewPager.SCROLL_STATE_IDLE:
                            if (mMainHomeVpMainpage.getCurrentItem()==0){
                                 mListener.onPageChange(0);

                                pageChange(0);
                            }else {
                                mListener.onPageChange(1);

                                pageChange(1);
                            }
                            break;
                            case ViewPager.SCROLL_STATE_SETTLING:
                                break;
                }
            }
        });
        mMainHomeSlidingTableLayout.setOnTabSelectListener(mOnTabSelectListener);

        mOnTabSelectListener = new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (mCurrentMainTabSelect != -1) {
                    TextView olderView = mMainHomeSlidingTableLayout.getTitleView(mCurrentMainTabSelect);
                    olderView.setTextSize(16);
                    olderView.setTypeface(Typeface.DEFAULT);
                }
                TextView textView = mMainHomeSlidingTableLayout.getTitleView(position);
                textView.setTextSize(20);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                mCurrentMainTabSelect = position;
                if (mListener!=null){
                    if (position==0)
                    {
                        mListener.enableMainPageSliding(false);
                    }else {
                        mListener.enableMainPageSliding(true);
                    }
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        };
        mMainHomeSlidingTableLayout.setCurrentTab(1);
    }

    private void pageChange(float positionOffset) {
        ViewGroup.LayoutParams lp = mToolbar.getLayoutParams();
        boolean change = (int) (mToolbarHight * (positionOffset)) != lp.height;
        if (change){
            mMainHomeSlidingTableLayout.setAlpha(positionOffset);
            lp.height= (int) (mToolbarHight*(positionOffset));
            mToolbar.setLayoutParams(lp);
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
        if (mMainHomeVpMainpage!=null){
            return mMainHomeVpMainpage.getCurrentItem();
        }else {
            return -1;
        }
    }

    public void setCurrentItem(int currentItem) {//暴露方法设置该Fragment中的ViewPage
      if (mMainHomeVpMainpage!=null){
          mMainHomeVpMainpage.setCurrentItem(currentItem,true);
      }
    }

    @Override
    public void matchBack() {//匹配界面返回按钮点击
      setCurrentItem(1);
    }

    public interface HomeInterActionListener {
        void onPageChange(float offset);
        void enableMainPageSliding(boolean isEnable);
        void onPageSelected(int position);
    }
}
