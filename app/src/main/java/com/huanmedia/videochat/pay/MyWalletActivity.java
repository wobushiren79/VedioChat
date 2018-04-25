package com.huanmedia.videochat.pay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.common.BaseMVPActivity;
import com.huanmedia.videochat.common.widget.dialog.DialogPick;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.my.OnCashFragmentListener;
import com.huanmedia.videochat.pay.mode.MyWalletMode;
import com.huanmedia.videochat.repository.entity.BillDetialEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class MyWalletActivity extends BaseMVPActivity<MyWalletPresenter> implements MyWalletView, OnCashFragmentListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.my_wallet_vp_content)
    ViewPager mMyWalletVpContent;
    @BindView(R.id.my_wallet_stl)
    SlidingTabLayout mMyWalletStl;
    private HintDialog mLoadingDialog;
    private HintDialog mHintDialog;
    private MyWalletPageFragmentAdapter mAdapter;
    //    private OnTabSelectListener mOnTabSelectListener;
    private int mCurrentMainTabSelect;
    private String mCurrentDate;
    private int mCurrentFragmentPosition;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, MyWalletActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_wallet;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_particulars_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.account_particulars_menu_time_filter://时间筛选
                DialogPick dialogPick = new DialogPick(this);
                dialogPick.setDatelistener(obj -> {
                    mCurrentDate = obj;
                    ((MyWalletFragment) mAdapter.getItem(mCurrentFragmentPosition)).setDate(mCurrentDate);
                });
                dialogPick.showDateFilter(mCurrentDate, "%d-%d");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initView() {
        initToolbar();

    }

    @Override
    protected void initData() {
        BaseFragment[] fragments = new BaseFragment[]{MyWalletFragment.newInstance(MyWalletFragment.WallPageType.COIN), MyWalletFragment.newInstance(MyWalletFragment.WallPageType.MCION)};
        mAdapter = new MyWalletPageFragmentAdapter(getSupportFragmentManager(), fragments);
        mMyWalletVpContent.setAdapter(mAdapter);
        mMyWalletStl.setViewPager(mMyWalletVpContent, new String[]{getString(R.string.my_wallet_coin), getString(R.string.my_wallet_mcoin)});
        mMyWalletVpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentFragmentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
//        mOnTabSelectListener.onTabSelect(0);
    }

    @Override
    protected View getTitlebarView() {
        return mToolbar;
    }

    @Override
    protected ImmersionBar defaultBarConfig() {
        return super.defaultBarConfig().statusBarDarkFont(true);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public void showLoading(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new HintDialog(context(), HintDialog.HintType.LOADING);
            mLoadingDialog.show();
            mLoadingDialog.setTitleText(getString(R.string.loading));
        } else if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(int flag, String message) {
        flag = flag == 0 ? HintDialog.HintType.WARN : flag;
        if (mHintDialog == null) {
            mHintDialog = new HintDialog(this, flag);
            mHintDialog.showToast(HintDialog.LENGTH_SHORT);
            mHintDialog.setTitleText(message);
        } else {
            if (mHintDialog.getType() != flag) {
                mHintDialog.setType(flag);
            }
            mHintDialog.setTitleText(message);
            mHintDialog.showToast(HintDialog.LENGTH_SHORT);
        }
    }


    @Override
    public Context context() {
        return this;
    }


    @Override
    public void showDataForType(@MyWalletFragment.WallPageType int type, BillDetialEntity rdata) {
        BaseFragment fragment = mAdapter.getItem(type);
        List<MyWalletMode> modes = null;
        if (rdata.getItems() != null && rdata.getItems().size() > 0) {

            modes = new ArrayList<>();
            String currentGroup = rdata.getItems().get(0).getDate();
            modes.add(new MyWalletMode(true, currentGroup));
            for (int i = 0; i < rdata.getItems().size(); i++) {
                BillDetialEntity.ItemsEntity item = rdata.getItems().get(i);
                if (!item.getDate().equals(currentGroup)) {//分组标识
                    currentGroup = rdata.getItems().get(i).getDate();
                    modes.add(new MyWalletMode(true, currentGroup));
                }
                modes.add(new MyWalletMode(item));
            }
        }
        if (type >= 0) {
            if (rdata.getPage() == 1) {
                ((MyWalletFragment) fragment).setNewData(modes, rdata.getItems().size() >= rdata.getPagesize());
            } else if (rdata.getPage() > 1) {
                ((MyWalletFragment) fragment).loadMore(modes, (rdata.getTotalpage() == rdata.getPage()));
            }
        }
    }

    @Override
    public void showHint(int positon, int hintType, String generalErrorStr) {
        switch (hintType) {
            case 1000:
                ((MyWalletFragment) mAdapter.getItem(positon)).loadMoreError();
                break;

            default:
                showError(HintDialog.HintType.WARN, generalErrorStr);
                break;
        }
    }

    @Override
    public void getDataForType(@MyWalletFragment.WallPageType int fragmentType, int page, String data) {
        getBasePresenter().loadData(fragmentType, page, data);
    }

    private static class MyWalletPageFragmentAdapter extends FragmentPagerAdapter {
        Map<Integer, String> fragmentNames = new HashMap<>();
        private final BaseFragment[] mFragments;

        public Map<Integer, String> getFragmentNames() {
            return fragmentNames;
        }

        public MyWalletPageFragmentAdapter(FragmentManager fm, BaseFragment[] fragments) {
            super(fm);
            this.mFragments = fragments;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            fragmentNames.put(position, makeFragmentName(container.getId(), position));
            return super.instantiateItem(container, position);
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }

        @Override
        public BaseFragment getItem(int position) {
            return mFragments[position];
        }

        private static String makeFragmentName(int viewId, long id) {
            return "android:switcher:" + viewId + ":" + id;
        }
    }
}
