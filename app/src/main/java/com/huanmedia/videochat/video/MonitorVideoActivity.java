package com.huanmedia.videochat.video;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.BaseVideoActivity;
import com.huanmedia.videochat.my.MonitorActivity;
import com.huanmedia.videochat.repository.net.NetConstants;

import butterknife.BindView;
import io.agora.propeller.model.AGEventHandler;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

public class MonitorVideoActivity extends BaseVideoActivity {

    @BindView(R.id.fl_layout_one)
    FrameLayout mFlLayoutOne;
    @BindView(R.id.fl_layout_two)
    FrameLayout mFlLayoutTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initUIandEvent() {
        //创建视频渲染视图, 设置远端视频视图
        String encryptionKey = NetConstants.S;
        String encryptionMode = "aes-128-xts";
        String channelName = getIntent().getStringExtra("channelName");
        int vProfile = io.agora.rtc.Constants.VIDEO_PROFILE_360P_8;

        worker().configEngine(vProfile, encryptionKey, encryptionMode);
        worker().getRtcEngine().muteLocalVideoStream(true);
        worker().getRtcEngine().muteLocalAudioStream(true);

        int uid1 = getIntent().getIntExtra("uid1", 0);
        SurfaceView remoteVideoViewOne = RtcEngine.CreateRendererView(getApplicationContext());
        worker().getRtcEngine().setupRemoteVideo(new VideoCanvas(remoteVideoViewOne, VideoCanvas.RENDER_MODE_HIDDEN, uid1));
        mFlLayoutOne.addView(remoteVideoViewOne);
        remoteVideoViewOne.setZOrderOnTop(false);
        remoteVideoViewOne.setZOrderMediaOverlay(true);

        int uid2 = getIntent().getIntExtra("uid2", 0);
        SurfaceView remoteVideoViewTwo = RtcEngine.CreateRendererView(getApplicationContext());
        worker().getRtcEngine().setupRemoteVideo(new VideoCanvas(remoteVideoViewTwo, VideoCanvas.RENDER_MODE_HIDDEN, uid2));
        mFlLayoutTwo.addView(remoteVideoViewTwo);
        remoteVideoViewTwo.setZOrderOnTop(true);
        remoteVideoViewTwo.setZOrderMediaOverlay(true);

        worker().joinChannel(channelName, config().mUid);
    }

    @Override
    protected void deInitUIandEvent() {
        String channelName = getIntent().getStringExtra("channelName");
        worker().leaveChannel(channelName);
    }

    @Override
    protected void enableProcess() {

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_monitor_video;
    }

    public static Intent getCallingIntent(Activity activity, String channelName, int uid1, int uid2) {
        Intent intent = new Intent(activity, MonitorVideoActivity.class);
        intent.putExtra("uid1", uid1);
        intent.putExtra("uid2", uid2);
        intent.putExtra("channelName", channelName);
        return intent;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();

    }
}
