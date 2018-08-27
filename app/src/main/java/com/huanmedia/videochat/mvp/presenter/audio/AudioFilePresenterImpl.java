package com.huanmedia.videochat.mvp.presenter.audio;

import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.AudioFileRequest;
import com.huanmedia.videochat.mvp.model.audio.AudioFileModelImpl;
import com.huanmedia.videochat.mvp.view.audio.IAudioAddView;
import com.huanmedia.videochat.mvp.view.audio.IAudioDeleteView;

public class AudioFilePresenterImpl extends BaseMVPPresenter<BaseMVPView, AudioFileModelImpl> implements IAudioFilePresenter {

    private IAudioAddView mAudioAddView;
    private IAudioDeleteView mAudioDeleteView;

    public AudioFilePresenterImpl(IAudioAddView mAudioAddView) {
        this(mAudioAddView, null);
    }

    public AudioFilePresenterImpl(IAudioDeleteView mAudioDeleteView) {
        this(null, mAudioDeleteView);
    }

    public AudioFilePresenterImpl(IAudioAddView mAudioAddView, IAudioDeleteView mAudioDeleteView) {
        super(AudioFileModelImpl.class);
        if (mAudioAddView != null)
            this.mAudioAddView = mAudioAddView;
        if (mAudioDeleteView != null)
            this.mAudioDeleteView = mAudioDeleteView;
    }

    @Override
    public void addAudioFile(String audioUrl) {
        if (mAudioAddView.getContext() == null)
            return;
        if (audioUrl == null || audioUrl.length() == 0) {
            mAudioAddView.showToast("没有音频地址");
            return;
        }
        AudioFileRequest params = new AudioFileRequest();
        params.setUrl(audioUrl);
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
        mMvpModel.addAudio(mAudioDeleteView.getContext(), params, new DataCallBack() {
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
}
