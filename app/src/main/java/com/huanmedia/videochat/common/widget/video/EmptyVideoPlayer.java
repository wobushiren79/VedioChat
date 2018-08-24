package com.huanmedia.videochat.common.widget.video;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.huanmedia.videochat.R;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.GSYADVideoPlayer;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class EmptyVideoPlayer extends StandardGSYVideoPlayer {

    private ImageView mStartBT;
    private RelativeLayout mFullLayout;
    private OnVideoClickListener mVideoClickListener;
    private boolean mIsCanTouch = true;

    public EmptyVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
        initView();
    }

    public EmptyVideoPlayer(Context context) {
        super(context);
        initView();
    }

    public EmptyVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    /**
     * 设置是否能点击
     * @param canTouch
     */
    public void setCanClick(boolean canTouch){
        mIsCanTouch=canTouch;
    }
//
//    @Override
//    public void startPlayLogic() {
//        super.startPlayLogic();
//    }

    @Override
    public void startPlayLogic() {
        super.startPlayLogic();
        if (mStartBT != null && mStartBT.getVisibility() == VISIBLE) {
            mStartBT.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onVideoPause() {
        super.onVideoPause();
        mStartBT.setVisibility(VISIBLE);
    }

    @Override
    public void onVideoResume() {
        super.onVideoResume();
        mStartBT.setVisibility(INVISIBLE);
    }

    @Override
    public void onVideoReset() {
        super.onVideoReset();
        mStartBT.setVisibility(INVISIBLE);
    }

    public View getStartBT() {
        return mStartBT;
    }

    private long mClickTime;

    private void initView() {
        mStartBT = findViewById(R.id.iv_start);
        mFullLayout = findViewById(R.id.surface_container);
        mFullLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
        this.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                if (mStartBT != null && mStartBT.getVisibility() == VISIBLE) {
                    mStartBT.setVisibility(INVISIBLE);
                }
            }
        });
    }

    public void setOnVideoClickListener(OnVideoClickListener onVideoClickListener) {
        mVideoClickListener = onVideoClickListener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_empty_video_player;
    }

    @Override
    protected void touchSurfaceMoveFullLogic(float absDeltaX, float absDeltaY) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
        //不给触摸快进，如果需要，屏蔽下方代码即可
        mChangePosition = false;

        //不给触摸音量，如果需要，屏蔽下方代码即可
        mChangeVolume = false;

        //不给触摸亮度，如果需要，屏蔽下方代码即可
        mBrightness = false;
    }

    @Override
    protected void touchDoubleUp() {
        //super.touchDoubleUp();
        //不需要双击暂停
    }

    //-----------------------视频监听-----------------------------------------

    /**
     * 下方两个重载方法，在播放开始前不屏蔽封面
     */
    @Override
    public void onSurfaceUpdated(Surface surface) {
        super.onSurfaceUpdated(surface);
        if (mThumbImageViewLayout != null && mThumbImageViewLayout.getVisibility() == VISIBLE) {
            mThumbImageViewLayout.setVisibility(INVISIBLE);
        }

    }

    @Override
    protected void setViewShowState(View view, int visibility) {
        if (view == mThumbImageViewLayout && visibility != VISIBLE) {
            return;
        }
        super.setViewShowState(view, visibility);
    }

    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

        /**
         * 发生确定的单击时执行
         * @param e
         * @return
         */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {//单击事件
            if (mVideoClickListener != null)
                mVideoClickListener.onSingleTap(e);
            if(!mIsCanTouch)
                return super.onSingleTapConfirmed(e);
            if (System.currentTimeMillis() - mClickTime < 800) {
                //此处做双击具体业务逻辑
            } else {
                mClickTime = System.currentTimeMillis();
                //表示单击，此处也可以做单击的操作
            }
            switch (mCurrentState) {
                case CURRENT_STATE_NORMAL:
                    break;
                case CURRENT_STATE_PREPAREING:
                    break;
                case CURRENT_STATE_PLAYING:
                    mStartBT.setVisibility(VISIBLE);
                    EmptyVideoPlayer.this.onVideoPause();
                    break;
                case CURRENT_STATE_PAUSE:
                    mStartBT.setVisibility(GONE);
                    EmptyVideoPlayer.this.onVideoResume();
                    break;
                case CURRENT_STATE_ERROR:
                    break;
                case CURRENT_STATE_AUTO_COMPLETE:
                    break;
                default:
                    mStartBT.setVisibility(GONE);
                    break;
            }
            return super.onSingleTapConfirmed(e);
        }

        /**
         * 双击发生时的通知
         * @param e
         * @return
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {//双击事件
            if (mVideoClickListener != null)
                mVideoClickListener.onDoubleTap(e);
            return super.onDoubleTap(e);
        }

        /**
         * 双击手势过程中发生的事件，包括按下、移动和抬起事件
         * @param e
         * @return
         */
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }
    });


    /**
     * 点击事件
     */
    public interface OnVideoClickListener {
        /**
         * 双击
         */
        void onDoubleTap(MotionEvent e);

        /**
         * 单击
         */
        void onSingleTap(MotionEvent e);
    }
}