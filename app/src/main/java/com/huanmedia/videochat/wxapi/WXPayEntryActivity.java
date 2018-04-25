package com.huanmedia.videochat.wxapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPActivity;
import com.huanmedia.videochat.common.event.CoinChangeEvent;
import com.huanmedia.videochat.common.manager.ActivitManager;
import com.huanmedia.videochat.common.pay.OnPayListener;
import com.huanmedia.videochat.common.pay.alipay.AliPayTools;
import com.huanmedia.videochat.common.pay.wechat.WechatPay;
import com.huanmedia.videochat.common.pay.wechat.WechatPayTools;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.pay.CoinPayActivity;
import com.huanmedia.videochat.repository.entity.PayCoinTypeMode;
import com.huanmedia.videochat.repository.entity.PayOlderEntity;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class WXPayEntryActivity extends BaseMVPActivity<PayOrderPresenter> implements PayOrderView, IWXAPIEventHandler {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.pay_older_tv_actual_payment)
    TextView mPayOlderTvActualPayment;
    @BindView(R.id.pay_older_tv_coin)
    TextView mPayOlderTvCoin;
    @BindView(R.id.pay_older_rl_wx)
    RelativeLayout mPayOlderRlWx;
    @BindView(R.id.pay_older_rl_alipay)
    RelativeLayout mPayOlderRlAlipay;
    @BindView(R.id.pay_order_btn_ok)
    Button mPayOrderBtnOk;
    @BindView(R.id.pay_older_rb_wxpay)
    RadioButton mPayOlderRbWxpay;
    @BindView(R.id.pay_older_rb_alipay)
    RadioButton mPayOlderRbAlipay;
    private PayCoinTypeMode mMode;
    private String mOdrderNum;
    private HintDialog mHintDialog;

    public static Intent getCallingIntent(Context context, PayCoinTypeMode payEntity, String orderNum) {
        Intent intent = new Intent(context, WXPayEntryActivity.class);
        if (payEntity != null)
            intent.putExtra("payEntity", payEntity);
        if (!Check.isEmpty(orderNum))
            intent.putExtra("orderNum", orderNum);
        return intent;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WechatPay.getInstance().getWXApi().handleIntent(intent, this);
    }

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        mMode = getIntent().getParcelableExtra("payEntity");
        mOdrderNum = getIntent().getStringExtra("orderNum");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());


        if (mMode == null || mOdrderNum == null) {
            Toast.makeText(this, "参数异常", Toast.LENGTH_SHORT).show();
            finish();
        }
        mPayOlderTvCoin.setText(Check.checkReplace((mMode.getCoin() + mMode.getExtra()) + " 钻石"));
        mPayOlderTvActualPayment.setText(String.format("实付款:￥%s", Check.checkReplace(mMode.getPrice())));
        mPayOrderBtnOk.setText("确认支付:￥ " + Check.checkReplace(mMode.getPrice()));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_order;
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
    public Context context() {
        return this;
    }

    @OnClick({R.id.pay_older_rl_wx, R.id.pay_older_rl_alipay, R.id.pay_order_btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pay_older_rl_wx:
                mPayOlderRbWxpay.setChecked(true);
                mPayOlderRbAlipay.setChecked(false);
                break;
            case R.id.pay_older_rl_alipay:
                mPayOlderRbWxpay.setChecked(false);
                mPayOlderRbAlipay.setChecked(true);
                break;
            case R.id.pay_order_btn_ok:
                if (mPayOlderRbAlipay.isChecked()) {
                    getBasePresenter().payOrder(mOdrderNum, "ALIPAY");
                } else if (mPayOlderRbWxpay.isChecked()) {
                    getBasePresenter().payOrder(mOdrderNum, "WXPAY");
                }
                break;
        }
    }

    @Override
    public void doingPay(PayOlderEntity payMode) {
        if (payMode.getAlipay() != null) {
            AliPayTools.aliPay(WXPayEntryActivity.this, new OnPayListener() {
                @Override
                public void onSuccess(String msg) {
                    EventBus.getDefault().post(new CoinChangeEvent(CoinChangeEvent.CoinChangeType.RECHARGE));
                    ActivitManager.getAppManager().finishActivity();
                    ActivitManager.getAppManager().finishActivity(CoinPayActivity.class);
                }

                @Override
                public void onError(String msg) {
                    error(msg);
                }
            }, payMode.getAlipay());
        } else if (payMode.getWxpay() != null) {
            WechatPayTools.wechatPayApp(WXPayEntryActivity.this, payMode.getWxpay(), new OnPayListener() {
                @Override
                public void onSuccess(String msg) {
                    EventBus.getDefault().post(new CoinChangeEvent(CoinChangeEvent.CoinChangeType.RECHARGE));
                    ActivitManager.getAppManager().finishActivity();
                    ActivitManager.getAppManager().finishActivity(CoinPayActivity.class);
                }

                @Override
                public void onError(String msg) {
                    error(msg);
                }
            });
            WechatPay.getInstance().getWXApi().handleIntent(getIntent(), this);
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void error(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        if (mHintDialog == null) {
            mHintDialog = new HintDialog(context(), HintDialog.HintType.LOADING);
            mHintDialog.showDelay(1);
            mHintDialog.setTitleText(getString(R.string.loading));
        } else if (!mHintDialog.isShowing()) {
            mHintDialog.showDelay(1);
        }
    }

    @Override
    public void hideLoading() {
        if (mHintDialog != null) {
            mHintDialog.dismiss();
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
//        //微信支付回调
//        error("通用回调接口:微信");
    }

    @Override
    public void onResp(BaseResp baseResp) {
        //微信支付回调
        WechatPay.getInstance().onResp(baseResp.errCode);
    }
}
