package com.huanmedia.videochat.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.widget.dialog.adapter.CitysSubWheelAdapter;
import com.huanmedia.videochat.common.widget.dialog.adapter.CitysWheelAdapter;
import com.huanmedia.videochat.common.widget.dialog.adapter.OccupationWheelAdapter;
import com.huanmedia.videochat.common.widget.dialog.adapter.StringWheelAdapter;
import com.huanmedia.videochat.common.widget.dialog.adapter.TimeQuantumWheelAdapter;
import com.huanmedia.videochat.common.widget.dialog.adapter.TimeWheelAdapter;
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
    private OnDatePickSelectListener datelistener;
    private ReadmainListener mReadmainListener;
    private OnTimePickSelectListener mOnTimeSelectListener;
    private OnStrPickSelectListener mOnStrSelectListener;
    private OnOccupationSelectListener mOnOccupationSelectListener;
    private OnTimeQuantumListener mTimeQuantumListener;
    private OnAreaPickSelectListener mOnAreaPickSelectListener;

    public DialogPick(Context context) {
        this.mContext = context;
    }


    public void setDatelistener(OnDatePickSelectListener datelistener) {
        this.datelistener = datelistener;
    }

    public void setOnTimeQuantumListener(OnTimeQuantumListener timeQuantumListener) {
        this.mTimeQuantumListener = timeQuantumListener;
    }

    public void setOnStrPickSelectListener(OnStrPickSelectListener onStrSelectListener) {
        mOnStrSelectListener = onStrSelectListener;
    }

    public void setOnTimeSelectListener(OnTimePickSelectListener onTimeSelectListener) {
        mOnTimeSelectListener = onTimeSelectListener;
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

    public void showPickerTimeMoney(String[] data, String titleStr, String hintString, String currentPrice) {
        int currentSelect = 0;
        if (Check.isEmpty(currentPrice) || currentPrice.equals("0")) {
            currentPrice = data[0];
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i].equals(currentPrice)) {
                currentSelect = i;
                break;
            }
        }
        int VisibleItems = 4;
        View dialogview = LayoutInflater.from(mContext).inflate(R.layout.dialog_wheelview_readmain, null);
        WheelView moneyView = dialogview.findViewById(R.id.whell_dialog_1);
        TextView title = dialogview.findViewById(R.id.wheel_title);
        TextView hint = dialogview.findViewById(R.id.whell_tv);
        TextView okButton = dialogview.findViewById(R.id.wheel_btn_ok);
        moneyView.setViewAdapter(new WheelReadmainOptionAdapter(mContext, data));
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
                mReadmainListener.pickSelect(result_money, Integer.parseInt(result_money));
            }
        });
        dialog.show();
        title.setText(Check.checkReplace(titleStr));
        hint.setText(Check.checkReplace(hintString));
        setDialogLayout(dialog);
    }


    /**
     * 显示时间选择对话框
     *
     * @param date   eg:1900-1-1
     * @param format eg:%s-%s-%s
     */
    public void showPickerDate(String title, String date, String format) {
        if (Check.isEmpty(date) || date.equals("0")) {
            date = WheelDateAdapter.DEFULTDATE;
        }
        String[] dates = date.split("-");
        int VisibleItems = 4;
        View dialogview = LayoutInflater.from(mContext).inflate(R.layout.dialog_wheelview, null);
        WheelView year = dialogview.findViewById(R.id.whell_dialog_1);
        WheelView month = dialogview.findViewById(R.id.whell_dialog_2);
        WheelView day = dialogview.findViewById(R.id.whell_dialog_3);
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
                if (dayM > size) {
                    dayM = size;
                }
                day.setCurrentItem(dayM - 1);
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
                if (dayM > size) {
                    dayM = size;
                }
                day.setCurrentItem(dayM - 1);
            }
        });
        WheelDateAdapter yearAdapter = new WheelDateAdapter(mContext, WheelDateAdapter.WheelDateType.YEAR);
        WheelDateAdapter monthAdapter = new WheelDateAdapter(mContext, WheelDateAdapter.WheelDateType.MONTH);
        WheelDateAdapter dayAdapter = new WheelDateAdapter(mContext, WheelDateAdapter.WheelDateType.DAY, Integer.parseInt(dates[0]), Integer.parseInt(dates[1]));
        yearAdapter.createData();
        year.setViewAdapter(yearAdapter);
        monthAdapter.createData();
        month.setViewAdapter(monthAdapter);
        dayAdapter.createData();
        day.setViewAdapter(dayAdapter);
        year.setVisibleItems(VisibleItems);
        year.setCyclic(true);
        year.setCurrentItem(yearAdapter.getDefPosition(Integer.valueOf(dates[0])));
        month.setVisibleItems(VisibleItems);
        month.setCurrentItem(monthAdapter.getDefPosition(Integer.valueOf(dates[1])));
        month.setCyclic(true);
        day.setVisibleItems(VisibleItems);
        day.setCurrentItem(dayAdapter.getDefPosition(Integer.valueOf(dates[2])));
        day.setCyclic(true);
        AlertDialog dialog = new AlertDialog
                .Builder(mContext, R.style.customDialog_upward)
                .setView(dialogview)
                .create();
        dialog.setOnDismissListener(dialog1 -> {
            if (datelistener != null) {
                Integer yearN = ((WheelDateAdapter) year.getViewAdapter()).getDatas().get(year.getCurrentItem());
                Integer monthM = ((WheelDateAdapter) month.getViewAdapter()).getDatas().get(month.getCurrentItem());
                Integer dayM = ((WheelDateAdapter) day.getViewAdapter()).getDatas().get(day.getCurrentItem());
                String birth = String.format(format, yearN, monthM, dayM);
                datelistener.pickSelect(birth);
            }
        });
        dialog.show();
        TextView tvTitle = (TextView) dialogview.findViewById(R.id.wheel_title);
        tvTitle.setText(title);
        setDialogLayout(dialog);
    }

    /**
     * 月份过滤器，用于账户明细
     *
     * @param date   eg:1900-1-1
     * @param format eg:%s-%s
     */
    public void showDateFilter(String date, String format) {
        if (Check.isEmpty(date) || date.equals("0")) {
            date = "2018-1-1";
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
        year.setVisibleItems(VisibleItems);
        year.setCyclic(false);
        year.setCurrentItem(adapter.getDefPosition(Integer.valueOf(dates[0])));
        month.setVisibleItems(VisibleItems);
        month.setCurrentItem(monthAdapter.getDefPosition(Integer.valueOf(dates[1])));
        month.setCyclic(false);
        AlertDialog dialog = new AlertDialog
                .Builder(mContext, R.style.customDialog_upward)
                .setView(dialogview)
                .create();
        btn_ok.setOnClickListener(v -> {
            if (datelistener != null) {
                Integer yearN = ((WheelDateAdapter) year.getViewAdapter()).getDatas().get(year.getCurrentItem());
                Integer monthM = ((WheelDateAdapter) month.getViewAdapter()).getDatas().get(month.getCurrentItem());
                Integer dayM = 1;
                String birth = String.format(format, yearN, monthM);
                if (isFutureDate("%d-%d-%d", yearN, monthM, dayM)) {
                    Toast.makeText(mContext, "时间过滤条件应该在今天以前", Toast.LENGTH_SHORT).show();
                    return;
                }
                datelistener.pickSelect(birth);
            }
            dialog.dismiss();
        });
        dialog.show();
        setDialogLayout(dialog);
    }

    private boolean isFutureDate(String format, Integer yearN, Integer monthM, Integer dayM) {
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        SimpleDateFormat fromat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();//"yyyy/MM/dd HH:mm:ss"
        fromat.applyLocalizedPattern("yyyy-MM-dd");
        Date dateOfBirth = null;
        String birth = String.format(format, yearN, monthM, dayM);
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
            if (mOnOccupationSelectListener != null) {
                mOnOccupationSelectListener.pickSelect(entity, provinces.getCurrentItem());
            }
            dialog.dismiss();
        });
        dialogview.findViewById(R.id.whell_dialog_2).setVisibility(View.GONE);
        setDialogLayout(dialog);
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
        if (defaultSelect == null || defaultSelect.length < 2) {
            defaultSelect = new int[]{0, 0};
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
            if (mOnAreaPickSelectListener != null) {
                int[] choosePositions = new int[]{provinces.getCurrentItem(), county.getCurrentItem()};
                mOnAreaPickSelectListener.pickSelect(areaentity, choosePositions, chooseids);
            }
//                profileEditorTvHometown.setText(String.format("%s.%s", areaentity.getName(), countyentity.getName()));
//                profileEditorTvHometown.setTag(chooseids);
            dialog.dismiss();
        });
        setDialogLayout(dialog);
    }

    /**
     * 展示时间选择（精确到分钟）
     */
    public void showTimeSelectPickerDialog(String defData, int intervalHour, int intervalMin) {
        String[] data = defData.split(":");
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_time_wheelview, null);
        WheelView hourWheel = dialogView.findViewById(R.id.whell_dialog_1);
        WheelView minWheel = dialogView.findViewById(R.id.whell_dialog_2);

        TimeWheelAdapter dayWheelAdapter = new TimeWheelAdapter(mContext, TimeWheelAdapter.TimeType.HOUR, intervalHour);
        TimeWheelAdapter minWheelAdapter = new TimeWheelAdapter(mContext, TimeWheelAdapter.TimeType.MIN, intervalMin);
        hourWheel.setViewAdapter(dayWheelAdapter);
        minWheel.setViewAdapter(minWheelAdapter);

        if (data != null && data.length == 2) {
            int dayPosition = dayWheelAdapter.getDefPosition(data[0]);
            if (dayPosition != -1)
                hourWheel.setCurrentItem(dayPosition);

            int minPosition = minWheelAdapter.getDefPosition(data[1]);
            if (minPosition != -1)
                minWheel.setCurrentItem(minPosition);
        }
        AlertDialog dialog = new AlertDialog
                .Builder(mContext, R.style.customDialog_upward)
                .setView(dialogView)
                .create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (mOnTimeSelectListener != null) {
                    String hourStr = ((TimeWheelAdapter) hourWheel.getViewAdapter()).getListData().get(hourWheel.getCurrentItem());
                    String minStr = ((TimeWheelAdapter) minWheel.getViewAdapter()).getListData().get(minWheel.getCurrentItem());
                    mOnTimeSelectListener.pickSelect(hourStr + ":" + minStr);
                }
            }
        });
        dialog.show();
        setDialogLayout(dialog);
    }

    /**
     * 设置普通列表
     */
    public void showStringListPickerDialog(List<String> listData, String defData) {
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_string_wheelview, null);
        WheelView strWheel = dialogView.findViewById(R.id.whell_dialog_1);
        StringWheelAdapter adapter = new StringWheelAdapter(mContext, listData);
        strWheel.setViewAdapter(adapter);

        if (defData != null && defData.length() > 0) {
            int position = adapter.getDefPosition(defData);
            if (position != -1)
                strWheel.setCurrentItem(position);
        }
        AlertDialog dialog = new AlertDialog
                .Builder(mContext, R.style.customDialog_upward)
                .setView(dialogView)
                .create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (mOnStrSelectListener != null) {
                    mOnStrSelectListener.pickSelect(strWheel.getCurrentItem(), adapter.getListData().get(strWheel.getCurrentItem()));
                }
            }
        });
        dialog.show();
        setDialogLayout(dialog);
    }

    /**
     * 设置时间段选择
     */
    public void showTimeQuantumPickerDialog(String beginTime, String endTime) {
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_time_quantum_wheelview, null);
        WheelView beginWheel = dialogView.findViewById(R.id.whell_dialog_1);
        WheelView endWheel = dialogView.findViewById(R.id.whell_dialog_2);

        TimeQuantumWheelAdapter beginAdapter = new TimeQuantumWheelAdapter(mContext);
        TimeQuantumWheelAdapter endAdapter = new TimeQuantumWheelAdapter(mContext);

        beginWheel.setViewAdapter(beginAdapter);
        endWheel.setViewAdapter(endAdapter);

        if (beginTime != null && endTime != null) {
            int beginPosition = beginAdapter.getDefPosition(beginTime);
            if (beginPosition != -1)
                beginWheel.setCurrentItem(beginPosition);

            int endPosition = endAdapter.getDefPosition(endTime);
            if (endPosition != -1)
                endWheel.setCurrentItem(endPosition);
        }
        AlertDialog dialog = new AlertDialog
                .Builder(mContext, R.style.customDialog_upward)
                .setView(dialogView)
                .create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (mTimeQuantumListener != null) {
                    String beginTime = beginAdapter.getListData().get(beginWheel.getCurrentItem());
                    String endTime = endAdapter.getListData().get(endWheel.getCurrentItem());
                    if (Integer.valueOf(beginTime.substring(0, 2)) >= Integer.valueOf(endTime.substring(0, 2))) {
                        ToastUtils.showToastShort(mContext, "结束时间不能比开始时间早");
                        return;
                    }
                    mTimeQuantumListener.pickSelect
                            (beginTime, endTime);
                }
            }
        });
        dialog.show();
        setDialogLayout(dialog);
    }

    /**
     * 设置弹窗样式高度
     *
     * @param dialog
     */
    private void setDialogLayout(Dialog dialog) {
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
    }


    /**
     * 时间段选择
     */
    public interface OnTimeQuantumListener {
        void pickSelect(String beginTime, String endTime);
    }

    /**
     * 时间选择器监听
     */
    public interface OnTimePickSelectListener {
        void pickSelect(String obj);
    }

    /**
     * 数据选择器监听
     */
    public interface OnStrPickSelectListener {
        void pickSelect(int position, String obj);
    }

    /**
     * 用于日期选择
     */
    public interface OnDatePickSelectListener {
        void pickSelect(String obj);
    }

    /**
     * 红人时间选择器
     */
    public interface ReadmainListener {
        void pickSelect(String showStr, int money);
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
