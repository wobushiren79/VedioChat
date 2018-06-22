package com.huanmedia.videochat.test;


import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.faceunity.FUManager;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.BaseVideoActivity;
import com.huanmedia.videochat.common.FApplication;
import com.huanmedia.videochat.common.navigation.Navigator;
import com.huanmedia.videochat.common.widget.dialog.MainHintDialog;
import com.huanmedia.videochat.video.CallingPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.agora.propeller.model.ConstantApp;
import io.agora.rtc.RtcEngine;

public class TestActivity extends BaseActivity {


    public TestActivity() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }


    public void testOnClick(View view) {
        MainHintDialog dialog = new MainHintDialog(this, MainHintDialog.MainHintType.Coin);
        dialog.setContentText("恭喜您获得300钻");
        dialog.show();
    }
}
