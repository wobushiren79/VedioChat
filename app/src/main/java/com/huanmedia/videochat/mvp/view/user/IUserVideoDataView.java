package com.huanmedia.videochat.mvp.view.user;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.UserVideoDataResults;

import java.util.List;

public interface IUserVideoDataView extends BaseMVPView {
    /**
     * 上传用户视频数据成功
     */
    void uploadUserVideoSuccess(UserVideoDataResults results);

    /**
     * 上传用户视频数据失败
     *
     * @param msg
     */
    void uploadUserVideoFail(String msg);

    /**
     * 删除视频成功
     */
    void deleteUserVideoSuccess();

    /**
     * 删除视频失败
     * @param msg
     */
    void deleteUserVideoFail(String msg);

    /**
     * 获取上传的视频名称
     *
     * @return
     */
    String getUpLoadVideoName();

    /**
     * 获取视频封面
     *
     * @return
     */
    String getUpLoadVideoIcon();

    /**
     * 获取需要删除的视频IDS
     * @return
     */
    List<String> getDeleteVideoIds();
}
