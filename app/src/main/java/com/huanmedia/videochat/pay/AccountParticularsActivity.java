package com.huanmedia.videochat.pay;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.common.BaseMVPActivity;
import com.huanmedia.videochat.common.widget.dialog.DialogPick;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.my.CashListFragment;
import com.huanmedia.videochat.my.OnCashFragmentListener;
import com.huanmedia.videochat.repository.entity.BillDetialEntity;

import butterknife.BindView;

/**
 * 可提现明细
 */
@SuppressWarnings("ButterKnifeInjectNotCalled")
public class AccountParticularsActivity extends BaseMVPActivity<AccountParticularsPresenter> implements AccountParticularsView, OnCashFragmentListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.account_particulars_title)
    TextView mAccoundParticularsTitle;
    private HintDialog mHintDialog;
    private HintDialog mLoadingDialog;
    private String[] mTitles;
    private ListPopupWindow mTitleWindow;
    private BaseFragment[] mFragments;
    private int mCurrentFragmentPosition;
    private String mCurrentDate;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, AccountParticularsActivity.class);
    }

    @Override
    protected View getTitlebarView() {
        return mToolbar;
    }
    @Override
    protected void initView() {
        initToolbar();
        mFragments = new BaseFragment[2];
        mFragments[0] = CashListFragment.newInstance(0);
        mFragments[1] = CashListFragment.newInstance(1);
        addFragmentAndShow(R.id.account_particulars_fl, mFragments[0], "0");
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mTitles = getResources().getStringArray(R.array.account_particualars_action_list);
        initTitlePopWindows();
    }

    private void initTitlePopWindows() {
        mAccoundParticularsTitle.setText(mTitles[0]);
        mAccoundParticularsTitle.setOnClickListener(v -> {
            if (mTitleWindow == null) {
                mTitleWindow = new ListPopupWindow(context());
                mTitleWindow.setWidth((int) mAccoundParticularsTitle.getPaint().measureText(mTitles[0]) + DisplayUtil.dip2px(context(), 30));
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context(), R.layout.sample_wheel_item, mTitles);
                mTitleWindow.setAdapter(adapter);
                mTitleWindow.setAnchorView(v);
            }
            mTitleWindow.show();
            if (mTitleWindow.getListView() != null) {
                mTitleWindow.getListView().setDivider(ContextCompat.getDrawable(context(), R.drawable.divider_ll));
                mTitleWindow.getListView().setDividerHeight(DisplayUtil.dip2px(context(), 0.5f));
                mTitleWindow.getListView().setOnItemClickListener((parent, view, position, id) -> {
                    addFragmentAndShow(R.id.account_particulars_fl, mFragments[position], String.valueOf(position));
                    mTitleWindow.dismiss();
                    mCurrentFragmentPosition = position;
                    mAccoundParticularsTitle.setText(mTitles[position]);
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_particulars_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.account_particulars_cashMoney://可提现明细
//                showError("可提现明细");
//                break;
//            case R.id.account_particulars_jewel://钻石明细
//                showError("钻石明细");
//                break;
            case R.id.account_particulars_menu_time_filter://时间筛选
                DialogPick dialogPick = new DialogPick(this);

                dialogPick .setDatelistener(obj -> {
                        mCurrentDate = obj;
                    ((CashListFragment)mFragments[mCurrentFragmentPosition]).setDate(mCurrentDate);
                      });
                dialogPick.showDateFilter(mCurrentDate,"%d-%d");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_particulars;
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
        showHint(mCurrentFragmentPosition, HintDialog.HintType.WARN, message);
    }

    public void showHint(int fragmentType, int type, String hint) {
        switch (type) {
            case HintDialog.HintType.WARN:
                if (mHintDialog == null) {
                    mHintDialog = new HintDialog(this, HintDialog.HintType.WARN);
                    mHintDialog.showToast(HintDialog.LENGTH_SHORT);
                    mHintDialog.setTitleText(hint);
                } else if (!mHintDialog.isShowing()) {
                    mHintDialog.setTitleText(hint);
                    mHintDialog.showToast(HintDialog.LENGTH_SHORT);
                }
                break;
            case 1000:
                ((CashListFragment) mFragments[fragmentType]).loadMoreError();
                break;
        }
    }

    @Override
    public Context context() {
        return this;
    }

    @Override
    public void getDataForType(int positon, int page, String data) {
        getBasePresenter().loadData(positon, page, data);
    }

    @Override
    public void showDataForType(int position, BillDetialEntity rdata) {
        BaseFragment fragment = mFragments[position];
        if (position >=0){
            if (rdata.getPage() == 1) {
                ((CashListFragment)fragment).setNewData(rdata);
            } else if (rdata.getPage() > 1) {
                ((CashListFragment)fragment).loadMore(rdata);
            }
        }
    }
}
