package com.huanmedia.videochat.mvp.view.user;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.SystemTagsResults;

import java.util.List;

public interface IEvaluationView extends BaseMVPView {

    /**
     * 获取系统标签成功
     */
    void getSystemTagSuccess(SystemTagsResults results);

    /**
     * 获取系统标签失败
     *
     * @param msg
     */
    void getSystemTagFail(String msg);

    /**
     * 提交成功
     */
    void submitEvaluationSuccess();

    /**
     * 提交失败
     * @param msg
     */
    void submitEvaluationFail(String msg);

    /**
     * 展示标签
     *
     * @param tags
     */
    void showSystemTags(List<String> tags);

    /**
     * 设置选中的tags
     *
     * @param selectTagPosition
     */
    void setSelectTags(List<Integer> selectTagPosition);

    /**
     * 设置综合Tag文字
     */
    void setOtherTagText(String text);


    /**
     * 获取被评论人ID
     *
     * @return
     */
    long getEvaluationUserId();

    /**
     * 获取被评论信息
     *
     * @return
     */
    String getEvaluationText();
}
