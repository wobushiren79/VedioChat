package com.huanmedia.videochat.appointment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;

import butterknife.BindView;
import mvp.data.store.glide.GlideApp;
import mvp.data.store.glide.GlideUtils;
import mvp.data.store.glide.transform.BlurTransformation;

public class AppointmentActivity extends BaseActivity {
    @BindView(R.id.iv_title_back)
    ImageView mIVTitleBack;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, AppointmentActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_appointment;
    }

    @Override
    protected void initView() {
        GlideApp.with(this).asBitmap()
                .load(R.drawable.icon_novice_guidance_match_2)
                .transform(new BlurTransformation(this, 23))// “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(mIVTitleBack);
    }

    @Override
    protected void initData() {
        super.initData();
    }
}
