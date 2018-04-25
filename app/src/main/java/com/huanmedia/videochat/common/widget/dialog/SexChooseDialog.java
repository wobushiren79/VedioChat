package com.huanmedia.videochat.common.widget.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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

public class SexChooseDialog extends Dialog {
    private View mRootView;
    private Unbinder mBind;
    private TextView mTitle;
    private RadioGroup mRaidoGroup;
    private CheckedSexListener mCheckedSexListener;
    private Integer mDefault;
    private RadioButton radioMan;
    private RadioButton radioWMan;

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

    @Override
    public void dismiss() {
        if (mCheckedSexListener != null) {
            RadioButton radioButton = mRaidoGroup.findViewById(mRaidoGroup.getCheckedRadioButtonId());
            if (radioButton != null)
                if (radioButton == radioMan) {
                    mCheckedSexListener.checked("男", Integer.parseInt(radioButton.getTag().toString()));
                } else {
                    mCheckedSexListener.checked("女", Integer.parseInt(radioButton.getTag().toString()));
                }
        }
        super.dismiss();

    }

    @SuppressLint("ResourceType")
    private void initView() {
        mTitle = findViewById(R.id.dialog_sex_title);
        mRaidoGroup = findViewById(R.id.dialog_sex_radioGroup);
        if (mDefault != null) {
            if (mDefault == 1) {
                mRaidoGroup.check(R.id.dialog_radioMan);
            }
            if (mDefault == 2) {
                mRaidoGroup.check(R.id.dialog_radioWMan);
            }
        }
        radioMan = findViewById(R.id.dialog_radioMan);
        radioWMan = findViewById(R.id.dialog_radioWMan);
        mRaidoGroup.setOnCheckedChangeListener((group, checkedId) -> {
            dismiss();
        });
    }

    public interface CheckedSexListener {
        void checked(String sex, int tag);
    }
}
