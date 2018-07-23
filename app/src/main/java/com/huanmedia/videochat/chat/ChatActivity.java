package com.huanmedia.videochat.chat;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.chat.bean.ChatIntentBean;
import com.huanmedia.videochat.chat.view.AppointmentChatLayout;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.mvp.entity.results.AppointmentDetailResults;

import butterknife.BindView;

public class ChatActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rl_head)
    RelativeLayout mRLHead;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;

    private ChatIntentBean mInfoBean;

    public static Intent getCallingIntent(Context context, ChatIntentBean intentBean) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("IntentInfo", intentBean);
        return intent;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
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
    protected View getTitlebarView() {
        return mToolbar;
    }

    @Override
    protected void initView() {
        initToolbar();
    }

    @Override
    protected void initData() {
        mInfoBean = (ChatIntentBean) getIntent().getSerializableExtra("IntentInfo");
        if (mInfoBean == null)
            return;
        switch (mInfoBean.getChatType()) {
            case ChatIntentBean.ChatType.Appointment:
                setAppointmentLayout();
                break;
        }


    }

    /**
     * 设置预约模式界面
     */
    private void setAppointmentLayout() {
        AppointmentChatLayout layout = new AppointmentChatLayout(this);
        layout.setOrderId(mInfoBean.getOrderId());
        layout.setCallBack(new AppointmentChatLayout.CallBack() {
            @Override
            public void getDetailsSuccess(AppointmentDetailResults results) {
                if (results != null && results.getOuinfo() != null)
                    mToolbar.setTitle(results.getOuinfo().getNickname());
            }

            @Override
            public void getDetailsFail(String msg) {

            }
        });
        mRLHead.addView(layout);
    }
}
