package com.huanmedia.videochat.common.widget.dialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.widget.dialog.adapter.CitysSubWheelAdapter;
import com.huanmedia.videochat.common.widget.dialog.adapter.CitysWheelAdapter;
import com.huanmedia.videochat.common.widget.dialog.adapter.OccupationWheelAdapter;
import com.huanmedia.videochat.repository.entity.ItemMenuEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;

/**
 * Created by yb on 2016/6/16.
 */
public class DialogPick {

    private final Context mContext;
    private  OnDatePickSelectListener datelistener;
    private  ReadmainListener mReadmainListener;
    private  OnOccupationSelectListener mOnOccupationSelectListener;
    private  OnAreaPickSelectListener mOnAreaPickSelectListener;

    public DialogPick(Context context) {
        this.mContext =context;
    }


    public void setDatelistener(OnDatePickSelectListener datelistener) {
        this.datelistener = datelistener;
    }

    public void setOnOccupationSelectListener(OnOccupationSelectListener onOccupationSelectListener) {
        mOnOccupationSelectListener = onOccupationSelectListener;
    }

    public void setOnAreaPickSelectListener(OnAreaPickSelectListener onAreaPickSelectListener) {
        mOnAreaPickSelectListener = onAreaPickSelectListener;
    }

    public void setReadmainListener(ReadmainListener readmainListener) {
        mReadmainListener = readmainListener;
    }

