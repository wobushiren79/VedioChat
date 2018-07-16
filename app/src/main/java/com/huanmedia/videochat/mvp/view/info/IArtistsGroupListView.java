package com.huanmedia.videochat.mvp.view.info;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.ArtistsGroupResults;

import java.util.List;

public interface IArtistsGroupListView extends BaseMVPView {

    /**
     * 获取艺人组合数据成功
     * @param listData
     */
    void getArtistsGroupListSuccess(List<ArtistsGroupResults> listData);

    /**
     * 获取艺人组合列表失败
     * @param msg
     */
    void getArtistsGroupListFail(String msg);

    /**
     * 展示艺人组合列表
     */
    void showArtistsGroupList(List<ArtistsGroupResults> listData);
}
