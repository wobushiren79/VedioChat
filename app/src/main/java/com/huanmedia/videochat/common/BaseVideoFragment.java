package com.huanmedia.videochat.common;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;

import com.huanmedia.videochat.R;

import java.util.List;

import io.agora.propeller.model.ConstantApp;
import io.agora.propeller.model.CurrentUserSettings;
import io.agora.propeller.model.EngineConfig;
import io.agora.propeller.model.MyEngineEventHandler;
import io.agora.propeller.model.WorkerThread;
import io.agora.propeller.model.WorkerThreadHandler;
import io.agora.rtc.RtcEngine;
import mvp.presenter.Presenter;
import mvp.presenter.PresenterBulide;
import mvp.view.BaseView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * Base {@link android.app.Activity} class for every Activity in this application.
 */
public abstract class BaseVideoFragment<P extends Presenter> extends BaseFragment implements EasyPermissions.PermissionCallbacks {
    P mBasePresenter;
    private final int REQUEST_VIDEOFRAGMENT = 10000;
    private final int REQUEST_PHONESTATUS = 10001;

    public P getBasePresenter() {
        return mBasePresenter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setBasePresenter();
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressWarnings("unchecked")
    public void setBasePresenter() {
        try {
            mBasePresenter = PresenterBulide.newPresenterInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (mBasePresenter != null) {
                mBasePresenter.setView((BaseView) this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        if (mBasePresenter != null) {
            mBasePresenter.destroy();
        }
        super.onDetach();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        deInitUIandEvent();
        deInitWorkerThread();
    }

    @Override
    protected void onVisible() {
        super.onVisible();
    }

    @Override
    protected void onInvisible() {
        super.onInvisible();
        deInitUIandEvent();
//        if (isDestoryWorkThread()){
        deInitWorkerThread();
//        }
    }


//    protected boolean isDestoryWorkThread(){
//        return WorkerThreadHandler.getInstance().getQuote()==0;
//    }


    protected abstract void initUIandEvent();

    protected abstract void deInitUIandEvent();

    protected abstract void enableProcess();

    protected abstract  void backMatch();

    /**
     * String[] perms = {<br><br/>
     * Manifest.permission.READ_PHONE_STATE,<br><br/>
     * Manifest.permission.WRITE_EXTERNAL_STORAGE,<br><br/>
     * Manifest.permission.READ_EXTERNAL_STORAGE,<br><br/>
     * Manifest.permission.RECORD_AUDIO,<br><br/>
     * };
     *
     * @return
     */
    abstract protected String[] getReqPremiss();

    @AfterPermissionGranted(REQUEST_VIDEOFRAGMENT)
    public void openCamera() {
        String[] perms = getReqPremiss();
        if (perms == null) return;
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            initWorkThread();
        } else {
            checkCamera();
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_camera_write_read),
                    REQUEST_VIDEOFRAGMENT, perms);
        }
        enableProcess();
    }

    @AfterPermissionGranted(REQUEST_PHONESTATUS)
    public void checkPhonePermission() {
        String perms = Manifest.permission.READ_PHONE_STATE;
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            initAiYa();
            openCamera();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_camera_write_read),
                    REQUEST_PHONESTATUS, perms);
        }
        enableProcess();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        enableProcess();
    }

    protected abstract void initAiYa();

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        String[] premsArray = new String[perms.size()];
        perms.toArray(premsArray);
        switch (requestCode) {
            case REQUEST_PHONESTATUS:
                if (EasyPermissions.hasPermissions(getContext(), premsArray)) {
                    initAiYa();
                }
                break;
            case REQUEST_VIDEOFRAGMENT:
                if (EasyPermissions.hasPermissions(getContext(), premsArray)) {
                    openCamera();
                }
                break;
        }
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (EasyPermissions.hasPermissions(getContext(), premsArray)) {
                openCamera();
            }
            if (EasyPermissions.hasPermissions(getContext(), premsArray)) {
                initAiYa();
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(getActivity()).build().show();
        }
        backMatch();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            openCamera();
            if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.READ_PHONE_STATE)) {
                initAiYa();
            }
        }
        enableProcess();
    }

    public void checkCamera() {
        if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.CAMERA)) {
            initWorkThread();
        }
    }

    private void initWorkThread() {
        WorkerThreadHandler.getInstance().quote(this.getClass());
        WorkerThreadHandler.getInstance().initWorkerThread();
        initUIandEvent();
    }

    protected RtcEngine rtcEngine() {
        return worker().getRtcEngine();
    }

    protected final WorkerThread worker() {
        return WorkerThreadHandler.getInstance().getWorkerThread();
    }

    protected final EngineConfig config() {
        return worker().getEngineConfig();
    }

    protected final MyEngineEventHandler event() {
        return worker().eventHandler();
    }

    protected final void deInitWorkerThread() {
        WorkerThreadHandler.getInstance().unquote(this.getClass());
        if (WorkerThreadHandler.getInstance().getQuote() == 0) {
            WorkerThreadHandler.getInstance().deInitWorkerThread();
        }
    }

    //获取级保存视频配置选项
    protected int getVideoProfileIndex() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(FApplication.getApplication());
        int profileIndex = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_PROFILE_IDX, ConstantApp.DEFAULT_PROFILE_IDX);
        if (profileIndex > ConstantApp.VIDEO_PROFILES.length - 1) {
            profileIndex = ConstantApp.DEFAULT_PROFILE_IDX;

            // save the new value
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(ConstantApp.PrefManager.PREF_PROPERTY_PROFILE_IDX, profileIndex);
            editor.apply();
        }
        return profileIndex;
    }

    protected CurrentUserSettings vSettings() {
        return WorkerThreadHandler.mVideoSettings;
    }

}
