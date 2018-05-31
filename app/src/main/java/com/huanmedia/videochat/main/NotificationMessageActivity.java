package com.huanmedia.videochat.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.ilibray.utils.TextViewDrawableUtils;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPActivity;
import com.huanmedia.videochat.common.PageBean;
import com.huanmedia.videochat.common.SimpleLoadMoreView;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.main.mode.SystemMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import q.rorbin.badgeview.QBadgeView;

public class NotificationMessageActivity extends BaseMVPActivity<NotificationMessagePresenter> implements NotificationMessageView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.notification_message_rv)
    RecyclerView mNotificationMessageRv;
    private HintDialog mHintDialog;
    private HintDialog mLoadingDialog;
    private BaseQuickAdapter<SystemMessage, BaseViewHolder> mAdapter;
    private PageBean mPage;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, NotificationMessageActivity.class);
        return intent;
    }

    @Override
    protected View getTitlebarView() {
        return mToolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notification_message;
    }

    @Override
    protected void initView() {
        mPage = new PageBean(20);
        mPage.setTotalSize(SystemMessage.count(SystemMessage.class));

        getBasePresenter().getData(mPage.nextpage(), mPage.getPageSize());
        initToolbar();
        mAdapter = new BaseQuickAdapter<SystemMessage, BaseViewHolder>(R.layout.item_notification_message) {
            @Override
            protected BaseViewHolder createBaseViewHolder(View view) {
                BaseViewHolder baseHolder = super.createBaseViewHolder(view);
                View targetVivew = baseHolder.getView(R.id.item_notif_msg_iv_icon);
                if (targetVivew == null) return baseHolder;
                targetVivew.setTag(R.id.image_tag, new QBadgeView(view.getContext())
                        .bindTarget(targetVivew)
                        .setBadgeGravity(Gravity.START | Gravity.TOP)
                        .setBadgePadding(0, false)
                        .setBadgeTextSize(8, true));
                return baseHolder;
            }

            @Override
            protected void convert(BaseViewHolder helper, SystemMessage item) {
                TextView titleView = helper.getView(R.id.item_notif_msg_tv_title);
                String title = item.getTitle();
                String desc = item.getDesc();
                if (title == null)
                    title = "";
                if (desc == null)
                    desc = "";
                titleView.setText(Check.checkReplace(title + desc));
                helper.setText(R.id.item_notif_msg_tv_time, Check.checkReplace(item.getStrtime()));
                if (!Check.isEmpty(item.getUrl())) {
                    helper.itemView.setOnClickListener(v -> {
                        SystemMessage data = (SystemMessage) getData().get(helper.getAdapterPosition());
                        if (!Check.isEmpty(data.getUrl())) {
                            data.setIsRed(1);
                            data.saveAsync().listen(success -> {
                            });
                            getNavigator().navtoWebActiviyt(NotificationMessageActivity.this, data.getUrl(), "详情");
                            notifyItemChanged(helper.getAdapterPosition());
                        }
                    });
                    TextViewDrawableUtils.setDrawable(titleView, null, null,
                            ContextCompat.getDrawable(context(), R.drawable.icon_system_jump), null);
                } else {
                    helper.itemView.setOnClickListener(null);
                    TextViewDrawableUtils.clearDrawable(titleView);
                }
                if (item != null && item.getType() != null && item.getType().equals("SYSTEMNOTICE")) {
                    ((RecyclerView.LayoutParams) helper.itemView.getLayoutParams()).topMargin = DisplayUtil.dip2px(context(), 20);
                    helper.setImageResource(R.id.item_notif_msg_iv_icon, R.drawable.icon_system_notice);
                } else {
                    helper.setImageResource(R.id.item_notif_msg_iv_icon, R.drawable.icon_system_msg);
                    ((RecyclerView.LayoutParams) helper.itemView.getLayoutParams()).topMargin = 0;
                }
                QBadgeView badgeView = (QBadgeView) helper.getView(R.id.item_notif_msg_iv_icon).getTag(R.id.image_tag);
                if (item.getIsRed() == 0) {
                    badgeView.setBadgeText(" ");
                } else {
                    badgeView.setBadgeText("");
                }

            }
        };
//        mNotificationMessageRv.addOnItemTouchListener(new OnItemClickListener() {
//            @Override
//            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//
//            }
//        });
        mNotificationMessageRv.setAdapter(mAdapter);
        mAdapter.setLoadMoreView(new SimpleLoadMoreView());
        mAdapter.setEmptyView(R.layout.base_list_empty, mNotificationMessageRv);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(() ->
                getBasePresenter()
                        .getData(mPage.nextpage(), mPage.getPageSize()), mNotificationMessageRv);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newMessge(SystemMessage message) {
        if (message != null) {
            mAdapter.addData(0, message);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newMessge(List<SystemMessage> message) {
        if (message != null) {
            mAdapter.addData(0, message);
        }
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
    public void showLoading(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new HintDialog(context(), HintDialog.HintType.LOADING);
            mLoadingDialog.show();
            mLoadingDialog.setTitleText(getString(R.string.loading));
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
        showHint(HintDialog.HintType.WARN, message);
    }

    public void showHint(int type, String hint) {
        if (mHintDialog == null) {
            mHintDialog = new HintDialog(this, HintDialog.HintType.WARN);
            mHintDialog.showToast(HintDialog.LENGTH_SHORT);
            mHintDialog.setTitleText(hint);
        } else {
            if (mHintDialog.getType() != type) {
                mHintDialog.setType(type);
            }
            mHintDialog.setTitleText(hint);
            mHintDialog.showToast(HintDialog.LENGTH_SHORT);
        }
    }

    @Override
    public Context context() {
        return this;
    }

    @Override
    public void showData(List<SystemMessage> systemMessages) {
        if (systemMessages != null && systemMessages.size() > 0) {
            mAdapter.addData(systemMessages);
        }
        if (mPage.getTotalPage() == mPage.getCurrentPage()) {
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }
    }
}
