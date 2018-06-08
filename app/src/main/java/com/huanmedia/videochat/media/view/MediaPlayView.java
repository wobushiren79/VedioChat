package com.huanmedia.videochat.media.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.common.BaseLinearLayout;

import java.util.HashMap;

public class MediaPlayView extends BaseFragment implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener {

    private String mVedioUrl;
    private RelativeLayout mRLLayout;
    private VideoView mVedioView;
    private ImageView mVedioIcon;
    private ImageView mVedioPic;
    private ProgressBar mVedioPB;

    private boolean isFirst = false;


    @Override
    protected int getLayoutId() {
        return R.layout.layout_media_play_item;
    }

    @Override
    protected void initView(View baseView) {
        mVedioView = findViewById(R.id.media_view);
        mVedioIcon = findViewById(R.id.medio_icon);
        mRLLayout = findViewById(R.id.media_rl_layout);
        mVedioPB = findViewById(R.id.media_pb);
        mVedioPic = findViewById(R.id.media_pic);

        mVedioView.setOnPreparedListener(this);
        mVedioView.setOnCompletionListener(this);
        mVedioView.setOnErrorListener(this);
    }

    @Override
    protected void initData() {
        Uri uri = Uri.parse(mVedioUrl);
        mVedioView.setVideoURI(uri);

//        ((Activity) getContext()).runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Bitmap videoPic = createVideoThumbnail(mVedioUrl);
//                if (videoPic != null)
//                    mVedioPic.setImageBitmap(videoPic);
//            }
//        });


    }

    /**
     * 设置视频地址
     *
     * @param videoUrl
     */
    public void setVedioUrl(String videoUrl) {
        mVedioUrl = videoUrl;
    }


    public void setFirst(boolean first) {
        isFirst = first;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (isFirst) {
//            mVedioIcon.setVisibility(View.VISIBLE);
            mVedioView.start();
        }
        isFirst = false;
        mVedioPB.setVisibility(View.GONE);
        mp.setLooping(true);
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
            mVedioView.pause();
            mVedioIcon.setVisibility(View.VISIBLE);
//            mVedioIcon.setImageResource(R.drawable.icon_video_play);
        } else {
            mVedioView.start();
            mVedioPic.setVisibility(View.GONE);
            mVedioIcon.setVisibility(View.GONE);
//            mVedioIcon.setImageResource(R.drawable.icon_video_stop);
        }
    }


    /**
     * 开始视频
     */
    public void startVideo() {
        if (mVedioView != null && mVedioIcon != null) {
            mVedioPic.setVisibility(View.GONE);
            mVedioIcon.setVisibility(View.GONE);
            mVedioView.setVisibility(View.VISIBLE);
            mVedioView.start();
//            mVedioIcon.setImageResource(R.drawable.icon_video_stop);
        }
    }


    /**
     * 暂停视频
     */
    public void stopVideo() {
        if (mVedioView != null && mVedioIcon != null) {
            mVedioPic.setVisibility(View.VISIBLE);
            mVedioIcon.setVisibility(View.VISIBLE);
            mVedioView.setVisibility(View.GONE);
            mVedioView.pause();
//            mVedioIcon.setImageResource(R.drawable.icon_video_play);
        }
    }

    /**
     * 获取网络缩略图
     *
     * @param url
     * @return
     */
    private Bitmap createVideoThumbnail(String url) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
//        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
//            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
//                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//        }
        return bitmap;
    }

    @Override
    public void onResume() {
        mVedioView.setVisibility(View.VISIBLE);
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (mVedioView != null && mVedioIcon != null) {
            mVedioView.stopPlayback();
            mVedioIcon.setVisibility(View.GONE);
//            mVedioIcon.setImageResource(R.drawable.icon_video_play);
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        stopVideo();
        super.onPause();
    }
}
