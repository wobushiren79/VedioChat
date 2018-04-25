package com.huanmedia.videochat.common;


import android.widget.TextView;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.huanmedia.videochat.R;

/**
 * Created by BlingBling on 2016/10/11.
 */

public class SimpleLoadMoreView extends LoadMoreView {
    boolean isShowLoadMoreEnd = true;

    public void setShowLoadMoreEnd(boolean showLoadMoreEnd) {
        isShowLoadMoreEnd = showLoadMoreEnd;
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_view_load_more;
    }

    @Override
    public int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    public int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    public int getLoadEndViewId() {
        if (isShowLoadMoreEnd)
            return R.id.load_more_load_end_view;
        else
            return R.id.load_more_load_end_no;
    }

}
