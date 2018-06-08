package com.huanmedia.videochat.test;


import android.os.Bundle;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.widget.video.VideoPtrLayout;

public class TestActivity extends BaseActivity {

    private VideoPtrLayout ptrLayout;

    public TestActivity() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }


    @Override
    protected void initView() {
        ptrLayout = findViewById(R.id.ptr_layout);
    }

    @Override
    protected void initData() {
        super.initData();
    }
}
