package com.huanmedia.videochat.my;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPActivity;
import com.huanmedia.videochat.repository.net.HostManager;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpActivity extends BaseMVPActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.ll_call_layout)
    LinearLayout mLLCallLayout;
    @BindView(R.id.ll_new_layout)
    LinearLayout mLLNewLayout;


    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, HelpActivity.class);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    protected ImmersionBar defaultBarConfig() {
        return super.defaultBarConfig().statusBarDarkFont(true);
    }

    @Override
    protected View getTitlebarView() {
        return mToolbar;
    }

    @OnClick({R.id.ll_call_layout, R.id.ll_new_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_new_layout:
                getNavigator().navtoWebActiviyt(this, HostManager.getHtmlUrl() + "wordh5/helpnewuser/guide1.html", getString(R.string.activity_help_title_1));
                break;
            case R.id.ll_call_layout:
                getNavigator().navtoWebActiviyt(this, HostManager.getHtmlUrl() + "wordh5/helpnotice/call.html", getString(R.string.activity_help_title_2));
                break;
        }
    }
}
