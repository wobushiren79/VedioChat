package com.huanmedia.videochat.discover;

import com.huanmedia.videochat.repository.entity.BusinessCardEntity;
import com.huanmedia.videochat.repository.entity.UserEvaluateEntity;

import mvp.view.LoadDataView;

/**
 * Create by Administrator
 * time: 2017/12/12.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

interface BusinessCardView extends LoadDataView{

    void showHeadData(BusinessCardEntity businessCard);

    /**
     * 显示更多评价
     * @param userEvaluateEntity
     */
    void showMore(UserEvaluateEntity userEvaluateEntity);

    void loadMoreFail();

    /**
     * 关注状态改变
     * @param flag
     */
    void resultFavoriteSuccess(int flag);
}
