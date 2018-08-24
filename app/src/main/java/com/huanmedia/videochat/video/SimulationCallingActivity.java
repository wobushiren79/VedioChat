package com.huanmedia.videochat.video;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.BaseVideoActivity;
import com.huanmedia.videochat.common.FApplication;
import com.huanmedia.videochat.common.widget.video.EmptyVideoPlayer;
import com.huanmedia.videochat.repository.net.NetConstants;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;

import butterknife.BindView;
import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;

public class SimulationCallingActivity extends BaseVideoActivity implements View.OnClickListener {

    private SurfaceView surfaceV;
    @BindView(R.id.view_self)
    FrameLayout mFLSelf;
    @BindView(R.id.view_simulation)
    EmptyVideoPlayer mSimulationView;
    @BindView(R.id.iv_exit)
    ImageView mIVExit;

    public static Intent getCallingIntent(Activity activity, String simulationVideo) {
        Intent intent = new Intent(activity, SimulationCallingActivity.class);
        intent.putExtra("simulationVideo", simulationVideo);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simulation_calling;
    }

    @Override
    protected void initData() {
        super.initData();
        mIVExit.setOnClickListener(this);
        String videoUrl = getIntent().getStringExtra("simulationVideo");
        mSimulationView.setUp(videoUrl, false, "");
        mSimulationView.setCanClick(false);
        mSimulationView.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
                finish();
            }
        });
        mSimulationView.startPlayLogic();
    }

    @Override
    protected void initUIandEvent() {
        //创建视频渲染视图, 设置远端视频视图
        String encryptionKey = NetConstants.S;
        String encryptionMode = "aes-128-xts";
        int vProfile = io.agora.rtc.Constants.VIDEO_PROFILE_360P_8;

        worker().configEngine(vProfile, encryptionKey, encryptionMode);
        worker().getRtcEngine().muteLocalVideoStream(false);
        worker().getRtcEngine().muteLocalAudioStream(false);

        surfaceV = RtcEngine.CreateRendererView(FApplication.getApplication());
        surfaceV.setZOrderOnTop(false);
        surfaceV.setZOrderMediaOverlay(false);
        mFLSelf.addView(surfaceV);
        worker().preview(true, surfaceV, 0);
    }

    @Override
    protected void deInitUIandEvent() {

    }

    @Override
    protected void enableProcess() {

    }

    @Override
    public void onClick(View view) {
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }
}
