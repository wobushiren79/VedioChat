package com.huanmedia.videochat.common.widget.dialog.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huanmedia.ilibray.utils.ViewFindUtils;

import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.adapters.AbstractWheelAdapter;

public class StringWheelAdapter extends AbstractWheelAdapter {

    private Context mContext;
    private List<String> mListData;
    private LayoutInflater inft;


    public StringWheelAdapter(Context context, List<String> listData) {
        inft = LayoutInflater.from(context);
        mContext = context;
        mListData = listData;
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

    public List<String> getListData() {
        return mListData;
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
