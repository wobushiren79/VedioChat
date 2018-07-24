package com.huanmedia.videochat.chat;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.chat.adapter.ChatAdpater;
import com.huanmedia.videochat.chat.bean.ChatIntentBean;
import com.huanmedia.videochat.chat.view.AppointmentChatLayout;
import com.huanmedia.videochat.chat.view.ChatPtrLayout;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.adpater.AppBarStateChangeListener;
import com.huanmedia.videochat.common.widget.ptr.PtrLayout;
import com.huanmedia.videochat.mvp.entity.results.AppointmentDetailResults;
import com.huanmedia.videochat.mvp.presenter.chat.ChatSendPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.chat.IChatSendPresenter;
import com.huanmedia.videochat.mvp.view.chat.IChatSendView;

import butterknife.BindView;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity implements IChatSendView, TextView.OnEditorActionListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rl_head)
    RelativeLayout mRLHead;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.ptr_layout)
    ChatPtrLayout mPtrLayout;
    @BindView(R.id.et_msg)
    EditText mETMsg;
    @BindView(R.id.tv_send)
    TextView mTVSend;
    @BindView(R.id.tv_video)
    TextView mTVVideo;

    private ChatIntentBean mInfoBean;
    private IChatSendPresenter mSendPresenter;

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
        mAppbar.addOnOffsetChangedListener(new ChatAppBarChange());
        mETMsg.setOnEditorActionListener(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected void initData() {
        mSendPresenter = new ChatSendPresenterImpl(this);
        mInfoBean = (ChatIntentBean) getIntent().getSerializableExtra("IntentInfo");
        if (mInfoBean == null)
            return;
        mPtrLayout.setChatUserId(mInfoBean.getChatUserId());
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

    //-------------聊天发送-------------
    @Override
    public void chatSendSuccess() {
        mETMsg.setText("");
    }

    @Override
    public void chatSendFail(String msg) {
        showToast(msg);
    }

    @Override
    public String getChatSendMsg() {
        return mETMsg.getText().toString();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShortInCenter(this, toast);
    }
    //------------私有方法---------------

    @OnClick({R.id.tv_send, R.id.tv_video})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send:
                sendChat();
                break;
            case R.id.tv_video:
                break;
        }
    }

    /**
     * 发送聊天
     */
    private void sendChat() {
        if (mInfoBean != null)
            mSendPresenter.chatSend(mInfoBean.getChatUserId());
        else
            showToast("数据错误");
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEND:
                sendChat();
                break;
        }
        return true;
    }

    @Override
    public void onRefresh() {

    }


    public class ChatAppBarChange extends AppBarStateChangeListener {

        @Override
        public void onStateChanged(AppBarLayout appBarLayout, State state) {
            switch (state) {
                case EXPANDED:
                    mPtrLayout.enabledOnePtr();
                    break;
                case IDLE:
                    mPtrLayout.disablePtr();
                    break;
                case COLLAPSED:
                    mPtrLayout.disablePtr();
                    break;
            }
        }
    }

}
