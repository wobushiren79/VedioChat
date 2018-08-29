package com.huanmedia.videochat.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.huanmedia.videochat.R;

public class WaveformView extends View {
    private int mWidth;
    private int mHeight;
    private byte[] mBytes;
    private int mWaveNumber;//波形数量
    private int mWaveInterval;//波形间隔
    private int mItemWaveWith;//单个波形宽度
    private int mItemRound;//波形圆角
    private int mDirection;//方向 0左开始，1右开始
    private float mItemHightRate;//单个波形高度比
    private Paint mWavePaint;

    public WaveformView(Context context) {
        this(context, null);
    }

    public WaveformView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mWaveNumber = 10;
        mWaveInterval = 10;
        mItemRound = 3;
        mWavePaint = new Paint();
        mWavePaint.setColor(getContext().getResources().getColor(R.color.color_f65aa0));

    }

    /**
     * 设置朝向
     *
     * @param direction 0左开始，1右开始
     */
    public void setDirection(int direction) {
        mDirection = direction;
    }

    /**
     * 设置关联MediaPlay
     *
     * @param bytes
     */
    public void updateWaveform(byte[] bytes) {
        mBytes = bytes;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBytes == null || mBytes.length < mWaveNumber) {
            mBytes = new byte[mWaveNumber];
            for (int i = 0; i < mWaveNumber; i++) {
                mBytes[i] = 0;
            }
        }
        for (int i = 0; i < mWaveNumber; i++) {
            int dataPosition;
            if (mDirection == 0) {
                dataPosition = i;
            } else {
                dataPosition = mWaveNumber - 1 - i;
            }
            createItemWave(canvas, i, dataPosition);
        }
    }

    private void createItemWave(Canvas canvas, int i, int dataPostion) {
        float itemLeft = i * (mItemWaveWith + mWaveInterval);
        float itemRight = itemLeft + mItemWaveWith;
        float itemTop = mHeight / 2f - mItemHightRate * mBytes[dataPostion] - 1;
        float itemBottom = mHeight / 2f + mItemHightRate * mBytes[dataPostion] + 1;
        RectF itemRectF = new RectF(itemLeft, itemTop, itemRight, itemBottom);
        canvas.drawRoundRect(itemRectF, mItemRound, mItemRound, mWavePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mItemWaveWith = (mWidth - (mWaveInterval * (mWaveNumber - 1))) / mWaveNumber;
        mItemHightRate = (mHeight / 2f) / 128f;
    }

}
