package com.huanmedia.videochat.mvp.presenter.audio;

import android.media.MediaRecorder;
import android.util.Log;
import android.widget.Toast;

import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.entity.request.AudioInfoRequest;
import com.huanmedia.videochat.mvp.view.audio.IAudioRecordView;

import java.io.File;

import io.reactivex.disposables.Disposable;

public class AudioRecordPresenterImpl extends BaseMVPPresenter<IAudioRecordView, BaseMVPModel> implements IAudioRecordPresenter {

    private boolean isRecording;
    private MediaRecorder mediaRecorder;
    private File audioFile;
    private Disposable mRecordCountDown;

    private String recordFilePath;

    public AudioRecordPresenterImpl(IAudioRecordView mMvpView) {
        super(mMvpView, BaseMVPModel.class);
        recordFilePath = "/sdcard/audiorecord.amr";
    }

    @Override
    public void startRecord() {
        try {
            audioFile = new File(recordFilePath);
            if (audioFile.exists()) {
                // 如果文件存在，删除它
                audioFile.delete();
            }
            mediaRecorder = new MediaRecorder();
            // 设置音频录入源
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置录制音频的输出格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            // 设置音频的编码格式
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            // 设置录制音频文件输出文件路径
            mediaRecorder.setOutputFile(audioFile.getAbsolutePath());

            mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {

                @Override
                public void onError(MediaRecorder mr, int what, int extra) {
                    // 发生错误，停止录制
                    try {
                        if (mRecordCountDown != null)
                            mRecordCountDown.dispose();
                        if (mediaRecorder == null) {
                            mediaRecorder.stop();
                            mediaRecorder.release();
                        }
                        isRecording = false;
                        mMvpView.audioRecordError("录制失败");
                    } catch (Exception e) {

                    }
                }

            });

            // 准备、开始
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            mMvpView.audioRecordStart();
            mRecordCountDown = RxCountDown.intervalMilliseconds(0, 100).subscribe(integer -> {
                int time = integer.intValue() / 10;
                byte[] dbList = new byte[10];
                if (mediaRecorder != null) {
                    double ratio = (double) mediaRecorder.getMaxAmplitude();
                    double db = 0;// 分贝
                    if (ratio > 1)
                        db = 20 * Math.log10(ratio);
                    for (int i = 0; i < dbList.length; i++) {
                        int dbint = (new Double(db)).intValue();
                        int dbSize = dbint / (i + 1);
                        if (dbSize > 127)
                            dbSize = 127;
                        dbList[i] = (byte) dbSize;
                    }
                }
                mMvpView.audioRecordDuration(time, dbList);
                if (time == 20) {
                    stopRecord();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopRecord() {
        try {
            if (mRecordCountDown != null)
                mRecordCountDown.dispose();
            if (isRecording) {
                // 如果正在录音，停止并释放资源
                if (mediaRecorder == null)
                    return;
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                isRecording = false;
                mMvpView.audioRecordStop();
            }
        } catch (Exception e) {
            mediaRecorder = null;
            isRecording = false;
        }
    }

    @Override
    public AudioInfoRequest getAudioInfo() {
        if (audioFile == null)
            return null;
        AudioInfoRequest audioData = new AudioInfoRequest();
        audioData.setAudioPath(audioFile.getPath());
        return audioData;
    }


}
