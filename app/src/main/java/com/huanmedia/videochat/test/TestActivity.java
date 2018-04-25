package com.huanmedia.videochat.test;


import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.faceunity.FUManager;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseVideoActivity;
import com.huanmedia.videochat.common.FApplication;
import com.huanmedia.videochat.video.CallingPresenter;

import io.agora.propeller.model.ConstantApp;
import io.agora.rtc.RtcEngine;

public class TestActivity extends BaseVideoActivity<CallingPresenter> {
    FrameLayout testLayout;
    Button textBt;
    SurfaceView surfaceV;

    public TestActivity() {

    }

    int s = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testLayout = findViewById(R.id.testlayout);
        textBt = findViewById(R.id.testBt);
        textBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FUManager.setCurrentFilterByPosition(s);
                s++;
                if (s > 5)
                    s = 0;
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }


    @Override
    protected void initUIandEvent() {
        int vProfile = ConstantApp.VIDEO_PROFILES[getVideoProfileIndex()];
        worker().configEngine(vProfile, null, null);
        worker().getRtcEngine().muteLocalVideoStream(true);//不发送本地视频数据
        surfaceV = RtcEngine.CreateRendererView(FApplication.getApplication());
        surfaceV.setZOrderOnTop(false);
        surfaceV.setZOrderMediaOverlay(false);
        testLayout.addView(surfaceV);
        worker().preview(true, surfaceV, 0);
    }

    @Override
    protected void deInitUIandEvent() {

    }

}
