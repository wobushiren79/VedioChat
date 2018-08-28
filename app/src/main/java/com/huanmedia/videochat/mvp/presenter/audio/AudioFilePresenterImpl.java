package com.huanmedia.videochat.mvp.presenter.audio;

import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AudioFileRequest;
import com.huanmedia.videochat.mvp.entity.results.AudioFileResults;
import com.huanmedia.videochat.mvp.model.audio.AudioFileModelImpl;
import com.huanmedia.videochat.mvp.view.audio.IAudioAddView;
import com.huanmedia.videochat.mvp.view.audio.IAudioDeleteView;
import com.huanmedia.videochat.mvp.view.audio.IAudioInfoView;

public class AudioFilePresenterImpl extends BaseMVPPresenter<BaseMVPView, AudioFileModelImpl> implements IAudioFilePresenter {

    private IAudioAddView mAudioAddView;
    private IAudioDeleteView mAudioDeleteView;
    private IAudioInfoView mAudioInfoView;

    public AudioFilePresenterImpl(IAudioInfoView mAudioInfoView) {
        this(null, null, mAudioInfoView);
    }

    public AudioFilePresenterImpl(IAudioAddView mAudioAddView) {
        this(mAudioAddView, null);
    }

    public AudioFilePresenterImpl(IAudioDeleteView mAudioDeleteView) {
        this(null, mAudioDeleteView);
    }

    public AudioFilePresenterImpl(IAudioAddView mAudioAddView, IAudioDeleteView mAudioDeleteView) {
        this(mAudioAddView, mAudioDeleteView, null);
    }

    public AudioFilePresenterImpl(IAudioDeleteView mAudioDeleteView, IAudioInfoView mAudioInfoView) {
        this(null, mAudioDeleteView, mAudioInfoView);
    }

    public AudioFilePresenterImpl(IAudioAddView mAudioAddView, IAudioDeleteView mAudioDeleteView, IAudioInfoView mAudioInfoView) {
        super(AudioFileModelImpl.class);
        if (mAudioAddView != null)
            this.mAudioAddView = mAudioAddView;
        if (mAudioDeleteView != null)
            this.mAudioDeleteView = mAudioDeleteView;
        if (mAudioInfoView != null)
            this.mAudioInfoView = mAudioInfoView;
    }

    @Override
    public void addAudioFile(String audioUrl, int audioTime) {
        if (mAudioAddView.getContext() == null)
            return;
        if (audioUrl == null || audioUrl.length() == 0) {
            mAudioAddView.showToast("没有音频地址");
            return;
        }
        AudioFileRequest params = new AudioFileRequest();
        params.setUrl(audioUrl);
        params.setAudiotimes(audioTime);
        mMvpModel.addAudio(mAudioAddView.getContext(), params, new DataCallBack() {
            @Override
            public void getDataSuccess(Object data) {
                mAudioAddView.addAudioSuccess();
            }

            @Override
            public void getDataFail(String msg) {
                mAudioAddView.addAudioFail(msg);
            }
        });
    }

    @Override
    public void deleteAudioFile() {
        if (mAudioDeleteView.getContext() == null)
            return;
        AudioFileRequest params = new AudioFileRequest();
        mMvpModel.deleteAudio(mAudioDeleteView.getContext(), params, new DataCallBack() {
            @Override
            public void getDataSuccess(Object data) {
                mAudioDeleteView.deleteAudioSuccess();
            }

            @Override
            public void getDataFail(String msg) {
                mAudioDeleteView.deleteAudioFail(msg);
            }
        });
    }

    @Override
    public void getAudioInfo() {
        if (mAudioInfoView.getContext() == null)
            return;
        AudioFileRequest params = new AudioFileRequest();
        mMvpModel.getAudio(mAudioInfoView.getContext(), params, new DataCallBack<AudioFileResults>() {
            @Override
            public void getDataSuccess(AudioFileResults data) {
                mAudioInfoView.getAudioInfoSuccess(data);
            }

            @Override
            public void getDataFail(String msg) {
                mAudioInfoView.getAudioInfoFail(msg);
            }
        });
    }
}
