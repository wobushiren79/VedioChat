package com.huanmedia.videochat.mvp.presenter.file;

public interface IFileInfoChangePresenter {
    /**
     * 修改展示价格
     *
     * @param fileId
     * @param changePrice
     */
    void changePhotoPrice(int fileId, int changePrice);

    void changeVideoPrice(int fileId, int changePrice);

    /**
     * 修改共有私有
     *
     * @param fileId
     * @param changeType 1共有 2私有
     */
    void changePhotoType(int fileId, int changeType);

    void changeVideoType(int fileId, int changeType);

    /**
     * 修改标签
     *
     * @param fileId
     * @param changeTag
     */
    void changePhotoTag(int fileId, String changeTag);

    void changeVideoTag(int fileId, String changeTag);
}
