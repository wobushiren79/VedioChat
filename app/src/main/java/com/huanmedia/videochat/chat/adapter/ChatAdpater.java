package com.huanmedia.videochat.chat.adapter;

import android.content.Context;

import com.applecoffee.devtools.base.adapter.BaseRCMultiAdatper;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.mvp.entity.results.ChatListResults;

import java.util.HashMap;
import java.util.Map;

public class ChatAdpater extends BaseRCMultiAdatper<ChatListResults.Item> {
    private final int OtherChatLayout = 1;
    private final int SelfChatLayout = 2;

    public ChatAdpater(Context context) {
        super(context);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, ChatListResults.Item itemData, int position, int type) {

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
        return OtherChatLayout;
    }
}
