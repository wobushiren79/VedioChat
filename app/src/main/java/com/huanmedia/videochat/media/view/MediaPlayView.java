package com.huanmedia.videochat.media.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseLinearLayout;

public class MediaPlayView extends BaseLinearLayout implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener {

    private String mVedioUrl;
    private RelativeLayout mRLLayout;
    private VideoView mVedioView;
    private ImageView mVedioIcon;
    private ProgressBar mVedioPB;

    public MediaPlayView(Context context, String vedioUrl) {
        super(context);
        this.mVedioUrl = vedioUrl;
        init();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.layout_media_play_item;
    }

    @Override
    protected void initView(View baseView) {
        mVedioView = getView(R.id.media_view);
        mVedioIcon = getView(R.id.medio_icon);
        mRLLayout = getView(R.id.media_rl_layout);
        mVedioPB = getView(R.id.media_pb);
        mVedioView.setOnPreparedListener(this);
        mVedioView.setOnCompletionListener(this);
        mVedioView.setOnErrorListener(this);

        this.setBackgroundColor(getContext().getResources().getColor(R.color.base_black));

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRLLayout.setLayoutParams(layoutParams);
    }

    @Override
    protected void initData() {

    }

    public void init() {
        if (mVedioUrl == null)
            return;
        Uri uri = Uri.parse(mVedioUrl);
        mVedioView.setVideoURI(uri);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mVedioIcon.setVisibility(VISIBLE);
        mVedioPB.setVisibility(GONE);
        mp.setLooping(true);
//        mVedioView.start();
//        mVedioView.pause();

        mVedioView.setOnTouchListener(new View.OnTouchListener() {
            GestureDetector mGesture;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mGesture == null) {
                    mGesture = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onDown(MotionEvent e) {
                            //返回false的话只能响应长摁事件
                            return true;
                        }

                        @Override
                        public void onLongPress(MotionEvent e) {
                            super.onLongPress(e);
                        }

                        @Override
                        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                            return super.onScroll(e1, e2, distanceX, distanceY);
                        }
                    });
                    mGesture.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {
                            changeVedioStatus();
                            return true;
                        }

                        @Override
                        public boolean onDoubleTap(MotionEvent e) {
                            return true;
                        }

                        @Override
                        public boolean onDoubleTapEvent(MotionEvent e) {
                            return false;
                        }
                    });
                }

                return mGesture.onTouchEvent(event);
            }
        });
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }


    public void changeVedioStatus() {
        if (mVedioView == null)
            return;
        if (mVedioView.isPlaying()) {
            stopVideo();
        } else {
            startVideo();
        }
    }


    /**
     * 开始视频
     */
    private void startVideo() {
        mVedioView.start();
        mVedioIcon.setImageResource(R.drawable.icon_video_stop);
    }

    /**
     * 暂停视频
     */
    public void stopVideo() {
        if (mVedioView != null && mVedioIcon != null) {
            mVedioView.pause();
            mVedioIcon.setImageResource(R.drawable.icon_video_play);
        }
    }


}
