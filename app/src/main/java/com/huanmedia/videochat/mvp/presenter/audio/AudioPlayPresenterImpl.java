package com.huanmedia.videochat.mvp.presenter.audio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.view.audio.IAudioPlayView;
import com.umeng.commonsdk.debug.E;

import java.io.IOException;

import io.reactivex.disposables.Disposable;

public class AudioPlayPresenterImpl extends BaseMVPPresenter<IAudioPlayView, BaseMVPModel>
        implements IAudioPlayPresenter, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private MediaPlayer mediaPlayer;
    private Disposable mCountDown;
    private int mTotalDuration;
    private boolean mHasPre = false;
    private String mResPath;

    public AudioPlayPresenterImpl(IAudioPlayView mMvpView) {
        super(mMvpView, BaseMVPModel.class);
    }

    /**
     * 获取总时长
     *
     * @return
     */
    @Override
    public int getPlayTotalTime() {
        return mTotalDuration;
    }


    @Override
    public MediaPlayer prePlay(String resPath) {
        mHasPre = false;
        mResPath=resPath;
        if (mediaPlayer != null)
            releasePlay();
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(resPath);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }

    @Override
    public MediaPlayer startPlay() {
        if (!mHasPre)
            return mediaPlayer;
        if (mCountDown != null && !mCountDown.isDisposed())
            mCountDown.dispose();
        mediaPlayer.seekTo(0);
        mCountDown = RxCountDown.countdown(mTotalDuration).subscribe(
                integer -> {
                    mMvpView.audioPlayDuration(mediaPlayer.getCurrentPosition() / 1000);
                });
        mediaPlayer.start();//开始播放
        return mediaPlayer;
    }

    @Override
    public void pausePlay() {
        if (!mHasPre)
            return;
        mediaPlayer.pause();
        if(mCountDown!=null)
            mCountDown.dispose();
    }



    @Override
    public void releasePlay() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        } catch (Exception e) {

        }
        if (mCountDown != null && !mCountDown.isDisposed())
            mCountDown.dispose();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        //  releasePlay();
        mMvpView.audioPlayComplete();
    }


    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mTotalDuration = mediaPlayer.getDuration() / 1000;
        mHasPre = true;
    }


}
