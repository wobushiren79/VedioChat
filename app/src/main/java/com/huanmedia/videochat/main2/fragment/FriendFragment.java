package com.huanmedia.videochat.main2.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.main2.adapter.HomeFragmentAdapter;

import butterknife.BindView;

public class FriendFragment extends BaseFragment {

    @BindView(R.id.main_friend_slidingTabLayout)
    SlidingTabLayout mMainFriendSlidingTabLayout;
    @BindView(R.id.home_toolbar_fl)
    FrameLayout mHomeToolbarFl;
    //    @BindView(R.id.toolbar)
//    Toolbar mToolbar;
    @BindView(R.id.main_friend_vp)
    ViewPager mMainFriendVp;
    private MainInteractionListener mListener;
    private Fragment[] mFragments;
    private OnTabSelectListener mOnTabSelectListener;
    private int mCurrentMainTabSelect;

    public FriendFragment() {
    }


    public static FriendFragment newInstance() {
        FriendFragment fragment = new FriendFragment();
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friend;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    @Override
    protected void statusBarConfig() {
        super.statusBarConfig();
        mHomeToolbarFl.getLayoutParams().height += DisplayUtil.getStatusBarHeight(getContext());
    }


    @Override
    protected void initView(View view) {
        mMainFriendVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mCurrentMainTabSelect != position) {
                    mOnTabSelectListener.onTabSelect(position);
                } else {
                    mOnTabSelectListener.onTabReselect(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mMainFriendSlidingTabLayout.setOnTabSelectListener(mOnTabSelectListener);

        mOnTabSelectListener = new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (mCurrentMainTabSelect != -1) {
                    TextView olderView = mMainFriendSlidingTabLayout.getTitleView(mCurrentMainTabSelect);
                    olderView.setTextSize(16);
                    olderView.setTypeface(Typeface.DEFAULT);
                }
                TextView textView = mMainFriendSlidingTabLayout.getTitleView(position);
                textView.setTextSize(20);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                mCurrentMainTabSelect = position;
            }

            @Override
            public void onTabReselect(int position) {

            }
        };
    }

    @Override
    protected void initData() {
        String[] titles;
        if (UserManager.getInstance().getCurrentUser().getUserinfo().getIsstarauth() == 1) {
            titles = new String[]{getString(R.string.calling_people), getString(R.string.tab_fans), getString(R.string.attention_people)};
            mFragments = new Fragment[]{ComeAcrossFriendFragment.newInstance(), FansFragment.newInstance(), AttentionFragment.newInstance()};
        } else {
            titles = new String[]{getString(R.string.calling_people), getString(R.string.attention_people)};
            mFragments = new Fragment[]{ComeAcrossFriendFragment.newInstance(), AttentionFragment.newInstance()};
        }

        HomeFragmentAdapter adapter = new HomeFragmentAdapter(getFragmentManager(), mFragments);
        mMainFriendVp.setOffscreenPageLimit(3);
        mMainFriendVp.setAdapter(adapter);


        mMainFriendSlidingTabLayout.setViewPager(mMainFriendVp, titles);
        mOnTabSelectListener.onTabSelect(0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainInteractionListener) {
            mListener = (MainInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
