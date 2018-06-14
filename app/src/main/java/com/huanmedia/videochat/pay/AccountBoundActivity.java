package com.huanmedia.videochat.pay;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPActivity;
import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.common.pay.OnPayListener;
import com.huanmedia.videochat.common.pay.alipay.AliPayTools;
import com.huanmedia.videochat.common.widget.dialog.GeneralDialog;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提现账户选择
 */
public class AccountBoundActivity extends BaseMVPActivity<AccountBoundPressenter> implements AccountBoundView {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.my_account_btn_ok)
    Button mMyAccountBtnOk;
    @BindView(R.id.my_account_bound_ll)
    LinearLayout mMyAccountBoundLl;
    @BindView(R.id.my_account_bound_rb_wxPay)
    RadioButton mMyAccountBoundRbWxPay;
    @BindView(R.id.my_account_bound_rb_aliPay)
    RadioButton mMyAccountBoundRbAliPay;
    private boolean mIsbound;//是否已绑定
    private int mBoundType;//1是微信2是支付宝
    private CompoundButton.OnCheckedChangeListener mCheckChangeListener;
    private int mCurrentCheckId;
    private HintDialog mHintDialog;
    private HintDialog mLoadingDialog;
    private int mCurrentBoundType;

    public static Intent getCallingIntent(Context context, boolean isBound, int boundType) {
        Intent intent = new Intent(context, AccountBoundActivity.class);
        intent.putExtra("isBound", isBound);
        intent.putExtra("boundType", boundType);
        return intent;
    }

    @Override
    protected View getTitlebarView() {
        return mToolbar;
    }

    @Override
    protected ImmersionBar defaultBarConfig() {
        return super.defaultBarConfig().statusBarDarkFont(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void initView() {
        mIsbound = getIntent().getBooleanExtra("isBound", false);
        initToolbar();
        if (mIsbound) {
            mBoundType = getIntent().getIntExtra("boundType", 0);
            setBtnState(mBoundType);
            mCurrentCheckId = mBoundType == 1 ? R.id.my_account_bound_rb_wxPay : mBoundType == 2 ? R.id.my_account_bound_rb_aliPay : 0;
        }
        if (mCurrentCheckId != 0) {
            checkForID(mCurrentCheckId);
        }
        mMyAccountBtnOk.setText(mCurrentCheckId == 0 ? "绑定" : "解绑");
        mMyAccountBtnOk.setBackground(ContextCompat.getDrawable(this, mCurrentCheckId == 0 ? R.drawable.base_btn_solid : R.drawable.base_btn_solid_disable));
        mMyAccountBtnOk.setTextColor(getResources().getColor(mCurrentCheckId == 0 ? R.color.white : R.color.base_gray));
        mCheckChangeListener = (buttonView, isChecked) -> {
            if (isChecked) {
                mCurrentCheckId = buttonView.getId();
            }
            switch (buttonView.getId()) {
                case R.id.my_account_bound_rb_wxPay:
                    setBtnState(1);
                    if (isChecked) {
                        mMyAccountBoundRbAliPay.setOnCheckedChangeListener(null);
                        mMyAccountBoundRbAliPay.setChecked(false);
                        mMyAccountBoundRbAliPay.setOnCheckedChangeListener(mCheckChangeListener);
                    }
                    break;
                case R.id.my_account_bound_rb_aliPay:
                    setBtnState(2);
                    if (isChecked) {
                        mMyAccountBoundRbWxPay.setOnCheckedChangeListener(null);
                        mMyAccountBoundRbWxPay.setChecked(false);
                        mMyAccountBoundRbWxPay.setOnCheckedChangeListener(mCheckChangeListener);
                    }
                    break;
            }
        };
        mMyAccountBoundRbAliPay.setOnCheckedChangeListener(mCheckChangeListener);
        mMyAccountBoundRbWxPay.setOnCheckedChangeListener(mCheckChangeListener);
    }

    private void checkForID(int id) {
        if (id != 0)
            ((RadioButton) mMyAccountBoundLl.findViewById(id)).setChecked(true);
    }

    private void setBtnState(int boundType) {
        mMyAccountBtnOk.setText(boundType == mBoundType ? "解绑" : "绑定");
        mMyAccountBtnOk.setBackground(ContextCompat.getDrawable(this, boundType == mBoundType ? R.drawable.base_btn_solid_disable : R.drawable.base_btn_solid));
        mMyAccountBtnOk.setTextColor(getResources().getColor(boundType == mBoundType ? R.color.base_gray : R.color.white));
    }

    @OnClick({R.id.my_account_btn_ok})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.my_account_btn_ok:
                switch (mCurrentCheckId) {
                    case R.id.my_account_bound_rb_aliPay:
                        if (mBoundType == 2) {
                            GeneralDialog dialog = new GeneralDialog(this);
                            dialog
                                    .setContent("是否解除账户绑定")
                                    .setCallBack(new GeneralDialog.CallBack() {
                                        @Override
                                        public void submitClick(Dialog dialog) {
                                            getBasePresenter().unBoundUser(2);
                                        }

                                        @Override
                                        public void cancelClick(Dialog dialog) {

                                        }
                                    })
                                    .show();
                        } else {
                            getBasePresenter().boundUser(2);
                        }
                        break;
                    case R.id.my_account_bound_rb_wxPay:
                        if (mBoundType == 1) {
                            Toast.makeText(this, "解绑", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "绑定", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        showHint(HintDialog.HintType.WARN, "请选择一个账户类型");
                        break;
                }
                break;
        }
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
    protected int getLayoutId() {
        return R.layout.activity_account_bound;
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
        showHint(HintDialog.HintType.WARN, message);
    }

    @Override
    public Context context() {
        return this;
    }

    @SuppressWarnings({"RedundantStringToString", "unchecked"})
    @Override
    public void beforBindData(Object result) {
        Map<String, Object> map = (Map<String, Object>) result;
        AliPayTools.aliAuth(this, new OnPayListener() {
            @Override
            public void onSuccess(String msg) {
                try {
                    msg = msg.substring(msg.indexOf("user_id="));
                    String[] prams = msg.split("&");
                    String user_id = null;
                    if (prams.length > 0) {
                        user_id = prams[0].split("=")[1];
                    }
                    mCurrentBoundType = 2;
                    getNavigator().navtoBoundIDCard(AccountBoundActivity.this, mCurrentBoundType, user_id);
                } catch (Exception e) {
                    showHint(HintDialog.HintType.WARN, "无法绑定该账户");
                }

            }

            @Override
            public void onError(String msg) {
                showHint(HintDialog.HintType.WARN, msg);
            }
        }, map.get("result").toString());
//        },"2015123101057029","2088811033511483","3");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void boundAccoundChange(Intent intent) {
        if (intent.getAction() == null) return;
        switch (intent.getAction()) {
            case EventBusAction.ACTION_BOUND_ACCOUND_CHANGE:
                int status = intent.getIntExtra("status", 0);
                if (status == 1) {//用户数据绑定成功（身份证）
                    boundSuccess(mCurrentBoundType);
                }
                break;
        }
    }

    @Override
    public void boundSuccess(int type) {
        mBoundType = type;
        mIsbound = true;
        setBtnState(mBoundType);
    }

    @Override
    public void unBoundSuccess() {
        showHint(HintDialog.HintType.WARN, "解绑成功");
        mBoundType = -1;
        mIsbound = false;
        setBtnState(0);
        EventBus.getDefault().post(new Intent(EventBusAction.ACTION_BOUND_ACCOUND_CHANGE));
    }
}
