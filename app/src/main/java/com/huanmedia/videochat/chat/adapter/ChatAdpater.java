package com.huanmedia.videochat.chat.adapter;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.applecoffee.devtools.base.adapter.BaseRCMultiAdatper;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.applecoffee.devtools.utils.TimeUtils;
import com.google.gson.internal.LinkedTreeMap;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.chat.ChatSpannableString;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.widget.dialog.ReportDialog;
import com.huanmedia.videochat.mvp.entity.results.ChatListResults;
import com.huanmedia.videochat.mvp.entity.results.UserInfoResults;
import com.huanmedia.videochat.mvp.presenter.user.IReportPresenter;
import com.huanmedia.videochat.mvp.presenter.user.ReportPresenterImpl;
import com.huanmedia.videochat.mvp.view.user.IReportView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mvp.data.store.glide.GlideUtils;

public class ChatAdpater extends BaseRCMultiAdatper<ChatListResults.Item> implements IReportView {
    private final int OtherChatLayout = 1;
    private final int SelfChatLayout = 2;
    private final int HintChatLayout = 3;
    private final int TimeChatLayout = 4;

    public static final int HintNormalType = -1;
    public static final int HintReportType = -2;

    private UserInfoResults mSelfInfo;
    private UserInfoResults mOtherInfo;

    private IReportPresenter reportPresenter;
    private int mReportType;
    private int mChatUserId;

    public ChatAdpater(Context context) {
        super(context);

        reportPresenter = new ReportPresenterImpl(this);
    }

    /**
     * 设置对面聊天ID
     *
     * @param chatUserId
     */
    public void setChatUserId(int chatUserId) {
        mChatUserId = chatUserId;
    }

    /**
     * 设置自己的信息
     */
    public void setSelfInfo(UserInfoResults results) {
        mSelfInfo = results;
    }

    /**
     * 设置其他人的信息
     */
    public void setOtherInfo(UserInfoResults results) {
        mOtherInfo = results;
    }

    public List<ChatListResults.Item> addDataInIndex(List<ChatListResults.Item> listData, int index) {
        this.mDatas.addAll(index, listData);
        addTimeItem();
        this.notifyDataSetChanged();
        return this.mDatas;
    }

    @Override
    public List addData(List mDatas) {
        if (mDatas.size() == 1) {
            this.mDatas.addAll(mDatas);
            this.notifyItemInserted(this.mDatas.size() - 1);
            return this.mDatas;
        } else {
            this.mDatas.addAll(mDatas);
            addTimeItem();
            this.notifyDataSetChanged();
            return this.mDatas;
        }
    }

    /**
     * 增加时间
     */
    private void addTimeItem() {
        if (mDatas == null || mDatas.size() == 0)
            return;
        Map<String, Integer> timeList = new LinkedTreeMap<>();
        List<ChatListResults.Item> oldTimeItem = new ArrayList<>();
        for (int i = mDatas.size() - 1; i >= 0; i--) {
            if (mDatas.get(i).getId() == 0) {
                oldTimeItem.add(mDatas.get(i));
                continue;
            }
        }
        mDatas.removeAll(oldTimeItem);
        for (int i = mDatas.size() - 1; i >= 0; i--) {
            if (mDatas.get(i).getCreatetime() == 0)
                continue;
            long timeLong = mDatas.get(i).getCreatetime() * 1000;
            String timeStr = TimeUtils.longToStr(timeLong, "yyyy-MM-dd");
            timeList.put(timeStr, i);
        }
        for (Map.Entry<String, Integer> entry : timeList.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            ChatListResults.Item itemData = new ChatListResults.Item(0, key);
            mDatas.add(value, itemData);
        }
    }

    @Override
    public List<ChatListResults.Item> getData() {
        List<ChatListResults.Item> listData = new ArrayList<>();
        for (ChatListResults.Item itemData : mDatas) {
            if (itemData.getId() > 0) {
                listData.add(itemData);
            }
        }
        return listData;
    }

    public List<ChatListResults.Item> getRealData() {
        return mDatas;
    }


    @Override
    public void convert(BaseViewHolder baseViewHolder, ChatListResults.Item itemData, int position, int type) {
        if (type == OtherChatLayout || type == SelfChatLayout) {
            setChatItem(baseViewHolder, itemData, position, type);
        } else if (type == TimeChatLayout) {
            setTimeItem(baseViewHolder, itemData, position, type);
        } else {
            setHintItem(baseViewHolder, itemData, position, type);
        }
    }


