package com.huanmedia.videochat.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huanmedia.ilibray.utils.TimeUtils;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.mvp.entity.request.AudioInfoRequest;
import com.huanmedia.videochat.mvp.entity.request.VideoInfoRequest;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;
import com.huanmedia.videochat.mvp.presenter.audio.AudioFilePresenterImpl;
import com.huanmedia.videochat.mvp.presenter.audio.AudioPlayPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.audio.AudioRecordPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.audio.IAudioFilePresenter;
import com.huanmedia.videochat.mvp.presenter.audio.IAudioPlayPresenter;
import com.huanmedia.videochat.mvp.presenter.audio.IAudioRecordPresenter;
import com.huanmedia.videochat.mvp.presenter.file.FileUpLoadPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.file.IFileUpLoadPresenter;
import com.huanmedia.videochat.mvp.view.audio.IAudioAddView;
import com.huanmedia.videochat.mvp.view.audio.IAudioPlayView;
import com.huanmedia.videochat.mvp.view.audio.IAudioRecordView;
import com.huanmedia.videochat.mvp.view.file.IFileUpLoadView;

import org.w3c.dom.Text;

import java.io.File;

public class AudioRecordDialog extends Dialog
        implements
        View.OnClickListener,
        IAudioRecordView,
        View.OnTouchListener,
        IFileUpLoadView,
        IAudioPlayView,
        IAudioAddView {
    private View mView;
    private ImageView mIVRecord;
    private ImageView mIVPlay;
    private LinearLayout mLLButton;
    private TextView mTVCancel;
    private TextView mTVSumbit;
    private TextView mTVTime;

    private CallBack mCallBack;

    private IAudioRecordPresenter mRecordPresenter;
    private IFileUpLoadPresenter mFileUplaodPresenter;
    private IAudioPlayPresenter mPlayPresenter;
    private IAudioFilePresenter mAudioFilePresenter;

    public AudioRecordDialog(@NonNull Context context) {
        super(context, R.style.customDialog);
        mRecordPresenter = new AudioRecordPresenterImpl(this);
        mFileUplaodPresenter = new FileUpLoadPresenterImpl(this);
        mPlayPresenter = new AudioPlayPresenterImpl(this);
        mAudioFilePresenter = new AudioFilePresenterImpl(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = View.inflate(getContext(), R.layout.dialog_audio_record, null);
        setContentView(mView);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = getContext().getResources().getDimensionPixelOffset(R.dimen.dimen_372dp);
        getWindow().setAttributes(layoutParams);

        initView();
        initData();
    }

    private void initView() {
        mIVRecord = mView.findViewById(R.id.iv_audio_record);
        mLLButton = mView.findViewById(R.id.ll_audio_bt);
        mIVPlay = mView.findViewById(R.id.iv_audio_play);
        mTVCancel = mView.findViewById(R.id.tv_cancel);
        mTVSumbit = mView.findViewById(R.id.tv_submit);
        mTVTime = mView.findViewById(R.id.tv_audio_time);

        mLLButton.setVisibility(View.GONE);

        mIVRecord.setOnTouchListener(this);

        mIVPlay.setOnClickListener(this);
        mTVCancel.setOnClickListener(this);
        mTVSumbit.setOnClickListener(this);
    }

    private void initData() {

    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    private boolean isPlaying = false;

    @Override
    public void onClick(View view) {
        if (view == mTVCancel) {
            this.cancel();
        } else if (view == mTVSumbit) {
            mFileUplaodPresenter.getAliyunUpLoadInfo(2);
        } else if (view == mIVPlay) {
            if (isPlaying) {
                mIVPlay.setImageResource(R.drawable.icon_audio_play);
                mPlayPresenter.releasePlay();
                isPlaying = false;
            } else {
                mIVPlay.setImageResource(R.drawable.icon_audio_stop);
                mPlayPresenter.startPlay(mRecordPresenter.getAudioInfo().getAudioPath());
                isPlaying = true;
            }

        }
    }

    @Override
    public void cancel() {
        mPlayPresenter.releasePlay();
        super.cancel();
    }

    //--------------录音处理---------------
    @Override
    public void audioRecordStart() {

    }

    @Override
    public void audioRecordDuration(int duration) {
        setAuditTime(duration);
    }

    @Override
    public void audioRecordStop() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = getContext().getResources().getDimensionPixelOffset(R.dimen.dimen_453dp);
        getWindow().setAttributes(layoutParams);
        mIVRecord.setVisibility(View.GONE);
        mIVPlay.setVisibility(View.VISIBLE);
        mLLButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void audioRecordError(String msg) {
        showToast(msg);
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShortInCenter(getContext(), toast);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view == mIVRecord) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mRecordPresenter.startRecord();
                    break;
                case MotionEvent.ACTION_UP:
                    mRecordPresenter.stopRecord();
                    break;
            }
        }
        return true;
    }

    //-----------文件上传处理---------------
    @Override
    public void uploadFileByAliyunSuccess(FileUpLoadResults results) {
        mAudioFilePresenter.addAudioFile(results.getFilename());
    }

    @Override
    public void uploadFileByAliyunFail(String msg) {
        showToast(msg);
    }

    @Override
    public void getAliyunUpLoadInfoSuccess(FileUpLoadResults results) {
        mFileUplaodPresenter.startUpLoadByAliyunForAudio(results);
    }

    @Override
    public void getAliyunUpLoadInfoFail(String msg) {
        showToast(msg);
    }

    @Override
    public void uploadFileOnProgress(long currentSize, long totalSize) {

    }

    @Override
    public void startUploadAliyun(FileUpLoadResults results) {

    }

    @Override
    public VideoInfoRequest getVideoInfo() {
        return null;
    }

    @Override
    public AudioInfoRequest getAudioInfo() {
        return mRecordPresenter.getAudioInfo();
    }


    @Override
    public void addAudioSuccess() {
        showToast("添加语音介绍成功");
        if (mCallBack != null) {
            mCallBack.addAudioSuccess();
        }
        this.cancel();
    }

    @Override
    public void addAudioFail(String msg) {
        showToast(msg);
    }

    //------------音频播放监听--------------
    @Override
    public void audioPlayComplete() {
        mIVPlay.setImageResource(R.drawable.icon_audio_play);
        isPlaying = false;
    }

    @Override
    public void audioPlayDuration(int duration) {
        setAuditTime(duration);
    }

    //--------------------------------------------------
    private void setAuditTime(int duration) {
        int minutes = duration / 60;
        int seconds = duration % 60;
        String minutesStr = "";
        String secondsStr = "";
        if (minutes <= 9) {
            minutesStr = "0" + minutes;
        } else {
            minutesStr = minutes + "";
        }
        if (seconds <= 9) {
            secondsStr = "0" + seconds;
        } else {
            secondsStr = seconds + "";
        }

        mTVTime.setText(minutesStr + ":" + secondsStr);
    }

    public interface CallBack {
        void addAudioSuccess();
    }

}
