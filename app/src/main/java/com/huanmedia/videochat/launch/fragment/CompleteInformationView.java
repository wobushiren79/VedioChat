package com.huanmedia.videochat.launch.fragment;

import com.huanmedia.videochat.repository.entity.UserEntity;

import mvp.data.net.DataResponse;
import mvp.view.BaseView;

/**
 * Create by Administrator
 * time: 2017/11/20.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public interface CompleteInformationView extends BaseView {
    /**
     * 数据上传成功
     * @param response
     */
    void upDateSuccess(DataResponse response);
    void showError(String message);

    /**
     * 确认提示
     * @param userinfo
     */
    void confirmUpdate(UserEntity.UserinfoEntity userinfo);
}
