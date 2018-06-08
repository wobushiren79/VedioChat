package com.huanmedia.videochat.main2.weight;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.faceunity.FUManager;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.main2.datamodel.BeautyLevelDataHandler;
import com.huanmedia.videochat.main2.datamodel.BeautyLevleMode;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import mvp.data.store.glide.GlideUtils;

;

/**
 * Create by Administrator
 * time: 2018/1/26.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class BeautyDialog extends Dialog {
    private List<BeautyLevleMode> data;
    private RecyclerView mBeautyLevleRv;
    private BaseQuickAdapter<BeautyLevleMode.BeautyPageData, BaseViewHolder> mAdapter;
    private RadioGroup mReadioGroup;
    private TextView mTitleView;
    private BeautyLevleMode mCurrenterData;


    public BeautyDialog(@NonNull Context context) {
        super(context, R.style.customDialog_Gift);
    }

    private List<BeautyLevleMode> getData() {
        if (data == null) {
            data = BeautyLevelDataHandler.getData();
        }
        return data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_beauty);
        if (getWindow() != null) {
            getWindow().getAttributes().gravity = Gravity.BOTTOM;
            getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        initView();
    }

    private void initView() {
        mCurrenterData = getData().get(0);
        mTitleView = findViewById(R.id.dialog_beauty_tv_title);
        mTitleView.setText(Check.checkReplace(mCurrenterData.getTitle()));
        mBeautyLevleRv = findViewById(R.id.dialog_beauty_rv);
        mBeautyLevleRv.setItemAnimator(null);
        mReadioGroup = findViewById(R.id.dialog_beauty_rg);
        mReadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.dialog_beauty_rb_filter:
                    mCurrenterData=getData().get(0);
                    break;
                case R.id.dialog_beauty_rb_buffing:
                    mCurrenterData=getData().get(1);
                    break;
                case R.id.dialog_beauty_rb_whitening:
                    mCurrenterData=getData().get(2);
                    break;
                case R.id.dialog_beauty_rb_yad:
                    mCurrenterData=getData().get(3);
                    break;
                case R.id.dialog_beauty_rb_bgieye:
                    mCurrenterData=getData().get(4);
                    break;
            }
            mTitleView.setText(mCurrenterData.getTitle());
            mAdapter.setNewData(mCurrenterData.getBeautyPageData());
        });

        mAdapter = new BaseQuickAdapter<BeautyLevleMode.BeautyPageData, BaseViewHolder>(R.layout.dialog_item_beauty_filter, mCurrenterData.getBeautyPageData()) {
            @Override
            protected void convert(BaseViewHolder helper, BeautyLevleMode.BeautyPageData item) {
                boolean isChecked = mCurrenterData.getDefaultLevel() == helper.getAdapterPosition();
                switch (item.getType()) {
                    case 2://文本类型
                        helper.setChecked(R.id.dialog_beauty_tv_name, isChecked)
                                .setVisible(R.id.dialog_beauty_tv_name, true)
                                .setVisible(R.id.dialog_item_beauty_cl_filter_type, false);
                        if (helper.getAdapterPosition() == 0) {
                            helper.setText(R.id.dialog_beauty_tv_name, "")
                                    .setBackgroundRes(R.id.dialog_beauty_tv_name, R.drawable.dialog_beauty_item_enable);
                        } else {
                            helper.setText(R.id.dialog_beauty_tv_name, item.getName())
                                    .setBackgroundRes(R.id.dialog_beauty_tv_name, R.drawable.beauty_item_level_select);
                        }
                        break;
                    case 1://上面图片下面文本
                        RoundedImageView imageView = helper.getView(R.id.dialog_item_beauty_iv_filter_type);
                        imageView.setBorderColor(ContextCompat.getColor(mContext,
                                isChecked ? R.color.color_f65aa0 : R.color.transparent));
                        helper.setChecked(R.id.dialog_beauty_ctv_name, isChecked)
                                .setVisible(R.id.dialog_beauty_tv_name, false)
                                .setVisible(R.id.dialog_item_beauty_cl_filter_type, true)
                                .setText(R.id.dialog_beauty_ctv_name, item.getName());
                        GlideUtils.getInstance().loadBitmapNoAnim(mContext, Check.checkReplace(item.getUrl())
                                , imageView);
                        break;
                }
            }
        };
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (position!=mCurrenterData.getDefaultLevel()){
                mAdapter.notifyItemChanged(mCurrenterData.getDefaultLevel());
                mCurrenterData.setDefaultLevel(position);
                mAdapter.notifyItemChanged(position);
            }
            switch (mCurrenterData.getType()){
                case BeautyLevelDataHandler.LeveType.BUFFING:
                    FUManager.setBlurLevel(position);
                    break;
                case BeautyLevelDataHandler.LeveType.WHITENING:
                    FUManager.setColorLevel((position*2f)/10f);
                    break;
                case BeautyLevelDataHandler.LeveType.FILTER:
                    FUManager.setCurrentFilterByPosition(position);
                    break;
                case BeautyLevelDataHandler.LeveType.YAD:
                    FUManager.setCheekThinning((position*2f)/10f);
                    break;
                case BeautyLevelDataHandler.LeveType.BIGEYE:
                    FUManager.setEyeEnlarging((position*2f)/10f);
                    break;
            }
        });
        mAdapter.bindToRecyclerView(mBeautyLevleRv);
//        mBeautyLevleRv.addOnItemTouchListener(new OnItemClickListener() {
//            @Override
//            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if (position != mCurrenterData.getDefaultLevel()) {
//                    mAdapter.notifyItemChanged(mCurrenterData.getDefaultLevel());
//                    mCurrenterData.setDefaultLevel(position);
//                    mAdapter.notifyItemChanged(position);
//                }
//            }
//        });
    }
}
