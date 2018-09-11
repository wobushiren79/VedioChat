package com.huanmedia.videochat.mvp.model.user;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.DynamicListRequset;

public interface IDynamicModel {
    /**
     * 获取动态列表
     *
     * @param params
     * @param callBack
     */
    void getDynamicList(DynamicListRequset params, DataCallBack callBack);
}
