package com.huanmedia.videochat.mvp.view.user;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.DynamicListResults;

public interface IDynamicListView extends BaseMVPView {
    /**
     * 获取动态列表数据成功
     * @param results
     */
    void getDynamicListSuccess(DynamicListResults results);

    /**
     * 获取动态列表数据失败
     * @param msg
     */
    void getDynamicListFail(String msg);
}
