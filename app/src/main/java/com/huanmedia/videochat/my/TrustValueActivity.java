package com.huanmedia.videochat.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPActivity;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.widget.WaveLoadingView;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.discover.adapter.BusinessCardAdapter;
import com.huanmedia.videochat.discover.weight.androidtagview.TagContainerLayout;
import com.huanmedia.videochat.repository.entity.BusinessCardUserTags;
import com.huanmedia.videochat.repository.entity.TrustValueEntity;
import com.huanmedia.videochat.repository.net.HostManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class TrustValueActivity extends BaseMVPActivity<TrustValuePresenter> implements TrustValueView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.trust_value_tgp_goods)
    TagContainerLayout mTrustValueTgpGoods;
    @BindView(R.id.trust_value_tgp_bad)
    TagContainerLayout mTrustValueTgpBad;
    @BindView(R.id.trust_value_tp)
    WaveLoadingView mTrustValueTp;
    @BindView(R.id.trust_value_sv)
    ScrollView mTrustValueSv;
    @BindView(R.id.tv_explain)
    TextView mTVExplain;
    private HintDialog mLoadingDialog;
    private HintDialog mHintDialog;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, TrustValueActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    protected void initView() {
        initToolbar();
        mTrustValueTp.setAnimDuration(3000);
    }

    @Override
    protected void initData() {
        mTrustValueTp.setProgressValue(UserManager.getInstance().getCurrentUser().getUserinfo().getTrustvalue());
        getBasePresenter().loadTrueData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_trust_value;
    }

    @Override
    public void showLoading(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new HintDialog(context(), HintDialog.HintType.LOADING);
            mLoadingDialog.show();
            mLoadingDialog.setTitleText("加载中...");
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
    public void showTrueData(TrustValueEntity trustValueEntity) {
        mTrustValueTp.setProgressValue(trustValueEntity.getTrustvalue());
        mTrustValueTp.setCenterTitle(trustValueEntity.getTrustvalue() + "");
        mTrustValueTp.setBottomTitle(BusinessCardAdapter.getTrustvalueStr(trustValueEntity.getTrustvalue()) + "");
        ArrayList<String> goodTags = new ArrayList<>();
        ArrayList<String> badTags = new ArrayList<>();
        for (int i = 0; i < trustValueEntity.getGood().size(); i++) {
            BusinessCardUserTags.TagEntity good = trustValueEntity.getGood().get(i);
            goodTags.add(good.getTag() + " +" + good.getTagcount());
        }
        for (int i = 0; i < trustValueEntity.getBad().size(); i++) {
            BusinessCardUserTags.TagEntity bad = trustValueEntity.getBad().get(i);
            badTags.add(bad.getTag() + " +" + bad.getTagcount());
        }
        mTrustValueTgpBad.setTags(badTags);
        mTrustValueTgpGoods.setTags(goodTags);
    }

    @OnClick({R.id.tv_explain})
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_explain:
                getNavigator().navtoWebActivity(this, HostManager.getHtmlUrl() + "wordh5/intro.html", "用户协议");
                break;
        }
    }
}