    public void showPickerTimeMoney( String[] data,String titleStr,String hintString,String currentPrice) {
        int currentSelect=0;
        if (Check.isEmpty(currentPrice)|| currentPrice.equals("0")){
            currentPrice = data[0];
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i].equals(currentPrice)){
                currentSelect=i;
                break;
            }
        }


        int VisibleItems = 4;
        View dialogview = LayoutInflater.from(mContext).inflate(R.layout.dialog_wheelview_readmain, null);
        WheelView moneyView = dialogview.findViewById(R.id.whell_dialog_1);
        TextView title = dialogview.findViewById(R.id.wheel_title);
        TextView hint = dialogview.findViewById(R.id.whell_tv);
        TextView okButton = dialogview.findViewById(R.id.wheel_btn_ok);
        moneyView.setViewAdapter(new WheelReadmainOptionAdapter(mContext,data));
        //默认值
        moneyView.setVisibleItems(VisibleItems);
        moneyView.setCurrentItem(currentSelect);
        moneyView.setCyclic(false);
        AlertDialog dialog = new AlertDialog
                .Builder(mContext, R.style.customDialog_upward)
                .setView(dialogview)
                .create();
       okButton.setOnClickListener(v -> {
           dialog.dismiss();
           if (mReadmainListener != null) {
               String result_money = ((WheelReadmainOptionAdapter) moneyView.getViewAdapter()).getDatas()[moneyView.getCurrentItem()];
               mReadmainListener.pickSelect(result_money,Integer.parseInt(result_money));
           }
       });

        dialog.show();
        title.setText(Check.checkReplace(titleStr));
        hint.setText(Check.checkReplace(hintString));
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
    }




    /**
     * 显示时间选择对话框
     * @param date eg:1900-1-1
     * @param format eg:%s-%s-%s
     */
    public  void showPickerDate(String date, String format){
        if (Check.isEmpty(date)|| date.equals("0")){
            date= WheelDateAdapter.DEFULTDATE;
        }
        String[] dates = date.split("-");
        int VisibleItems = 4;
        View dialogview = LayoutInflater.from(mContext).inflate(R.layout.dialog_wheelview, null);
        WheelView year = (WheelView) dialogview.findViewById(R.id.whell_dialog_1);
        WheelView month = (WheelView) dialogview.findViewById(R.id.whell_dialog_2);
        WheelView day = (WheelView) dialogview.findViewById(R.id.whell_dialog_3);
//        Button btn_ok = (Button) dialogview.findViewById(R.id.wheel_ok);
        month.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }
            @Override
            public void onScrollingFinished(WheelView wheel) {
                Integer yearN = ((WheelDateAdapter) year.getViewAdapter()).getDatas().get(year.getCurrentItem());
                Integer monthM = ((WheelDateAdapter) wheel.getViewAdapter()).getDatas().get(wheel.getCurrentItem());
                Integer dayM = ((WheelDateAdapter) day.getViewAdapter()).getDatas().get(day.getCurrentItem());
                WheelDateAdapter adapter = new WheelDateAdapter(mContext, WheelDateAdapter.WheelDateType.DAY, yearN, monthM);
                adapter.createData();
                day.setViewAdapter(adapter);
                int size = ((WheelDateAdapter) day.getViewAdapter()).getDatas().size();
                if (dayM>size){
                    dayM=size;
                }
                day.setCurrentItem(dayM-1);
            }
        });
        year.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }
            @Override
            public void onScrollingFinished(WheelView wheel) {
                Integer yearN = ((WheelDateAdapter) wheel.getViewAdapter()).getDatas().get(wheel.getCurrentItem());
                Integer monthM = ((WheelDateAdapter) month.getViewAdapter()).getDatas().get(month.getCurrentItem());
                Integer dayM = ((WheelDateAdapter) day.getViewAdapter()).getDatas().get(day.getCurrentItem());
                WheelDateAdapter adapter = new WheelDateAdapter(mContext, WheelDateAdapter.WheelDateType.DAY, yearN, monthM);
                adapter.createData();
                day.setViewAdapter(adapter);
                int size = ((WheelDateAdapter) day.getViewAdapter()).getDatas().size();
                if (dayM>size){
                    dayM=size;
                }
                day.setCurrentItem(dayM-1);
            }
        });
        WheelDateAdapter yearAdapter = new WheelDateAdapter(mContext, WheelDateAdapter.WheelDateType.YEAR);
        WheelDateAdapter monthAdapter = new WheelDateAdapter(mContext, WheelDateAdapter.WheelDateType.MONTH);
        WheelDateAdapter dayAdapter =new WheelDateAdapter(mContext, WheelDateAdapter.WheelDateType.DAY, Integer.parseInt(dates[0]), Integer.parseInt(dates[1]));
        yearAdapter.createData();
        year.setViewAdapter(yearAdapter);
        monthAdapter.createData();
        month.setViewAdapter(monthAdapter);
        dayAdapter.createData();
        day.setViewAdapter(dayAdapter);
        //默认值
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        int curentCurentYears = currentYear - Integer.parseInt(dates[0]);
        int countyears = currentYear - 1970;
        year.setVisibleItems(VisibleItems);
        year.setCyclic(true);
        year.setCurrentItem(2002-10);
        month.setVisibleItems(VisibleItems);
        month.setCurrentItem(Integer.parseInt(dates[1])-1);
        month.setCyclic(true);
        day.setVisibleItems(VisibleItems);
        day.setCurrentItem(Integer.parseInt(dates[2])-1);
        day.setCyclic(true);
        AlertDialog dialog = new AlertDialog
                .Builder(mContext, R.style.customDialog_upward)
                .setView(dialogview)
                .create();
        dialog.setOnDismissListener(dialog1 -> {
            if (datelistener!=null){
                Integer yearN = ((WheelDateAdapter) year.getViewAdapter()).getDatas().get(year.getCurrentItem());
                Integer monthM = ((WheelDateAdapter) month.getViewAdapter()).getDatas().get(month.getCurrentItem());
                Integer dayM = ((WheelDateAdapter) day.getViewAdapter()).getDatas().get(day.getCurrentItem());
                String birth= String.format(format,yearN,monthM,dayM);
                if (isFutureDate(format,yearN,monthM,dayM)) {
                    Toast.makeText(mContext, "出生日期不能在未来", Toast.LENGTH_SHORT).show();
                    return;
                }
                datelistener.pickSelect(birth);
            }
        });
        dialog.show();
        TextView title = (TextView) dialogview.findViewById(R.id.wheel_title);
        title.setText(R.string.dialog_title_age);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics metrics = DisplayUtil.getDisplayMetrics(mContext);
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.gravity= Gravity.BOTTOM;
//        lp.windowAnimations=R.style.Windows_Anim_Upward;
        dialogWindow.setAttributes(lp);
    }
    /**
     * 月份过滤器，用于账户明细
     * @param date eg:1900-1-1
     * @param format eg:%s-%s
     */
    public  void showDateFilter(String date, String format){
        if (Check.isEmpty(date)|| date.equals("0")){
            date= "2018-1-1";
        }
        String[] dates = date.split("-");
        int VisibleItems = 4;
        View dialogview = LayoutInflater.from(mContext).inflate(R.layout.dialog_date_filter_wheelview, null);
        WheelView year = (WheelView) dialogview.findViewById(R.id.whell_dialog_1);
        WheelView month = (WheelView) dialogview.findViewById(R.id.whell_dialog_2);
        TextView btn_ok = (TextView) dialogview.findViewById(R.id.wheel_btn_ok);
        int minValue = 2018;
        WheelDateAdapter adapter = new WheelDateAdapter(mContext, WheelDateAdapter.WheelDateType.YEAR);
        adapter.setMinValue(minValue);
        adapter.createData();
        year.setViewAdapter(adapter);
        WheelDateAdapter monthAdapter = new WheelDateAdapter(mContext, WheelDateAdapter.WheelDateType.MONTH);
        monthAdapter.createData();
        month.setViewAdapter(monthAdapter);
        //默认值
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        int curentCurentYears = currentYear - Integer.parseInt(dates[0]);
        int countyears = currentYear - minValue;
        year.setVisibleItems(VisibleItems);
        year.setCyclic(false);
        year.setCurrentItem(countyears-curentCurentYears);
        month.setVisibleItems(VisibleItems);
        month.setCurrentItem(Integer.parseInt(dates[1])-1);
        month.setCyclic(false);
        AlertDialog dialog = new AlertDialog
                .Builder(mContext, R.style.customDialog_upward)
                .setView(dialogview)
                .create();
        btn_ok.setOnClickListener(v -> {
            if (datelistener!=null){
                Integer yearN = ((WheelDateAdapter) year.getViewAdapter()).getDatas().get(year.getCurrentItem());
                Integer monthM = ((WheelDateAdapter) month.getViewAdapter()).getDatas().get(month.getCurrentItem());
                Integer dayM = 1;
                String birth= String.format(format,yearN,monthM);
                if (isFutureDate("%d-%d-%d",yearN,monthM,dayM)) {
                    Toast.makeText(mContext, "时间过滤条件应该在今天以前", Toast.LENGTH_SHORT).show();
                    return;
                }
                datelistener.pickSelect(birth);
            }
            dialog.dismiss();
        });
        dialog.show();
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics metrics = DisplayUtil.getDisplayMetrics(mContext);
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.gravity= Gravity.BOTTOM;
//        lp.windowAnimations=R.style.Windows_Anim_Upward;
        dialogWindow.setAttributes(lp);
    }
    private boolean isFutureDate(String format, Integer yearN, Integer monthM , Integer dayM){
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        SimpleDateFormat fromat= (SimpleDateFormat) SimpleDateFormat.getDateInstance();//"yyyy/MM/dd HH:mm:ss"
        fromat.applyLocalizedPattern("yyyy-MM-dd");
        Date dateOfBirth= null;
        String birth= String.format(format,yearN,monthM,dayM);
        try {
            dateOfBirth = fromat.parse(birth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        now.setTime(new Date());
        born.setTime(dateOfBirth);
        return born.after(now);
    }
    /**
     * 显示行业对话框
     *
     * @param data
     */
    public void showMajorPickerDialog(List<ItemMenuEntity> data, int defaultSelect) {
        if (data == null) {
            return;
        }
        int VisibleItems = 4;
        View dialogview = LayoutInflater.from(mContext).inflate(R.layout.dialog_tow_ok_wheelview, null);
        WheelView provinces = (WheelView) dialogview.findViewById(R.id.whell_dialog_1);
        TextView btn_ok = (TextView) dialogview.findViewById(R.id.wheel_btn_ok);
        provinces.setVisibleItems(VisibleItems);//一页显示数量
        provinces.setViewAdapter(new OccupationWheelAdapter(mContext, data));
        provinces.setCurrentItem(defaultSelect);//默认选中1
        AlertDialog dialog = new AlertDialog
                .Builder(mContext, R.style.customDialog_upward)
                .setView(dialogview)
                .create();
        dialog.show();
        btn_ok.setOnClickListener(v -> {
            ItemMenuEntity entity = ((OccupationWheelAdapter) provinces.getViewAdapter()).getEntities().get(provinces.getCurrentItem());
            if (mOnOccupationSelectListener!=null){
                mOnOccupationSelectListener.pickSelect(entity,provinces.getCurrentItem());
            }
            dialog.dismiss();
        });
        dialogview.findViewById(R.id.whell_dialog_2).setVisibility(View.GONE);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.gravity= Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
    }

    /**
     * 显示地址选择对话框
     *
     * @param data
     */
    public void showAreaPickerDialog(List<ItemMenuEntity> data, int[] defaultSelect) {
        if (data == null) {
            return;
        }
        if (defaultSelect==null || defaultSelect.length<2){
            defaultSelect=new int[]{0,0};
        }
        int VisibleItems = 4;
        View dialogview = LayoutInflater.from(mContext).inflate(R.layout.dialog_tow_ok_wheelview, null);
        WheelView provinces = (WheelView) dialogview.findViewById(R.id.whell_dialog_1);
        WheelView county = (WheelView) dialogview.findViewById(R.id.whell_dialog_2);
        TextView btn_ok = (TextView) dialogview.findViewById(R.id.wheel_btn_ok);

        provinces.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                county.setViewAdapter(new CitysSubWheelAdapter(mContext, data.get(wheel.getCurrentItem()).getSub() == null
                        ? new ArrayList<>()
                        : data.get(wheel.getCurrentItem()).getSub())
                );
                county.setCurrentItem(0);
            }
        });
        provinces.setVisibleItems(VisibleItems);//一页显示数量
        county.setVisibleItems(VisibleItems);
        provinces.setViewAdapter(new CitysWheelAdapter(mContext, data));
        provinces.setCurrentItem(defaultSelect[0]);//默认选中3
        county.setViewAdapter(new CitysSubWheelAdapter(mContext, data.get(defaultSelect[0]).getSub()));//默认子列表
        county.setCurrentItem(defaultSelect[1]);
        AlertDialog dialog = new AlertDialog
                .Builder(mContext, R.style.customDialog_upward)
                .setView(dialogview)
                .create();
        dialog.show();
        btn_ok.setOnClickListener(v -> {
            ItemMenuEntity areaentity = ((CitysWheelAdapter) provinces.getViewAdapter()).getEntities().get(provinces.getCurrentItem());
            ItemMenuEntity.SubEntity countyentity = ((ItemMenuEntity) areaentity).getSub().get(county.getCurrentItem());
            int[] chooseids = new int[2];//将选择的ID保存到数组中
            chooseids[0] = areaentity.getId();
            chooseids[1] = countyentity.getId();
            if (mOnAreaPickSelectListener!=null){
                int[] choosePositions=new int[]{provinces.getCurrentItem(),county.getCurrentItem()};
                mOnAreaPickSelectListener.pickSelect(areaentity,choosePositions,chooseids);
            }
//                profileEditorTvHometown.setText(String.format("%s.%s", areaentity.getName(), countyentity.getName()));
//                profileEditorTvHometown.setTag(chooseids);
            dialog.dismiss();
        });
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.gravity= Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
    }

    /**
     * 用于时间选择
     */
    public interface OnDatePickSelectListener {
        void pickSelect(String obj);
    }

    /**
     * 红人时间选择器
     */
    public interface ReadmainListener {
        void pickSelect(String showStr,int money);
    }
    /**
     * 行业
     */
    public interface OnOccupationSelectListener {
        void pickSelect(ItemMenuEntity date, int position);
    }

    /**
     * 城市选择器
     */
    public interface OnAreaPickSelectListener {
        void pickSelect(ItemMenuEntity areaentity, int[] choosePositions, int[] chooseids);
    }
}
