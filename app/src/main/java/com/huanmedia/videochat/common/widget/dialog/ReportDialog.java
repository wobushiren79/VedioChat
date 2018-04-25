package com.huanmedia.videochat.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.huanmedia.videochat.R;

import java.util.Arrays;

/**
 * Create by Administrator
 * time: 2018/3/5.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class ReportDialog extends Dialog implements View.OnClickListener {
    private RecyclerView mRv;
    private Button mCancle,mConfirm;
    private ReportClickLisitener mReportClickLisitener;
    private SparseArray<String> mChooseMap;
    private BaseQuickAdapter<String, BaseViewHolder> mAdapter;

    public ReportDialog(@NonNull Context context) {
        this(context, R.style.customDialog);
    }

    public ReportDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_report);
        if (getWindow()!=null){
            getWindow().getAttributes().gravity= Gravity.CENTER;
            getWindow().getAttributes().width= ViewGroup.LayoutParams.MATCH_PARENT;
        }
        initView();
    }

    public ReportDialog setReportClickLisitener(ReportClickLisitener reportClickLisitener) {
        mReportClickLisitener = reportClickLisitener;
        return this;
    }

    private void initView() {
        mRv =findViewById(R.id.dialog_report_rv);
        mCancle=findViewById(R.id.dialog_report_btn_cancle);
        mConfirm=findViewById(R.id.dialog_report_btn_confirm);
        mCancle.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
//        mAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_list_item1);
//        mAdapter.addAll(getContext().getResources().getStringArray(R.array.report_type));
        mChooseMap = new SparseArray<>();
      mAdapter=  new BaseQuickAdapter<String,BaseViewHolder>(R.layout.simple_list_item1, Arrays.asList(getContext().getResources().getStringArray(R.array.report_type))) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(android.R.id.text1,item)
                        .setBackgroundRes(android.R.id.text1,mChooseMap.get(helper.getAdapterPosition())!=null?R.color.block_gray:R.color.transparent);
            }
        };
        mRv.setAdapter(mAdapter);
        mRv.setItemAnimator(null);
        mRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mChooseMap.size()>0){
                    int oldPosition = mChooseMap.keyAt(0);
                    mChooseMap.clear();
                    adapter.notifyItemChanged(oldPosition);
                }
                mChooseMap.put(position, (String) adapter.getItem(position));
                adapter.notifyItemChanged(position);
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (mReportClickLisitener!=null){
            switch (v.getId()){
                case R.id.dialog_report_btn_confirm:
                    if (mChooseMap.size()>0){
                        //type +1 type类型最小为1
                        mReportClickLisitener.confirm(mChooseMap.keyAt(0)+1,  mChooseMap.get(mChooseMap.keyAt(0)));
                    }else {
                        Toast.makeText(getContext(), "请选择一个类型", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                case R.id.dialog_report_btn_cancle:
                    break;
            }
        }
        dismiss();
    }

    public interface ReportClickLisitener{
         void confirm(int type,String item);
    }
}
