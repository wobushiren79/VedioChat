package com.huanmedia.videochat.mvp.view.file;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.FileManageResults;

public interface IFileManagePayView extends BaseMVPView {
    /**
     * 支付文件成功
     * @param results
     */
    void payFileSuccess(int fileID, int fileType,FileManageResults results);

    /**
     * 支付文件失败
     * @param msg
     */
    void payFileFail(String msg);
}
