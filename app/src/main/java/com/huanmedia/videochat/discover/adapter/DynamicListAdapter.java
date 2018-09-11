package com.huanmedia.videochat.discover.adapter;

import android.content.Context;

import com.applecoffee.devtools.base.adapter.BaseRCAdapter;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.mvp.entity.results.DynamicListResults;

public class DynamicListAdapter extends BaseRCAdapter<DynamicListResults.DynamicItem> {
    public DynamicListAdapter(Context context) {
        super(context, R.layout.item_dynamic_list);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, DynamicListResults.DynamicItem dynamicItem, int i) {

    }
}
