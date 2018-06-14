package com.huanmedia.videochat.common.widget.video;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.huanmedia.videochat.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class VideoPtrItemPlayer extends EmptyVideoPlayer {

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

    private void init() {
        this.setLooping(true);
    }


}
