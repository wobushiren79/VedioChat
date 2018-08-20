package com.huanmedia.videochat.mvp.view.file;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.FileManageResults;

public interface IFileManageCheckView extends BaseMVPView {

    /**
     * 检测是否拥有此文件成功
     *
     * @param fileID
     * @param fileType
     * @param results
     */
    void checkHasFileSuccess(int fileID, int fileType, FileManageResults results);

    /**
     * 检测是否拥有此文件失败
     *
     * @param msg
     */
    void checkHasFileFail(String msg);
}
