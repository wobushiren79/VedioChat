package com.huanmedia.videochat.common.widget.video;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.huanmedia.videochat.R;
import com.shuyu.gsyvideoplayer.listener.GSYMediaPlayerListener;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import org.litepal.util.LogUtil;

public class VideoPtrItemPlayer extends EmptyVideoPlayer  {

    private VideoPtrItemCallBack mCallBack;

    public VideoPtrItemPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
        init();
    }

    public VideoPtrItemPlayer(Context context) {
        super(context);
        init();
    }

    public VideoPtrItemPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setCallBack(VideoPtrItemCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    private void init() {
        this.setLooping(true);
        this.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onPlayError(String url, Object... objects) {
                super.onPlayError(url, objects);
                Log.v("this", "onPlayError:" + url);
                if (mCallBack != null)
                    mCallBack.onPlayError(url);
            }
        });
    }

    public interface VideoPtrItemCallBack {
        void onPlayError(String url);
    }

}
