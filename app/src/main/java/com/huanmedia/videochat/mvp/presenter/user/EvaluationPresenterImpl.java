package com.huanmedia.videochat.mvp.presenter.user;

import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.SubmitEvaluationRequest;
import com.huanmedia.videochat.mvp.entity.results.SystemTagsResults;
import com.huanmedia.videochat.mvp.model.user.EvaluationModelImpl;
import com.huanmedia.videochat.mvp.view.user.IEvaluationView;

import java.util.ArrayList;
import java.util.List;

public class EvaluationPresenterImpl extends BaseMVPPresenter<IEvaluationView, EvaluationModelImpl> implements IEvaluationPresenter {

    //选择的数据
    private List<Integer> mSelectData;
    private int oldPosition;
    //标签数据
    private List<String> mListTagStr;
    private List<SystemTagsResults.Tag> mListTagData;
    private SystemTagsResults mData;

    public EvaluationPresenterImpl(IEvaluationView mMvpView) {
        super(mMvpView, EvaluationModelImpl.class);

    }

    @Override
    public void getSystemTag() {
        mMvpModel.getSystemTag(new DataCallBack<SystemTagsResults>() {

            @Override
            public void getDataSuccess(SystemTagsResults data) {
                if (data == null)
                    return;
                mData = data;
                mMvpView.getSystemTagSuccess(data);
                showTagList(data);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.getSystemTagFail(msg);
            }
        });
    }

    @Override
    public void setSelectTag(int position, String text) {
        if (mSelectData.size() < 3) {
            mSelectData.add(position);
        } else {
            mSelectData.set(oldPosition, position);
        }
        mMvpView.setSelectTags(mSelectData);

        int badorgood = 0;
        for (Integer itemPosition : mSelectData) {
            SystemTagsResults.Tag tagData = mListTagData.get(itemPosition);
            if (tagData.getType().equals("GOOD")) {
                badorgood++;
            } else if (tagData.getType().equals("BAD")) {
                badorgood--;
            }
        }
        if (badorgood >= 0) {
            mMvpView.setOtherTagText(mData.getGoodtxt());
        } else {
            mMvpView.setOtherTagText(mData.getBadtxt());
        }

        oldPosition++;
        if (oldPosition > 2)
            oldPosition = 0;
    }

    @Override
    public void submitEvaluation(int submitStyle) {
        long userId = mMvpView.getEvaluationUserId();
        String evaluationStr = mMvpView.getEvaluationText();
        StringBuffer evaluationTags = new StringBuffer();
        if (userId == 0) {
            mMvpView.showToast("没有被评价人ID");
            return;
        }
        if (mData == null || mData.getGoodtags().size() <= 0) {
            mMvpView.showToast("没有评价数据");
            return;
        }
        if (submitStyle == 1) {
            evaluationStr = mData.getGoodtxt();
            evaluationTags.append(mData.getGoodtags().get(0).getId() + "");
        }else{
            if(mSelectData==null||mSelectData.size()<=0){
                mMvpView.showToast("请至少选择一项");
                return;
            }
            boolean isFirst = true;
            for (Integer position : mSelectData) {
                SystemTagsResults.Tag itemTag = mListTagData.get(position);
                if (!isFirst)
                    evaluationTags.append(",");
                evaluationTags.append(itemTag.getId());
                isFirst = false;
            }
        }

        SubmitEvaluationRequest params = new SubmitEvaluationRequest();
        params.setUid((int) userId);
        params.setTxt(evaluationStr);
        params.setTagids(evaluationTags.toString());
        mMvpModel.submitEvaluation(params, new DataCallBack() {
            @Override
            public void getDataSuccess(Object data) {
                mMvpView.submitEvaluationSuccess();
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.submitEvaluationFail(msg);
            }
        });
    }

    /**
     * 展示Tag列表
     *
     * @param data
     */
    private void showTagList(SystemTagsResults data) {
        oldPosition = 0;
        mListTagStr = new ArrayList<>();
        mListTagData = new ArrayList<>();
        mSelectData = new ArrayList<>();

        if (data.getGoodtags() != null)
            for (SystemTagsResults.Tag item : data.getGoodtags()) {
                mListTagStr.add(item.getTag());
                mListTagData.add(item);
            }
        if (data.getBadtags() != null)
            for (SystemTagsResults.Tag item : data.getBadtags()) {
                mListTagStr.add(item.getTag());
                mListTagData.add(item);
            }
        mMvpView.showSystemTags(mListTagStr);
    }
}
