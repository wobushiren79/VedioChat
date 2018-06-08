package com.huanmedia.videochat.common.widget.dialog.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huanmedia.ilibray.utils.ViewFindUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.adapters.AbstractWheelAdapter;

public class TimeWheelAdapter extends AbstractWheelAdapter {

    private @TimeType
    int mTimeType;
    private int mIntervalTime;
    private Context mContext;
    private List<String> mListData;
    private LayoutInflater inft;

    @Retention(RetentionPolicy.SOURCE)
    public @interface TimeType {
        int HOUR = 1, MIN = 2;
    }


    public TimeWheelAdapter(Context context, @TimeType int timeType, int intervalTime) {
        inft = LayoutInflater.from(context);
        mTimeType = timeType;
        mContext = context;
        mIntervalTime = intervalTime;
        initData();
    }

    public List<String> getListData() {
        return mListData;
    }

    /**
     * 获取目标数值的下标
     *
     * @param itemData
     * @return
     */
    public int getDefPosition(String itemData) {
        for (int i = 0; i < mListData.size(); i++) {
            if (itemData.equals(mListData.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private void initData() {
        mListData = new ArrayList<>();
        int totalSize = 0;
        switch (mTimeType) {
            case TimeType.HOUR:
                totalSize = 24;
                break;
            case TimeType.MIN:
                totalSize = 60;
                break;
        }
        timeHandler(mListData, totalSize);
    }

    private void timeHandler(List<String> mListData, int totalSize) {
        for (int i = 0; i < totalSize; i++) {
            if ((mIntervalTime != 0 && (i % mIntervalTime == 0)) || mIntervalTime == 0) {
                StringBuffer itemData = new StringBuffer();
                if (i < 10)
                    itemData.append("0");
                itemData.append(i + "");
                mListData.add(itemData.toString());
            }
        }
    }


    @Override
    public int getItemsCount() {
        return mListData.size();
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inft.inflate(android.R.layout.simple_list_item_1, null);
            ((TextView) convertView).setGravity(Gravity.CENTER);
        }
        TextView tvContent = ViewFindUtils.find(convertView, android.R.id.text1);
        tvContent.setText(mListData.get(index));
        return convertView;
    }
}
