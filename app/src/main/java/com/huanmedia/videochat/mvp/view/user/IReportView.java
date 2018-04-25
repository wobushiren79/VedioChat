package com.huanmedia.videochat.mvp.view.user;

import com.huanmedia.videochat.mvp.base.BaseMVPView;

public interface IReportView extends BaseMVPView {
    void reportSuccess();

    void reportFail(String msg);

    /**
     * 获取被举报人ID
     * @return
     */
    long getReportUserId();

    /**
     * 获取举报类型
     * @return
     */
    int getReportType();
}
