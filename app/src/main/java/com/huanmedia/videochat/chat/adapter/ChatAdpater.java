package com.huanmedia.videochat.chat.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.widget.TextView;

import com.applecoffee.devtools.base.adapter.BaseRCMultiAdatper;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.chat.ChatSpannableString;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.mvp.entity.results.ChatListResults;
import com.huanmedia.videochat.mvp.entity.results.UserInfoResults;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mvp.data.store.glide.GlideUtils;

public class ChatAdpater extends BaseRCMultiAdatper<ChatListResults.Item> {
    private final int OtherChatLayout = 1;
    private final int SelfChatLayout = 2;

    private UserInfoResults mSelfInfo;
    private UserInfoResults mOtherInfo;

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
            return super.addData(mDatas);
        }
    }

    public ChatAdpater(Context context) {
        super(context);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, ChatListResults.Item itemData, int position, int type) {
        RoundedImageView ivHeard = null;
        TextView tvContent = null;
        TextView tvHasRead = null;

        if (type == OtherChatLayout || type == SelfChatLayout) {
            ivHeard = baseViewHolder.getView(R.id.iv_heard);
            tvContent = baseViewHolder.getView(R.id.tv_content);
            tvHasRead = baseViewHolder.getView(R.id.tv_hasread);
        }

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
        } else {
            if (mOtherInfo != null)
                GlideUtils.getInstance().loadBitmapNoAnim(mContext, mOtherInfo.getUserphoto_thumb(), ivHeard);
            tvHasRead.setText("");
        }
        if (itemData.getMsg() != null) {
            ChatSpannableString spannableString = new ChatSpannableString(getContext(),itemData.getMsg());
            tvContent.setText(spannableString);
        }


    }

    @Override
    public Map<Integer, Integer> setLayoutMap() {
        Map<Integer, Integer> layoutMaps = new HashMap<>();
        layoutMaps.put(OtherChatLayout, R.layout.item_chat_other);
        layoutMaps.put(SelfChatLayout, R.layout.item_chat_self);
        return layoutMaps;
    }

    @Override
    public int setItemType(int i) {
        int sendUserId = mDatas.get(i).getAccount_id();
        if (sendUserId == UserManager.getInstance().getId()) {
            return SelfChatLayout;
        } else {
            return OtherChatLayout;
        }
    }
}
