package com.huanmedia.videochat.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class WaveformView extends View {
    private int mWidth;
    private int mHeight;
    private byte[] mBytes;

    private Visualizer mVisualizer;

    public WaveformView(Context context) {
        this(context, null);
    }

    public WaveformView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public void setMediaPlay(MediaPlayer mediaPlay) {
        mVisualizer = new Visualizer(mediaPlay.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener
                (captureListener, Visualizer.getMaxCaptureRate() / 2, true, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = widthMeasureSpec;
        mHeight = heightMeasureSpec;
    }

    // Pass through Visualizer data to VisualizerView
    Visualizer.OnDataCaptureListener captureListener = new Visualizer.OnDataCaptureListener() {

        //捕获波形数据
        @Override
        public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
                                          int samplingRate) {
            updateVisualizer(bytes);
        }

        //捕获傅里叶数据
        @Override
        public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
                                     int samplingRate) {

        }
    };

    public void updateVisualizer(byte[] bytes) {
        mBytes = bytes;
        invalidate();
    }

}
