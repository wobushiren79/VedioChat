package com.huanmedia.videochat.common.widget.dialog;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huanmedia.ilibray.utils.ViewFindUtils;
import com.huanmedia.videochat.R;

import kankan.wheel.widget.adapters.AbstractWheelAdapter;

/**
 * 红人选项
 */
public class WheelReadmainOptionAdapter extends AbstractWheelAdapter {
    private  LayoutInflater inft;
    private String[] datas;

    public String[] getDatas() {
        return datas;
    }

    public void setDatas(String[] datas) {
        this.datas = datas;
    }


    public WheelReadmainOptionAdapter(Context context, String data[]) {
        inft = LayoutInflater.from(context);
        this.datas = data;
    }


    @Override
    public int getItemsCount() {
        return datas.length;
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inft.inflate(R.layout.sample_wheel_item, null);
            ((TextView) convertView).setGravity(Gravity.CENTER);
        }
        TextView tv = ViewFindUtils.find(convertView, android.R.id.text1);
        tv.setText(datas[index]);
        return convertView;
    }
}