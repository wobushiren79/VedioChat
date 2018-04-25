package com.huanmedia.videochat.common;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.orhanobut.logger.Logger;

import java.util.Arrays;

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


/**
 * Base {@link android.app.Activity} class for every Activity in this application.
 */
public abstract class BaseVideoActivity<P extends Presenter> extends BaseActivity {
    P mBasePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setPresenter();
        super.onCreate(savedInstanceState);
    }

    public P getBasePresenter() {
        return mBasePresenter;
    }


    @Override
    protected void onDestroy() {
        deInitData();
        super.onDestroy();
        if (getBasePresenter() != null) {//回收Presenter
            getBasePresenter().destroy();
        }
    }

    protected void deInitData() {
        deInitUIandEvent();
        deInitWorkerThread();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getBasePresenter() != null) {
            getBasePresenter().resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getBasePresenter() != null) {
            getBasePresenter().pause();
        }
    }

    @SuppressWarnings("unchecked")
    public void setPresenter() {

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
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        checkSelfPermissions();
    }

    protected abstract void initUIandEvent();

    protected abstract void deInitUIandEvent();

    protected boolean checkSelfPermissions() {
        return checkSelfPermission(Manifest.permission.READ_PHONE_STATE, ConstantApp.PERMISSION_REQ_ID_READ_PHONE_STATE) &&
                checkSelfPermission(Manifest.permission.RECORD_AUDIO, ConstantApp.PERMISSION_REQ_ID_RECORD_AUDIO) &&
                checkSelfPermission(Manifest.permission.CAMERA, ConstantApp.PERMISSION_REQ_ID_CAMERA) &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, ConstantApp.PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE);
    }

    public boolean checkSelfPermission(String permission, int requestCode) {
        Logger.d("checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
            return false;
        }

        if (Manifest.permission.CAMERA.equals(permission)) {
            initWorkThread();
        }
        return true;
    }

    protected void initWorkThread() {
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
        if (worker() != null)
            return worker().getEngineConfig();
        return null;
    }

    protected final MyEngineEventHandler event() {
        if (worker() != null)
            return worker().eventHandler();
        return null;
    }

    protected final void deInitWorkerThread() {
        WorkerThreadHandler.getInstance().unquote(this.getClass());
        if (WorkerThreadHandler.getInstance().getQuote() == 0) {
            WorkerThreadHandler.getInstance().deInitWorkerThread();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        Logger.d("onRequestPermissionsResult " + requestCode + " " + Arrays.toString(permissions) + " " + Arrays.toString(grantResults));
        switch (requestCode) {
            case ConstantApp.PERMISSION_REQ_ID_READ_PHONE_STATE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.RECORD_AUDIO, ConstantApp.PERMISSION_REQ_ID_RECORD_AUDIO);
                } else {
                    finish();
                }
                break;
            }
            case ConstantApp.PERMISSION_REQ_ID_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.CAMERA, ConstantApp.PERMISSION_REQ_ID_CAMERA);
                } else {
                    finish();
                }
                break;
            }
            case ConstantApp.PERMISSION_REQ_ID_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, ConstantApp.PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE);
                    initWorkThread();
                } else {
                    finish();
                }
                break;
            }
            case ConstantApp.PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    checkSelfPermission(Manifest.permission.READ_PHONE_STATE,ConstantApp.PERMISSION_REQ_ID_READ_PHONE_STATE);
                } else {
                    finish();
                }
                break;
            }
        }
    }

    //获取级保存视频配置选项
    protected int getVideoProfileIndex() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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
