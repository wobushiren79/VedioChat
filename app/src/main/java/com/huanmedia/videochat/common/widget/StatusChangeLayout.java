package com.huanmedia.videochat.common.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseLinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatusChangeLayout extends BaseLinearLayout {
    LinearLayout mLLLayout;
    Map<Integer, String> mMapStatus;
    List<View> mListStatusItem;

    public StatusChangeLayout(Context context) {
        this(context, null);
    }

    public StatusChangeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setStatusMap(Map<Integer, String> mapStatus) {
        this.mMapStatus = mapStatus;
        mListStatusItem = new ArrayList<>();
        mLLLayout.removeAllViews();
        if (mapStatus != null)
            for (Map.Entry<Integer, String> entry : mapStatus.entrySet()) {
                int key = entry.getKey();
                String value = entry.getValue();
                createStatusItem(key, value);
            }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_status_change;
    }

    @Override
    protected void initView(View baseView) {
        mLLLayout = getView(R.id.ll_layout);
        LayoutParams layoutParams = new LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLLLayout.setLayoutParams(layoutParams);
    }

    @Override
    protected void initData() {

    }

    private void createStatusItem(int status, String statusName) {
        View mView = View.inflate(getContext(), R.layout.layout_status_change_item, null);
        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        mView.setLayoutParams(layoutParams);

        TextView tvStatusName = mView.findViewById(R.id.tv_status_name);
        tvStatusName.setText(statusName);
        mView.setTag(status);
        mListStatusItem.add(mView);
        mLLLayout.addView(mView);
    }

    public void setStatus(int status) {
        for (int i = 0; i < mListStatusItem.size(); i++) {
            View mView = mListStatusItem.get(i);
            ImageView leftLine = mView.findViewById(R.id.left_line);
            ImageView rightLine = mView.findViewById(R.id.right_line);
            ImageView ivMark = mView.findViewById(R.id.iv_mark);
            int tagStatus = (int) mView.getTag();
            if (tagStatus < status) {
                ivMark.setVisibility(VISIBLE);
                ivMark.setImageResource(R.drawable.base_bg_round_theme);
                leftLine.setImageResource(R.color.color_f65aa0);
                rightLine.setImageResource(R.color.color_f65aa0);
            } else if (tagStatus > status) {
                ivMark.setVisibility(GONE);
                leftLine.setImageResource(R.color.color_e8ebf1);
                rightLine.setImageResource(R.color.color_e8ebf1);
            } else {
                ivMark.setVisibility(VISIBLE);
                ivMark.setImageResource(R.drawable.base_bg_round_theme_3);
                leftLine.setImageResource(R.color.color_f65aa0);
                rightLine.setImageResource(R.color.color_e8ebf1);
            }
        }
    }
}

