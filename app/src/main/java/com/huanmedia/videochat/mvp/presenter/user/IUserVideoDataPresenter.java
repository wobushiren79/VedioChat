package com.huanmedia.videochat.mvp.presenter.user;

import java.util.List;

public interface IUserVideoDataPresenter {

    /**
     * 上传用户数据
     */
    void uploadUserVideoInfo();

    /**
     * 上传隐私视频
     */
    void uploadUserVideoInfoBySercet(int price, String tag);

    /**
     * 删除用户数据
     */
    void deleteUserVideoInfo();

}
