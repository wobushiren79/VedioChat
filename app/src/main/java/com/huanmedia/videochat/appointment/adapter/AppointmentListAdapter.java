package com.huanmedia.videochat.appointment.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.applecoffee.devtools.base.adapter.BaseRCAdapter;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.mvp.entity.results.AppointmentDataResults;

import mvp.data.store.glide.GlideApp;
import mvp.data.store.glide.GlideUtils;

public class AppointmentListAdapter extends BaseRCAdapter<AppointmentDataResults> {

    public AppointmentListAdapter(Context context) {
        super(context, R.layout.item_appointment_style_1);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, AppointmentDataResults appointmentDataResults, int i) {
        TextView tvName = baseViewHolder.getView(R.id.tv_name);
        TextView tvTime = baseViewHolder.getView(R.id.tv_time);
        ImageView ivIcon = baseViewHolder.getView(R.id.iv_icon);

        String nameStr = appointmentDataResults.getNickname();
        String timeStr = appointmentDataResults.getDate() + " " + appointmentDataResults.getTime();
        String photoUrl = appointmentDataResults.getUserphoto_thumb();

        tvName.setText(nameStr);
        tvTime.setText(timeStr);
        GlideApp.with(mContext).asBitmap().load(photoUrl).into(ivIcon);
    }
}
