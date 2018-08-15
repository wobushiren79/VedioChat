package com.huanmedia.videochat.mvp.presenter.photo;

public interface IPhotoListPresenter {
    /**
     * 获取所有照片
     */
    void getAllPhoto();

    /**
     * 获取公开照片
     */
    void getOpenPhoto();

    /**
     * 获取隐私照片
     */
    void getSecretPhoto();
}
