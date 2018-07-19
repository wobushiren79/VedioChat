package com.huanmedia.videochat.mvp.view.info;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.ArtistsGroupShowResults;

import java.util.List;

public interface IArtistsGroupShowView extends BaseMVPView {
    /**
     * 获取艺人展示数据成功
     * @param results
     */
    void getArtistsGroupShowDataSuccess(ArtistsGroupShowResults results);

    /**
     * 获取艺人展示数据失败
     * @param msg
     */
    void getArtistsGroupShowDataFail(String msg);


    /**
     * 获取艺人组合ID
     * @return
     */
    int getArtistsGroupId();


    /**
     * 设置以及组合背景
     * @param backGroundUrl
     */
    void setArtistsGroupBackGround(String backGroundUrl);


    /**
     * 设置艺人列表
     * @param listData
     */
    void setArtistsList(List<ArtistsGroupShowResults.Items> listData);
}
