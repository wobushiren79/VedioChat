package com.huanmedia.videochat.mvp.view.info;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.AdsLuanchResults;

public interface IAdsLuanchView extends BaseMVPView {

    /**
     * 获取首页广告成功
     * @param request
     */
    void getLuanchInfoAdsSuccess(AdsLuanchResults request);

    /**
     * 获取首页广告是失败
     * @param msg
     */
    void getLuanchInfoAdsFail(String msg);

    /**
     * 设置首页广告图片
     * @param imageUrl
     */
    void setLuanchImage(String imageUrl);

    /**
     * 设置跳转内容
     * @param jumpUrl
     */
    void setJumpUrl(String jumpUrl);
}
