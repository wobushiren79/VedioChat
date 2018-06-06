package com.huanmedia.videochat.mvp.view.info;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.AdsShufflingResults;

import java.util.List;

public interface IAdsShufflingView extends BaseMVPView {
    /**
     * 获取轮播广告信息成功
     * @param listData
     */
    void getShufflingAdsSuccess(List<AdsShufflingResults> listData);

    /**
     * 获取轮播广告信息失败
     * @param mgs
     */
    void getShufflingAdsFail(String mgs);

    /**
     * 获取轮播广告类型
     * @return
     */
    int getShufflingAdsType();
}
