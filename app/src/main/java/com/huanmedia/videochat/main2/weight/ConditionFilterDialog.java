package com.huanmedia.videochat.main2.weight;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.widget.bubbleseekbar.BubbleSeekBar;

/**
 * Create by Administrator
 * time: 2017/11/30.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class ConditionFilterDialog extends Dialog implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private final ConditionEntity mConditionEntity;
    private RadioButton mCheckFilter;
    private RadioButton mCheckAll;
    private RadioGroup mSexGroup;
    private Button mConfirm;
    private ConditionLisenter mConditionLisenter;
    private BubbleSeekBar mSeekBar;

    public ConditionFilterDialog setConditionLisenter(ConditionLisenter conditionLisenter) {
        mConditionLisenter = conditionLisenter;
        return this;
    }
    public ConditionFilterDialog(@NonNull Context context) {
        this(context,R.style.customDialog_upward);
    }

    public ConditionFilterDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mConditionEntity=new ConditionEntity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_conditionfilter);
        if (getWindow()!=null){
            getWindow().getAttributes().gravity= Gravity.BOTTOM;
            getWindow().getAttributes().width= ViewGroup.LayoutParams.MATCH_PARENT;
        }
        mConfirm = findViewById(R.id.dialog_condition_btn_confirm);
        mCheckFilter=findViewById(R.id.dialog_condition_rb_filter);
        mSexGroup=findViewById(R.id.dialog_condition_rg_sex);
        mCheckAll=findViewById(R.id.dialog_condition_rb_all);
        mCheckFilter.setOnCheckedChangeListener(this);
        mCheckAll.setOnCheckedChangeListener(this);
        mConfirm.setOnClickListener(this);
        mSeekBar =findViewById(R.id.dialog_condition_seebar);
        mSeekBar.setCustomSectionTextArray((sectionCount, array) -> {
            String[] strings = getContext().getResources().getStringArray(R.array.dialog_conditionfilter_ages);
            for (int i = 0; i <= sectionCount; i++) {
                array.append(i,strings[i]);
            }
            return array;
        });
        if ( mConditionEntity.getMatchConfig().getType()==ConditionEntity.ConditionTtype.ALL){
            mCheckAll.setChecked(true);
        }
        if ( mConditionEntity.getMatchConfig().getType()==ConditionEntity.ConditionTtype.FILTER){
            mCheckFilter.setChecked(true);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.dialog_condition_rb_filter:
                mCheckAll.setOnCheckedChangeListener(null);
                mCheckAll.setChecked(false);
                mCheckAll.setOnCheckedChangeListener(this);
                sexGroupEnable(true);
                mSeekBar.setEnabled(true);
                break;
            case R.id.dialog_condition_rb_all:
                sexGroupEnable(false);
                mSeekBar.setEnabled(false);
                mCheckFilter.setOnCheckedChangeListener(null);
                mCheckFilter.setChecked(false);
                mCheckFilter.setOnCheckedChangeListener(this);
                break;
        }
    }

    private void sexGroupEnable(boolean enable) {
        for (int i = 0; i < mSexGroup.getChildCount(); i++) {
            mSexGroup.getChildAt(i).setEnabled(enable);
        }
    }


    public void setSex(int checkedId) {
        switch (checkedId){
            case R.id.dialog_condition_rg_rb_wman:
                mConditionEntity.getMatchConfig().setSex(2);
                break;
            case R.id.dialog_condition_rg_rb_none:
                mConditionEntity.getMatchConfig().setSex(0);
                break;
            case R.id.dialog_condition_rg_rb_man:
                mConditionEntity.getMatchConfig().setSex(1);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (mConditionLisenter!=null){
            setSex(mSexGroup.getCheckedRadioButtonId());
            mConditionEntity.getMatchConfig().setType(mCheckFilter.isChecked()?ConditionEntity.ConditionTtype.FILTER:ConditionEntity.ConditionTtype.ALL);
            mConditionEntity.getMatchConfig().setAge(getRelAge());
            mConditionLisenter.resultData(mConditionEntity);
        }
        //确定
        dismiss();
    }

    public int getRelAge() {
        int age = 0;
        int currProgress = mSeekBar.getProgress();
        int unit = (int) Math.floor((mSeekBar.getMax() / mSeekBar.getSectionCount()));
        if (currProgress<unit){//16.66<16
            age=0;
        }else if (currProgress<unit*2){//33.33<33
            age=16;
        }else if (currProgress<unit*3){//
            age=20;
        }else if (currProgress<unit*4){
            age=25;
        }else if (currProgress<unit*5){
            age=30;
        }else if (currProgress >=unit*5){
            age=35;
        }
        return age;
    }

    public interface ConditionLisenter{
        void resultData(ConditionEntity conditionEntity);
    }
}
