package com.huanmedia.videochat.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPActivity;
import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class BoundIDCardActivity extends BaseMVPActivity<BoundIDCardPresenter> implements BoundIDCardView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bound_idcard_et_rename)
    EditText mBoundIdcardEtRename;
    @BindView(R.id.bound_idcard_et_idCard)
    EditText mBoundIdcardEtIdCard;
    @BindView(R.id.bound_idcard_btn_ok)
    Button mBoundIdcardBtnOk;
    private HintDialog mHintDialog;
    private HintDialog mLoadingDialog;
    private int mType;
    private String mAccoundID;

    public static Intent getCallingIntent(Context context, int boundType, String useri_d) {
        Intent intent = new Intent(context, BoundIDCardActivity.class);
        intent.putExtra("boundType", boundType);
        intent.putExtra("accound_id", useri_d);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        mType=getIntent().getIntExtra("boundType",0);
        mAccoundID=getIntent().getStringExtra("accound_id");
        initToolbar();
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
    protected int getLayoutId() {
        return R.layout.activity_bound_idcard;
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
        showHint(HintDialog.HintType.WARN, message);
    }

    public void showHint(int type, String hint) {
        if (mHintDialog == null) {
            mHintDialog = new HintDialog(this, HintDialog.HintType.WARN);
            mHintDialog.showToast(HintDialog.LENGTH_SHORT);
            mHintDialog.setTitleText(hint);
        } else {
            if (mHintDialog.getType() != type) {
                mHintDialog.setType(type);
            }
            mHintDialog.setTitleText(hint);
            mHintDialog.showToast(HintDialog.LENGTH_SHORT);
        }
    }

    @Override
    public Context context() {
        return this;
    }


    @Override
    public void boundSuccess() {
        Intent intent = new Intent(EventBusAction.ACTION_BOUND_ACCOUND_CHANGE);
        intent.putExtra("status", 1);
        EventBus.getDefault().post(intent);
        finish();
    }

    @OnClick(R.id.bound_idcard_btn_ok)
    public void onViewClicked() {
        getBasePresenter().upUserBoundData(mType,mAccoundID,
                mBoundIdcardEtRename.getText().toString(),
                mBoundIdcardEtIdCard.getText().toString());
    }
}
