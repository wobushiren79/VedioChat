package com.huanmedia.videochat.common.service.notifserver.ringtone;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;

import com.huanmedia.videochat.common.FApplication;
import com.huanmedia.videochat.common.manager.ResourceManager;

/**
 * Create by Administrator
 * time: 2017/12/15.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class RingtoneManager {

    public static final String ACTION_DISMIS = "com.huanmedia.videochat.RingtoneManager.action_dismiss";
    public static final String ACTION_CONTENT = "com.huanmedia.videochat.RingtoneManager.action_content";
    public static final String ACTION_START_VIDEOCALL = "com.huanmedia.videochat.RingtoneManager.start_videocall";

    private static RingtoneManager Instance;
    private final Application mContext;
    private MediaPlayer mMediaPlayer;
    private Vibrator mVibrator;

    public RingtoneManager(Application application) {
        this.mContext = application;
    }

    public static RingtoneManager getInstance() {
        if (Instance == null) {
            synchronized (ResourceManager.class) {
                if (Instance == null) {
                    Instance = new RingtoneManager(FApplication.getApplication());
                }
            }
        }
        return Instance;
    }
    public void aleartSoundVibrate(AlertSoundAndVibrateHelper helper) {

        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager!=null){
//            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            playSound(mContext,helper);
        }
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        // 等待3秒，震动3秒，从第0个索引开始，一直循环new long[] {0, 100, 1000, 300, 200, 100, 500, 200, 100}
        if (mVibrator!=null){
            mVibrator.vibrate(helper.getVibrate(), 0);
        }
    }

    public void playSound(final Context context, AlertSoundAndVibrateHelper helper) {

        // 使用来电铃声的铃声路径
//        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        // 如果为空，才构造，不为空，说明之前有构造过
        try {
            if (mMediaPlayer == null)
                mMediaPlayer = new MediaPlayer();
            if (mMediaPlayer.isPlaying() || mMediaPlayer.isLooping()){
                mMediaPlayer.stop();
                mMediaPlayer.reset();
            }
            mMediaPlayer.setDataSource(context, helper.soundUri);
            mMediaPlayer.setLooping(true); //循环播放
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopSoundVibrate() {
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                // 要释放资源，不然会打开很多个MediaPlayer
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            if (mVibrator != null) {
                mVibrator.cancel();
                mVibrator = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
