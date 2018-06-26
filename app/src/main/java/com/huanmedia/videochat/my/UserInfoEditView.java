package com.huanmedia.videochat.my;

import com.huanmedia.videochat.repository.entity.ItemMenuEntity;
import com.huanmedia.videochat.repository.entity.OccupationsEntity;
import com.huanmedia.videochat.repository.entity.PhotosEntity;
import com.huanmedia.videochat.repository.entity.VideoEntity;

import java.util.ArrayList;
import java.util.List;

import mvp.view.LoadDataView;

/**
 * Create by Administrator
 * time: 2017/12/28.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

interface UserInfoEditView extends LoadDataView {
    /**
     * 显示照片墙数据
     *
     * @param phpots
     */
    void showPhotos(List<PhotosEntity> phpots);

    /**
     * 展示视频数据
     *
     * @param videos
     */
    void showVideos(List<VideoEntity> videos);

    /**
     * 显示城市数据
     *
     * @param citys
     */
    void showCitys(List<ItemMenuEntity> citys);

    /**
     * 显示行业信息
     *
     * @param userConfigData
     */
    void showCoinfgData(OccupationsEntity userConfigData);

    /**
     * 提示信息
     *
     * @param type
     * @param hint
     */
    public void showHint(int type, String hint);

    /**
     * 认证状态：下一步跳入红人资料界面
     */
    void showNext();
}
