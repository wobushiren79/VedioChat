package com.huanmedia.videochat.discover;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;

/**
 * 这个页面修改为个人名片
 */
public class BusinessCardAcitivty extends BaseActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_discover_info;
    }

    @Override
    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        addFragmentAndShow(R.id.discover_info_fl_content, BusinessCardFragment.newInstance(getIntent().getIntExtra("data",0),getIntent().getStringExtra("destance")), BusinessCardFragment.TAG);
    }

    @Override
    protected View getTitlebarView() {
        return findViewById(R.id.toolbar);
    }

    @Override
    protected ImmersionBar defaultBarConfig() {
        return super.defaultBarConfig().statusBarDarkFont(true);
    }

    public static Intent getCallingIntent(Context context, int uid, String destance) {
        Intent intent = new Intent(context, BusinessCardAcitivty.class);
        intent.putExtra("data",uid);
        intent.putExtra("destance",destance);
        return intent;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
