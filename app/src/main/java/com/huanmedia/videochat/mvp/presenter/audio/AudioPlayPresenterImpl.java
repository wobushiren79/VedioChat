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

public class AudioPlayPresenterImpl extends BaseMVPPresenter<IAudioPlayView, BaseMVPModel> implements IAudioPlayPresenter, MediaPlayer.OnCompletionListener {
    private MediaPlayer mediaPlayer;
    private Disposable mCountDown;

    public AudioPlayPresenterImpl(IAudioPlayView mMvpView) {
        super(mMvpView, BaseMVPModel.class);
    }

    @Override
    public MediaPlayer startPlay(String resPath) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(resPath);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.prepare(); //准备播放
            mediaPlayer.start();//开始播放
            int totalDuration = mediaPlayer.getDuration() / 1000;
            mCountDown = RxCountDown.countdown(totalDuration).subscribe(
                    integer -> {
                        mMvpView.audioPlayDuration(totalDuration - integer);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }

    @Override
    public void pausePlay() {
        if (mediaPlayer != null)
            mediaPlayer.pause();
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
        releasePlay();
        mMvpView.audioPlayComplete();
    }


}
