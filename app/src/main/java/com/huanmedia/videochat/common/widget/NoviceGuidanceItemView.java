package com.huanmedia.videochat.common.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.applecoffee.devtools.base.layout.BaseFrameLayout;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import mvp.data.store.glide.GlideApp;

public class NoviceGuidanceItemView extends BaseFrameLayout implements View.OnClickListener {
    private ImageView mIVContent;
    private View mViewRead;
    private TextView mViewAppointment;
    private CallBack mCallBack;
    @GuidanceType
    int mGuidanceType = 0;

    @Override
    public void onClick(View view) {
        if (view == mViewAppointment) {
            ((BaseActivity)getContext()).getNavigator().navtoSimulationCalling((Activity)getContext(),
                    "http://116.211.155.77/65724C6CA983D8331EDABD4652/03000A01005B831BD85C81145665C75CAF5AFB-BBA2-47FE-9317-071EF78FE045.mp4?ccode=0519&duration=51&expire=18000&psid=e35f97e33b5eeb60a81ab9e2419022f5&sp=&ups_client_netip=b6941f97&ups_ts=1535334811&ups_userid=&utid=LDJTE36tDz0CAX1FNfW%2Fkj9q&vid=XMzc5OTEwNjg2MA%3D%3D&vkey=B768c541278ab8d507409afb1b15ed073");
        }
        if (mCallBack != null)
            mCallBack.Next();
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface GuidanceType {
        int Normal = 0, Read = 1, Appointment = 2;
    }

    public NoviceGuidanceItemView(Context context) {
        super(context);
    }

    public NoviceGuidanceItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView() {
        mIVContent = getView(R.id.iv_content);
        mViewRead = getView(R.id.view_read);
        mViewAppointment = getView(R.id.view_appointment);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.item_novice_guidance;
    }

    public ImageView getImage() {
        return mIVContent;
    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    public void setData(int imageRes, @GuidanceType int guidanceType) {
        GlideApp.with(getContext()).asBitmap().load(imageRes).into(mIVContent);
        mViewRead.setVisibility(GONE);
        mViewAppointment.clearAnimation();
        mViewAppointment.setVisibility(GONE);

        if (guidanceType == GuidanceType.Normal) {
            mIVContent.setOnClickListener(this);
            mViewRead.setOnClickListener(null);
            mViewAppointment.setOnClickListener(null);
        } else if (guidanceType == GuidanceType.Read) {
            mIVContent.setOnClickListener(null);
            mViewRead.setOnClickListener(this);
            mViewAppointment.setOnClickListener(null);
            mViewRead.setVisibility(VISIBLE);
        } else if (guidanceType == GuidanceType.Appointment) {
            mIVContent.setOnClickListener(null);
            mViewRead.setOnClickListener(null);
            mViewAppointment.setOnClickListener(this);
            mViewAppointment.setVisibility(VISIBLE);
            appointmentAnim(mViewAppointment);
        }
    }


    private void appointmentAnim(View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.2f, 1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        view.startAnimation(scaleAnimation);
    }

    public interface CallBack {
        void Next();
    }
}
