package com.huanmedia.videochat.common.widget.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.huanmedia.videochat.R;

import butterknife.Unbinder;

/**
 * 性别选择对话框
 * Create by Administrator
 * time: 2017/11/21.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class SexChooseDialog extends Dialog implements View.OnClickListener {
    private View mRootView;
    private Unbinder mBind;
    private TextView mTitle;
    private CheckedSexListener mCheckedSexListener;
    private Integer mDefault;
    private LinearLayout mLLMan;
    private LinearLayout mLLWoman;


    public SexChooseDialog(@NonNull Context context) {
        super(context, R.style.customDialog_upward);
    }

    public void setDefault(Integer aDefault) {
        mDefault = aDefault;
    }

    public void setCheckedSexListener(CheckedSexListener checkedSexListener) {
        mCheckedSexListener = checkedSexListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sexchoose);
        if (getWindow() != null) {
            getWindow().getAttributes().gravity = Gravity.BOTTOM;
            getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        initView();
    }


    @SuppressLint("ResourceType")
    private void initView() {
        mTitle = findViewById(R.id.dialog_sex_title);
        mLLMan = findViewById(R.id.ll_man);
        mLLWoman = findViewById(R.id.ll_woman);

        mLLMan.setOnClickListener(this);
        mLLWoman.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mLLMan) {
            mCheckedSexListener.checked("男", 1);
        } else if (view == mLLWoman) {
            mCheckedSexListener.checked("女", 2);
        }
        this.cancel();
    }

    public interface CheckedSexListener {
        void checked(String sex, int tag);
    }
}