    /**
     * 设置聊天 item
     *
     * @param baseViewHolder
     * @param itemData
     * @param position
     * @param type
     */
    private void setChatItem(BaseViewHolder baseViewHolder, ChatListResults.Item itemData, int position, int type) {
        RoundedImageView ivHeard = baseViewHolder.getView(R.id.iv_heard);
        TextView tvContent = baseViewHolder.getView(R.id.tv_content);
        TextView tvHasRead = baseViewHolder.getView(R.id.tv_hasread);
        if (type == SelfChatLayout) {
            if (mSelfInfo != null)
                GlideUtils.getInstance().loadBitmapNoAnim(mContext, mSelfInfo.getUserphoto_thumb(), ivHeard);
            if (itemData.getHandle_type() == 0) {
                tvHasRead.setText("[未读]");
            } else if (itemData.getHandle_type() == 1) {
                tvHasRead.setText("[已读]");
            } else {
                tvHasRead.setText("[未知]");
            }
        } else if (type == OtherChatLayout) {
            if (mOtherInfo != null)
                GlideUtils.getInstance().loadBitmapNoAnim(mContext, mOtherInfo.getUserphoto_thumb(), ivHeard);
            tvHasRead.setText("");
        }
        if (itemData.getMsg() != null) {
            ChatSpannableString spannableString = new ChatSpannableString(getContext(), itemData.getMsg());
            tvContent.setText(spannableString);
        }
    }

    /**
     * 设置时间 item
     *
     * @param baseViewHolder
     * @param itemData
     * @param position
     * @param type
     */
    private void setTimeItem(BaseViewHolder baseViewHolder, ChatListResults.Item itemData, int position, int type) {
        TextView tvContent = baseViewHolder.getView(R.id.tv_content);

        if (itemData.getMsg().equals(TimeUtils.longToStr(TimeUtils.getNowTime().getTime(), "yyyy-MM-dd"))) {
            tvContent.setText("今天");
        }else{
            tvContent.setText(itemData.getMsg());
        }


    }

    /**
     * 设置提示 item
     *
     * @param baseViewHolder
     * @param itemData
     * @param position
     * @param type
     */
    private void setHintItem(BaseViewHolder baseViewHolder, ChatListResults.Item itemData, int position, int type) {
        TextView tvContent = baseViewHolder.getView(R.id.tv_content);
        Spanned msg = Html.fromHtml(itemData.getMsg());
        tvContent.setText(msg);
        switch (itemData.getId()) {
//            case HintNormalType:
//                break;
            case HintReportType:
                tvContent.setOnClickListener(view -> {
                    reportUser();
                });
                break;
            default:
                tvContent.setOnClickListener(null);
                break;
        }
    }

    /**
     * 举报用户
     */
    private void reportUser() {
        ReportDialog reportDialog = new ReportDialog(getContext());
        reportDialog.setReportClickLisitener((reportType, item) -> {
            mReportType = reportType;
            reportPresenter.report();
        }).show();
    }

    @Override
    public Map<Integer, Integer> setLayoutMap() {
        Map<Integer, Integer> layoutMaps = new HashMap<>();
        layoutMaps.put(OtherChatLayout, R.layout.item_chat_other);
        layoutMaps.put(SelfChatLayout, R.layout.item_chat_self);
        layoutMaps.put(HintChatLayout, R.layout.item_chat_hint);
        layoutMaps.put(TimeChatLayout, R.layout.item_chat_time);
        return layoutMaps;
    }

    @Override
    public int setItemType(int i) {
        int sendUserId = mDatas.get(i).getAccount_id();
        int orderId = mDatas.get(i).getId();
        if (orderId <= 0) {
            if (orderId == 0)
                return TimeChatLayout;
            else
                return HintChatLayout;
        } else {
            if (sendUserId == UserManager.getInstance().getId())
                return SelfChatLayout;
            else
                return OtherChatLayout;
        }
    }

    //------------举报操作-----------------
    @Override
    public void reportSuccess() {
        showToast("举报成功");
    }

    @Override
    public void reportFail(String msg) {
        showToast(msg);
    }

    @Override
    public long getReportUserId() {
        return mChatUserId;
    }

    @Override
    public int getReportType() {
        return mReportType;
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShortInCenter(getContext(), toast);
    }
}
