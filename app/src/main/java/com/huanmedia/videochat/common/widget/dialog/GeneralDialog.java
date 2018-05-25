package com.huanmedia.videochat.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huanmedia.videochat.R;

public class GeneralDialog extends Dialog implements View.OnClickListener {
    private TextView mTVTitle;
    private TextView mTVContent;
    private TextView mTVSubmit;
    private TextView mTVCancel;

    private CallBack mCallBack;

    private String titleStr;
    private String contentStr;
    private String submitStr;
    private String cancelStr;

    public GeneralDialog(@NonNull Context context) {
        super(context, R.style.customDialog);
        titleStr = "提示";
        contentStr = "";
        submitStr = "确定";
        cancelStr = "取消";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_general);
        if (getWindow() != null) {
            getWindow().getAttributes().gravity = Gravity.CENTER;
            getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        initView();
        initData();
    }


    private void initView() {
        mTVTitle = findViewById(R.id.tv_title);
        mTVContent = findViewById(R.id.tv_content);
        mTVSubmit = findViewById(R.id.tv_submit);
        mTVCancel = findViewById(R.id.tv_cancel);
    }

    private void initData() {
        mTVTitle.setText(titleStr);
        mTVContent.setText(contentStr);
        mTVSubmit.setText(submitStr);
        mTVCancel.setText(cancelStr);

        mTVSubmit.setOnClickListener(this);
        mTVCancel.setOnClickListener(this);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public GeneralDialog setTitle(String title) {
        titleStr = title;
        return this;
    }

    /**
     * 设置内容
     *
     * @param content
     */
    public GeneralDialog setContent(String content) {
        contentStr = content;
        return this;
    }

    /**
     * 设置提交文字信息
     *
     * @param submit
     * @return
     */
    public GeneralDialog setSubmitText(String submit) {
        submitStr = submit;
        return this;
    }

    /**
     * 设置取消文字提示
     *
     * @param cancel
     * @return
     */
    public GeneralDialog setCancelText(String cancel) {
        cancelStr = cancel;
        return this;
    }

    /**
     * 设置回调
     *
     * @param mCallBack
     */
    public GeneralDialog setCallBack(CallBack mCallBack) {
        this.mCallBack = mCallBack;
        return this;
    }

    @Override
    public void onClick(View view) {
        if (view == mTVSubmit) {
            if (mCallBack != null)
                mCallBack.submitClick(this);
        } else if (view == mTVCancel) {
            if (mCallBack != null)
                mCallBack.cancelClick(this);
        }
        this.cancel();
    }

    public interface CallBack {
        void submitClick(Dialog dialog);

        void cancelClick(Dialog dialog);
    }
}
