package com.huanmedia.videochat.launch.fragment;

import com.huanmedia.videochat.repository.entity.UserEntity;

import mvp.data.net.DataResponse;
import mvp.view.LoadDataView;

/**
 * Create by Administrator
 * time: 2017/11/20.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public interface LoginView extends LoadDataView {
    void showLoading(String title);
    /**
     * 登录成功
     * @param userModle
     */
    void loginSuccess(UserEntity userModle);

    /**
     * 需要完善资料
     * @param userModle
     */
    void completeInfo(UserEntity userModle);

    /**
     * 验证码回执
     * @param dataResponse
     */
    void smsCodeReturn(DataResponse dataResponse);
}
