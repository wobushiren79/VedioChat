package com.huanmedia.videochat.test;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.widget.tablayout.TabSwitchBean;
import com.huanmedia.videochat.common.widget.tablayout.TabSwitchView;
import com.huanmedia.videochat.common.widget.video.VideoPtrLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TestActivity extends BaseActivity {

    @BindView(R.id.bt_test_1)
    Button btTest1;
    @BindView(R.id.bt_test_2)
    Button btTest2;
    @BindView(R.id.tab_switch_view)
    TabSwitchView tabSwitchView;

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
        List<TabSwitchBean> mTabs = new ArrayList<>();
        mTabs.add(new TabSwitchBean("约聊", R.drawable.tab_home_found_sel, R.drawable.tab_home_found_nor));
        mTabs.add(new TabSwitchBean("视频", R.drawable.tab_home_video_sel, R.drawable.tab_home_video_nor));
        mTabs.add(new TabSwitchBean("萌友", R.drawable.tab_home_friend_sel, R.drawable.tab_home_friend_nor));
        mTabs.add(new TabSwitchBean("我", R.drawable.tab_home_my_sel, R.drawable.tab_home_my_nor));
        tabSwitchView.setTabData(mTabs);

        btTest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabSwitchView.showMsgNumber(2, 99);
            }
        });
        btTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabSwitchView.showMsgNumber(2, 0);
            }
        });
    }

    @Override
    protected void initData() {
    }
}
