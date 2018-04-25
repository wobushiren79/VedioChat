package com.huanmedia.videochat.my;

import com.huanmedia.videochat.repository.entity.PhpotsEntity;

import java.util.List;

import mvp.view.LoadDataView;

/**
 * Create by Administrator
 * time: 2017/12/29.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

interface PhotosView extends LoadDataView{
    /**
     * 照片删除成功
     */
    void deleteSuccess();

    /**
     * 照片上传成功
     */
    void upPhotoSuccess(List<PhpotsEntity> phpotsEntities);    /**
     * 照片上传成功
     */
    void updateUserPhotosOrderSuccess();
}
