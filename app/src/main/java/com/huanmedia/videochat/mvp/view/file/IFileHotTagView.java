package com.huanmedia.videochat.mvp.view.file;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.FileHotTagResults;

import java.util.List;

public interface IFileHotTagView extends BaseMVPView {
    /**
     * 获取热门标签成功
     *
     * @param listData
     */
    void getFileHotTagListSuccess(List<FileHotTagResults> listData);


    /**
     * 获取热门标签失败
     *
     * @param msg
     */
    void getFileHotTagListFail(String msg);

    /**
     * 设置热门标签
     * @param listData
     */
    void setFileHotTagList(List<String> listData);
}
