package com.huanmedia.videochat.common.widget.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huanmedia.ilibray.utils.ViewFindUtils;
import com.huanmedia.videochat.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import kankan.wheel.widget.adapters.AbstractWheelAdapter;

/**
 * 兴趣、家乡、行业
 */
public class WheelDateAdapter extends AbstractWheelAdapter {
    public static String DEFULTDATE = "1970-1-1";
    private final LayoutInflater inft;
    private final WheelDateType type;
    private int currentYear = 1970;
    private int currentMoth = 1;
    private ArrayList<Integer> datas = new ArrayList<>();
    private int MaxValue;
    private int MinValue;

    public ArrayList<Integer> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<Integer> datas) {
        this.datas = datas;
    }

    /**
     * 适配类型
     */
    public enum WheelDateType {
        YEAR, MONTH, DAY
    }

    public WheelDateAdapter(Context context, WheelDateType type) {
        inft = LayoutInflater.from(context);
        this.type = type;
    }

    public WheelDateAdapter(Context context, WheelDateType type, int Year, int Month) {
        inft = LayoutInflater.from(context);
        this.type = type;
        currentYear = Year;
        currentMoth = Month;
    }
//        public WheelDateAdapter(Context context,WheelDateType type,int Year) {
//            inft = LayoutInflater.from(context);
//            this.type=type;
//            currentYear = Year;
//        }
//        public WheelDateAdapter(Context context,WheelDateType type,int Year,int Month,int day) {
//            inft = LayoutInflater.from(context);
//            this.type=type;
//            currentYear = Year;
//            currentMoth=Month;
//        }

    public void createData() {
        Calendar cal = Calendar.getInstance();

        switch (type) {
            case YEAR:
                MaxValue = cal.get(Calendar.YEAR)+10;
                if (MinValue == 0)
                    MinValue = 1970;
                addData(MaxValue + 1 - MinValue);
                break;
            case MONTH:
                MaxValue = 12;
                if (MinValue == 0)
                    MinValue = 1;
                addData(MaxValue);
                break;
            case DAY:
                cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, currentYear);
                cal.set(Calendar.MONTH, currentMoth - 1);//7月
                MaxValue = cal.getActualMaximum(Calendar.DATE);
                if (MinValue == 0)
                    MinValue = 1;
                addData(MaxValue);
                break;
        }
    }

    public void setMinValue(int minValue) {
        MinValue = minValue;
    }

    private void addData(int addsize) {
        int yearvalue = MaxValue;
        for (int i = 0; i < addsize; i++) {
            datas.add(yearvalue);
            yearvalue -= 1;
        }
        Collections.reverse(datas);
    }


    @Override
    public int getItemsCount() {
        return datas.size();
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inft.inflate(R.layout.sample_wheel_item, null);
            ((TextView) convertView).setGravity(Gravity.CENTER);
        }
        TextView tv = ViewFindUtils.find(convertView, android.R.id.text1);
        tv.setText(datas.get(index) + "");
        return convertView;
    }
}