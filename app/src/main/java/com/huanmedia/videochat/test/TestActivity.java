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
import com.huanmedia.videochat.video.CallingPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.agora.propeller.model.ConstantApp;
import io.agora.rtc.RtcEngine;

public class TestActivity extends BaseActivity {

    @BindView(R.id.bt_test_1)
    Button mMediaPlay;
    @BindView(R.id.bt_test_2)
    Button mMediaRecord;
    @BindView(R.id.bt_test_3)
    Button mMediaUpload;


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

    @OnClick({R.id.bt_test_1, R.id.bt_test_2, R.id.bt_test_3})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.bt_test_1:
                ArrayList<String> listData = new ArrayList<>();
                listData.add("http://vali.cp31.ott.cibntv.net/youku/65738C3469B3D718409C351C6/03000801005AEE8D1286B6003E88038DD8CA5B-E149-8D0D-E2C9-39D65C8ADB9E.mp4?sid=052582806071417144099_00_Afbd705a07accf4cf85f8621433817080&sign=ceb6313720657e3ca0c1b4ad4fe5ba01&ctype=50&hd=1");
                listData.add("http://vali.cp31.ott.cibntv.net/youku/67715AC27C932713CE5CF2164/03000801005AE9946D9E0F003E880362843D4D-3E46-9962-392D-534460995B09.mp4?sid=052582745690019002087_00_Ada9a1680f030140d2b3cc5d0dfc27b23&sign=262fd6ca652248ecb96857b9cf77dbe7&ctype=50&hd=1");
                listData.add("http://vali.cp31.ott.cibntv.net/youku/69755ba87e13e718a81575a05/03000801005AEFC867A0AE003E88033473ABA0-849D-20EB-AB48-560E5D308B98.mp4?sid=052582727200010002675_00_A3f733e2a7e8b7b2734e4825f861338ea&sign=a47c8325e83612423222ffc3bfa669bc&ctype=50");
                listData.add("http://vali.cp31.ott.cibntv.net/youku/67724F24C193E718A81642D4C/03000801005AEFFFA02BA5003E8803FD9A7EFC-9AE0-D363-625D-DFACF85AFA9F.mp4?sid=052582806078017808476_00_Ab9b003637ab8f666a461872f6b01f5c3&sign=714f6d0d26bbc7f3a75369847900136b&ctype=50&hd=1");
                getNavigator().navtoMediaPlay(this, listData);
                break;
            case R.id.bt_test_2:
                break;
            case R.id.bt_test_3:
                break;
        }
    }
}
