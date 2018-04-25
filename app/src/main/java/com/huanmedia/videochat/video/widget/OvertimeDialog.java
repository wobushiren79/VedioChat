package com.huanmedia.videochat.video.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.ilibray.utils.TextViewDrawableUtils;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.manager.UserManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mvp.data.store.DataKeeper;

/**
 * Create by Administrator
 * time: 2017/12/9.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class OvertimeDialog extends Dialog implements View.OnClickListener {
    private final List<OvertimeMode> mOvertimeModes;
    private TextView mTitleTv;
    private RecyclerView mTimesRv;
    private TextView mCoinTv;
    private Button mBtnOk;
    private TextView mPayTv;
    private OvertimeListener mOvertimeListener;
    private OvertimeMode mCurrentItem;
    private DataKeeper mDatakeep;
    private final int mMaxFreeTimes= 3;

    public OvertimeDialog(@NonNull Context context, List<OvertimeMode> overtimeModes) {
        this(context, 0, overtimeModes);
    }

    public OvertimeDialog(@NonNull Context context, int themeResId, List<OvertimeMode> overtimeModes) {
        super(context, R.style.customDialog_upward);
        if (overtimeModes==null){
            overtimeModes =createDefaultData();
        }
        this.mOvertimeModes = overtimeModes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_overtime);
        if (getWindow() != null) {
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.gravity = Gravity.BOTTOM;
            attr.width = ViewGroup.LayoutParams.MATCH_PARENT;
            attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        initView();
    }
    private List<OvertimeMode> createDefaultData(){
        int times=0;
        mDatakeep = new DataKeeper(getContext(), DataKeeper.DEFULTFILE);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat fomrat = new SimpleDateFormat("yyyy/MM/dd");
        if (mDatakeep.get("overtime_date", (String) null)==null){
            String tayday = fomrat.format(Calendar.getInstance().getTime());
            mDatakeep.put("overtime_date", tayday);
            mDatakeep.putInt("overtime_times",mMaxFreeTimes);
            times = mMaxFreeTimes;
        }else {
            String timestr = mDatakeep.get("overtime_date","");
                String tayday = fomrat.format(Calendar.getInstance().getTime());
                if (timestr.equals(tayday)){
                    times = mDatakeep.getInt("overtime_times",0);
                }else {
                    mDatakeep.put("overtime_date", tayday);
                    mDatakeep.putInt("overtime_times",mMaxFreeTimes);
                    times = 3;
                }
        }

        OvertimeMode mode1 = new OvertimeMode(R.drawable.icon10s,"免费",0,times);
        mode1.setType(1);

        OvertimeMode mode2 = new OvertimeMode(R.drawable.icon30s,"10钻",1,-1);
        OvertimeMode mode3 = new OvertimeMode(R.drawable.icon1fz,"25钻",3,-1);
        OvertimeMode mode4 = new OvertimeMode(R.drawable.icon_10fz,"90钻",10,-1);
        List<OvertimeMode> overtimeModes = new ArrayList<>();
        overtimeModes.add(mode1);
        overtimeModes.add(mode2);
        overtimeModes.add(mode3);
        overtimeModes.add(mode4);
        return overtimeModes;
    }
    private void initView() {
        mTitleTv = findViewById(R.id.dialog_overtime_tv_title);
        mTimesRv = findViewById(R.id.dialog_overtime_rv_times);
        mCoinTv = findViewById(R.id.dialog_overtime_tv_coin);
        mBtnOk = findViewById(R.id.dialog_overtime_btn_ok);
        mPayTv = findViewById(R.id.dialog_overtime_tv_pay);
        mBtnOk.setOnClickListener(this);
        mPayTv.setOnClickListener(this);
        if (UserManager.getInstance().islogin() && UserManager.getInstance().getCurrentUser().getUserinfo()!=null){
            mCoinTv.setText(String.valueOf(UserManager.getInstance().getCurrentUser().getUserinfo().getCoin()));
        }
        if (mOvertimeModes != null && mOvertimeModes.size() > 0) {
            mTimesRv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
            int defaulValue = (DisplayUtil.getDisplayWidth(getContext()) - DisplayUtil.dip2px(getContext(), 40)) / 4;
            mTimesRv.getLayoutParams().height=defaulValue;
            mTimesRv.setAdapter(new BaseQuickAdapter<OvertimeMode, BaseViewHolder>(R.layout.dialog_item_overtime, mOvertimeModes) {
                @SuppressWarnings("SuspiciousNameCombination")
                @Override
                public BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
                    BaseViewHolder holder =super.onCreateDefViewHolder(parent, viewType);
                    FrameLayout mFL = holder.getView(R.id.dialog_item_overtime_fl);
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) mFL.getLayoutParams();
                    layoutParams.width=defaulValue;
                    layoutParams.height=layoutParams.width;
                    mFL.setLayoutParams(layoutParams);
                    return holder;
                }

                @Override
                protected void convert(BaseViewHolder helper, OvertimeMode item) {
                    TextView tv = helper.getView(R.id.dialog_item_overtime_tv);
                    String timesStr = "";
                    if (item.getTimes()>-1){
                        timesStr = "(" + item.getTimes() + ")";
                    }
                    tv.setText(String.format("%s%s", Check.checkReplace(item.getHint()), timesStr));
                    if (item.isSelect()){
                        helper.setBackgroundRes(R.id.dialog_item_overtime_fl,R.drawable.dialog_overtime_selectbg_stroke);
                    }else {
                        helper.setBackgroundRes(R.id.dialog_item_overtime_fl,0);
                    }
                    TextViewDrawableUtils.setDrawable(tv, null, ContextCompat.getDrawable(mContext, item.getSrc()), null, null);
                    helper.itemView.setOnClickListener(v -> {
                        if (mCurrentItem!=null){
                            int oldItemId = getData().indexOf(mCurrentItem);
                            getData().get(oldItemId).setSelect(false);
                            notifyItemChanged(oldItemId);
                        }
                        int mcposition = helper.getAdapterPosition();
                        mCurrentItem = (OvertimeMode) getItem(mcposition);
                        mCurrentItem.setSelect(true);
                        notifyItemChanged(mcposition);
                        mTimesRv.getLayoutManager().scrollToPosition(mcposition);
                    });
                }

            });
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_overtime_btn_ok:
                dismiss();
                if (mCurrentItem==null){
                    Toast.makeText(getContext(), "请选择一个加时选项", Toast.LENGTH_SHORT).show();
                    return;
                };
                if (mCurrentItem.getType()==1){
                    if (mCurrentItem.getTimes()==0){
                        Toast.makeText(getContext(), "每天最多三次免费加时", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mCurrentItem.setTimes((mCurrentItem.getTimes()-1)<0?0:(mCurrentItem.getTimes()-1));
                    if (mTimesRv.getAdapter()!=null){
                        BaseQuickAdapter baseQuickAdapter= (BaseQuickAdapter) mTimesRv.getAdapter();
                        baseQuickAdapter.notifyItemChanged(baseQuickAdapter.getData().indexOf(mCurrentItem));
                    }
                    mDatakeep.putInt("overtime_times",mDatakeep.getInt("overtime_times",1)-1);
                }
                //确定
                if (mOvertimeListener != null){
                    mOvertimeListener.okBtn(mCurrentItem);
                }

                break;
            case R.id.dialog_overtime_tv_pay:
                //充值
                dismiss();
                if (mOvertimeListener != null){
                    mOvertimeListener.pay(mCurrentItem);
                }
                break;
        }
    }


    public void setOvertimeListener(OvertimeListener overtimeListener) {
        mOvertimeListener = overtimeListener;
    }

    public interface OvertimeListener{
        /**
         * 充值回调
         * @param overtimeMode
         */
        void pay(@Nullable OvertimeMode overtimeMode);

        void okBtn( @Nullable OvertimeMode overtimeMode);
    }

}
