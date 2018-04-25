package com.huanmedia.videochat.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.videochat.BuildConfig;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;

import butterknife.BindView;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.about_tv_version)
    TextView mAboutTvVersion;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        initToolbar();
        mAboutTvVersion.setText(String.format(mAboutTvVersion.getText().toString(), BuildConfig.VERSION_NAME));
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
        return R.layout.activity_about;
    }
}
