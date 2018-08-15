package com.huanmedia.videochat.mvp.view.photo;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.repository.entity.PhotosEntity;

import java.util.List;

public interface IPhotoListView extends BaseMVPView {

    /**
     * 获取照片列表成功
     *
     * @param listData
     */
    void getPhotoListSuccess(List<PhotosEntity> listData);

    /**
     * 获取照片列表失败
     *
     * @param msg
     */
    void getPhotoListFail(String msg);

    /**
     * 设置所有照片
     * @param listData
     */
    void setAllPhotoList(List<PhotosEntity> listData);

    /**
     * 设置公开照片
     * @param listData
     */
    void setOpenPhotoList(List<PhotosEntity> listData);

    /**
     * 设置私有照片
     * @param listData
     */
    void setSecretList(List<PhotosEntity> listData);
}
