package com.huanmedia.videochat.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.huanmedia.videochat.R;

public class EditTextDialog extends Dialog implements View.OnClickListener {

    private EditText mETContent;
    private TextView mTVTitle;
    private TextView mTVEditTitle;
    private TextView mTVSubmit;

    private String mTitleStr;
    private String mEditTitleStr;
    private String mEditContentStr;
    private int mInputType;

    private CallBack mCallBack;

    public EditTextDialog(@NonNull Context context) {
        super(context, R.style.customDialog);
        mTitleStr = "";
        mEditTitleStr = "";
        mEditContentStr = "";
        mInputType = InputType.TYPE_CLASS_TEXT;
    }

    /***
     * 设置标题
     * @param title
     */
    public void setTitle(String title) {
        mTitleStr = title;
    }

    /**
     * 设置输入框标题
     *
     * @param title
     */
    public void setEditTitle(String title) {
        mEditTitleStr = title;
    }

    public void setEditContent(String content) {
        mEditContentStr = content;
    }

    /**
     * 设置回调
     *
     * @param callBack
     */
    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    /**
     * 设置输入类型
     *
     * @param inputType
     */
    public void setInputType(int inputType) {
        mInputType = inputType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edittext);
        if (getWindow() != null) {
            getWindow().getAttributes().gravity = Gravity.CENTER;
            getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        initView();
        initData();
    }

    private void initView() {
        mETContent = findViewById(R.id.et_content);
        mTVTitle = findViewById(R.id.tv_title);
        mTVEditTitle = findViewById(R.id.tv_edit_title);
        mTVSubmit = findViewById(R.id.tv_submit);

        mTVSubmit.setOnClickListener(this);
    }

    private void initData() {
        mTVTitle.setText(mTitleStr);
        mTVEditTitle.setText(mEditTitleStr);
        mETContent.setInputType(mInputType);
        mETContent.setText(mEditContentStr);
    }

    @Override
    public void onClick(View view) {
        if (view == mTVSubmit) {
            if (mCallBack != null)
                mCallBack.submitOnClick(EditTextDialog.this, mETContent.getText().toString());
        }
        cancel();
    }

    public interface CallBack {
        void submitOnClick(Dialog view, String content);
    }

}
