package com.huanmedia.videochat.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.appointment.fragment.AppointmentListOpFragment;
import com.huanmedia.videochat.chat.adapter.ChatAdpater;
import com.huanmedia.videochat.chat.bean.ChatIntentBean;
import com.huanmedia.videochat.chat.view.AppointmentChatLayout;
import com.huanmedia.videochat.chat.view.ChatPtrLayout;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.adpater.AppBarStateChangeListener;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.service.socket.WebSocketManager;
import com.huanmedia.videochat.common.utils.VideoChatUtils;
import com.huanmedia.videochat.common.widget.ptr.PtrLayout;
import com.huanmedia.videochat.main2.weight.ConditionEntity;
import com.huanmedia.videochat.mvp.entity.results.AppointmentDataOpResults;
import com.huanmedia.videochat.mvp.entity.results.AppointmentDetailResults;
import com.huanmedia.videochat.mvp.entity.results.ChatListResults;
import com.huanmedia.videochat.mvp.entity.results.ChatSendResults;
import com.huanmedia.videochat.mvp.presenter.chat.ChatSendPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.chat.IChatSendPresenter;
import com.huanmedia.videochat.mvp.view.chat.IChatSendView;
import com.huanmedia.videochat.repository.entity.VideoChatEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ChatActivity
        extends BaseActivity
        implements
        IChatSendView,
        TextView.OnEditorActionListener,
        View.OnClickListener,
        AppointmentChatLayout.CallBack,
        ChatMessage.CallBack {


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
    @BindView(R.id.ll_send)
    LinearLayout mLLSend;

    private ChatIntentBean mInfoBean;
    private IChatSendPresenter mSendPresenter;
    private ChatMessage mChatMessageListener;

    private AppointmentDetailResults mAppointmentInfo;

    @Override
    protected void onResume() {
        super.onResume();
        initHeardLayout();
        AppointmentListOpFragment.Is_Refresh_Data = true;
    }

    @Override
    protected void onDestroy() {
        if (mChatMessageListener != null)
            WebSocketManager.getInstance().removeListener(mChatMessageListener);
        super.onDestroy();
    }

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
        mToolbar.setNavigationOnClickListener(v -> finish());

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
        mInfoBean = (ChatIntentBean) getIntent().getSerializableExtra("IntentInfo");
        mSendPresenter = new ChatSendPresenterImpl(this);
        mChatMessageListener = new ChatMessage();
        mChatMessageListener.setCallBack(this);
        WebSocketManager.getInstance().addMessageListener(mChatMessageListener);
        if (mInfoBean == null)
            return;
        mPtrLayout.setChatUserId(mInfoBean.getChatUserId());
        //initHeardLayout();
    }

    /**
     * 初始化头部布局
     */
    private void initHeardLayout() {
        if (mInfoBean == null)
            return;
        mRLHead.removeAllViews();
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
        layout.setChatUserId(mInfoBean.getChatUserId());
        layout.setCallBack(this);
        mRLHead.addView(layout);
    }

    //-------------聊天发送-------------
    @Override
    public void chatSendSuccess(ChatSendResults results) {
        mETMsg.setText("");
        //发送刷新通知
        if (mInfoBean != null)
            mChatMessageListener.msgRefreshData(mInfoBean.getChatUserId());
        //刷新当前数据
        mPtrLayout.getNewChatData();
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
    public List<String> getChatSengImages() {
        return null;
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send:
                sendChat();
                break;
            case R.id.tv_video:
                startVideo();
                break;
        }
    }

    /**
     * 开始聊天
     */
    private void startVideo() {
        if (mInfoBean.getChatType() == ChatIntentBean.ChatType.Appointment && mAppointmentInfo != null) {
            if (mAppointmentInfo.getDetail().getAstatus() == AppointmentDataOpResults.OrderStatus.NoConfirm) {
                if (mAppointmentInfo.getDetail().getAccount_id() == mAppointmentInfo.getMyifno().getUid()) {
                    showToast("对方还没有确认预约哟~");
                }else{
                    showToast("您还没有确认预约哟~");
                }
            } else if (mAppointmentInfo.getDetail().getAstatus() == AppointmentDataOpResults.OrderStatus.ReadManConfirm) {
                VideoChatUtils.StartAppointmentCall(
                        this,
                        mAppointmentInfo.getDetail().getAccount_id(),
                        mAppointmentInfo.getDetail().getAccount_vipid(),
                        mAppointmentInfo.getDetail().getId(),
                        mInfoBean.getChatUserId(),
                        (int) UserManager.getInstance().getId());
            }
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

    //--------------预约详情布局回调-------------------------
    @Override
    public void getDetailsSuccess(AppointmentDetailResults results) {
        mAppointmentInfo = results;
        if (results != null && results.getOuinfo() != null)
            mToolbar.setTitle(results.getOuinfo().getNickname());
    }

    @Override
    public void getDetailsFail(String msg) {

    }

    @Override
    public void onStatusChange(int orderStatus, int complainStatus) {
        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        if (complainStatus == AppointmentDataOpResults.ComplainStatus.NoComplain) {
            switch (orderStatus) {
                case AppointmentDataOpResults.OrderStatus.NoConfirm:
                    mTVVideo.setBackgroundResource(R.drawable.btn_chat_video_style_2);
                    mLLSend.setVisibility(View.VISIBLE);
                    mTVVideo.setOnClickListener(this);
                    mTVSend.setOnClickListener(this);
                    layoutParams.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.dimen_120dp);
                    break;
                case AppointmentDataOpResults.OrderStatus.ReadManConfirm:
                    mTVVideo.setBackgroundResource(R.drawable.btn_chat_video);
                    mLLSend.setVisibility(View.VISIBLE);
                    mTVVideo.setOnClickListener(this);
                    mTVSend.setOnClickListener(this);
                    layoutParams.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.dimen_120dp);
                    break;
                default:
                    mLLSend.setVisibility(View.GONE);
                    mTVVideo.setOnClickListener(view -> {
                        showToast("当前状态不能发起视频哟~");
                    });
                    layoutParams.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.dimen_12dp);
                    break;
            }
        } else {
            layoutParams.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.dimen_12dp);
            mLLSend.setVisibility(View.GONE);
        }
        mPtrLayout.setLayoutParams(layoutParams);
    }

    //------------websocket操作------------------
    @Override
    public void websocketRefreshData() {
        mPtrLayout.setAllMsgRead();
        mPtrLayout.getNewChatData();
    }


    //------------------------滑动监听
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
