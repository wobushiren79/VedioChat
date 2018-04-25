package com.huanmedia.videochat.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;

import com.huanmedia.videochat.mvp.entity.results.SystemTagsResults;
import com.huanmedia.videochat.mvp.presenter.user.EvaluationPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.user.IEvaluationPresenter;
import com.huanmedia.videochat.mvp.presenter.user.IReportPresenter;
import com.huanmedia.videochat.mvp.presenter.user.ReportPresenterImpl;
import com.huanmedia.videochat.mvp.view.user.IEvaluationView;
import com.huanmedia.videochat.mvp.view.user.IReportView;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class EvaluationDialog extends Dialog implements TagView.OnTagClickListener, View.OnClickListener, IEvaluationView, IReportView {
    private TextView mTVCancel;
    private TextView mTVSumit;
    private TextView mTVEdit;
    private ImageView mIVReport;
    private TagContainerLayout mLayoutTag;

    private IEvaluationPresenter evaluationPresenter;
    private IReportPresenter reportPresenter;
    //回调
    private CallBack mCallBack;
    //被评价人ID
    private long mEvalutaionUserId;

    public EvaluationDialog(@NonNull Context context) {
        this(context, R.style.customDialog);
    }

    public EvaluationDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setEvalutaionUserId(long evalutaionUserId) {
        this.mEvalutaionUserId = evalutaionUserId;
    }

    public void setCallBack(CallBack callBack) {
        this.mCallBack = callBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_evaluation);
        if (getWindow() != null) {
            getWindow().getAttributes().gravity = Gravity.CENTER;
            getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        initView();
        initData();
    }

    private void initData() {
        reportPresenter = new ReportPresenterImpl(this);
        evaluationPresenter = new EvaluationPresenterImpl(this);

        evaluationPresenter.getSystemTag();

    }

    private void initView() {
        mTVCancel = findViewById(R.id.tv_cancel);
        mTVSumit = findViewById(R.id.tv_submit);
        mLayoutTag = findViewById(R.id.layout_tag);
        mTVEdit = findViewById(R.id.dialog_edit);
        mIVReport = findViewById(R.id.iv_report);

        mTVCancel.setOnClickListener(this);
        mTVSumit.setOnClickListener(this);
        mIVReport.setOnClickListener(this);
        mLayoutTag.setOnTagClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == mTVCancel) {
            evaluationPresenter.submitEvaluation(1);
            this.cancel();
        } else if (v == mTVSumit) {
            evaluationPresenter.submitEvaluation(0);
        } else if (v == mIVReport) {
            showReprotDialog();
        }
    }

    private int reportType;
    private ReportDialog reportDialog;

    private void showReprotDialog() {
        reportDialog = new ReportDialog(getContext());
        reportDialog.setReportClickLisitener((type, item) -> {
            reportType = type;
            reportPresenter.report();
            mIVReport.setVisibility(View.INVISIBLE);
        })
                .show();
    }

    //--------------------------标签样式------------------------------
    public void setTagViewStyle(TagView tagView, int style) {
        switch (style) {
            case 0:
                tagView.setTagTextColor(getContext().getResources().getColor(R.color.base_gray));
                tagView.setTagBorderColor(getContext().getResources().getColor(R.color.color_9D9FA5));
                tagView.setTagBackgroundColor(getContext().getResources().getColor(R.color.white));
                break;
            case 1:
                tagView.setTagTextColor(getContext().getResources().getColor(R.color.white));
                tagView.setTagBorderColor(getContext().getResources().getColor(R.color.white));
                tagView.setTagBackgroundColor(getContext().getResources().getColor(R.color.color_FF7978));
                break;
        }
        tagView.invalidate();
    }

    //------------------------------标签数据--------------------------
    @Override
    public void getSystemTagSuccess(SystemTagsResults results) {
        if (reportDialog != null) {
            reportDialog.cancel();
        }
    }

    @Override
    public void getSystemTagFail(String msg) {
        showToast(msg);
    }

    @Override
    public void submitEvaluationSuccess() {
        if (mCallBack != null) {
            mCallBack.submitSuccess();
        }
    }

    @Override
    public void submitEvaluationFail(String msg) {
//        showToast(msg);
        if (mCallBack != null) {
            mCallBack.submitSuccess();
        }
    }

    @Override
    public void showSystemTags(List<String> tags) {
        mLayoutTag.setTags(tags);
    }

    @Override
    public void setSelectTags(List<Integer> selectTagPosition) {
        for (int i = 0; i < mLayoutTag.getTags().size(); i++) {
            TagView tagView = mLayoutTag.getTagView(i);
            setTagViewStyle(tagView, 0);
        }
        for (Integer itemPosition : selectTagPosition) {
            TagView tagView = mLayoutTag.getTagView(itemPosition);
            setTagViewStyle(tagView, 1);
        }
    }

    @Override
    public void setOtherTagText(String text) {
        mTVEdit.setVisibility(View.VISIBLE);
        mTVEdit.setText(text);
    }

    @Override
    public long getEvaluationUserId() {
        return mEvalutaionUserId;
    }

    @Override
    public String getEvaluationText() {
        return mTVEdit.getText().toString();
    }


    @Override
    public void showToast(String toast) {
        ToastUtils.showToastLong(getContext(), toast);
    }

    //---------------------------标签点击------------------------------
    @Override
    public void onTagClick(int position, String text) {
        evaluationPresenter.setSelectTag(position, text);
    }

    @Override
    public void onTagLongClick(int position, String text) {

    }

    @Override
    public void onTagCrossClick(int position) {

    }

    //---------------------举报处理---------------------------
    @Override
    public void reportSuccess() {

    }

    @Override
    public void reportFail(String msg) {
//        showToast(msg);
    }

    @Override
    public long getReportUserId() {
        return mEvalutaionUserId;
    }

    @Override
    public int getReportType() {
        return reportType;
    }

    //----------------------- dialog回调-------------------------------
    public interface CallBack {
        void cancel();

        void submitSuccess();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mCallBack != null)
            mCallBack.cancel();
    }

    @Override
    public void cancel() {
        super.cancel();
        if (mCallBack != null)
            mCallBack.cancel();
    }
}
