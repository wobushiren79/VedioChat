package com.huanmedia.videochat.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.huanmedia.videochat.R;

/**
 * Create by Administrator
 * time: 2017/11/23.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class VideoChatWindowView extends FrameLayout {
    private Paint mPaint;
    private Path mPath = new Path();
    private Path mCirclePath = new Path();
    private Path mClipPath = new Path();
    private int mEndColor=Color.parseColor("#eeffb400"),mStartColor=Color.parseColor("#eeffcc00");//渐变颜色
    private boolean mSuppertBackground;//是否支持渐变背景

    private Paint mClipPaint;
    private int mLeft=dip2px(16);
    private int mRight=dip2px(16);
    private int mButtom=dip2px(42);
    private int mTop=dip2px(54);
    private RectF mFRect;
    private RectF mCRect;
    private Paint mBorderPaint;
    private int mArcRadius=dip2px(38);
    private int mArcOffset=dip2px(16);
    private int mBorderWidth=dip2px(1);
    private int mBorderColor=Color.WHITE;


    public VideoChatWindowView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public VideoChatWindowView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VideoChatWindowView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VideoChatWindowView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }
    private void init(AttributeSet attrs) {
        if (attrs!=null){
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.VideoChatWindowView);
            mLeft = typedArray.getDimensionPixelSize(R.styleable.VideoChatWindowView_vwv_center_left,mLeft);
            mRight = typedArray.getDimensionPixelSize(R.styleable.VideoChatWindowView_vwv_center_right,mRight);
            mButtom = typedArray.getDimensionPixelSize(R.styleable.VideoChatWindowView_vwv_center_buttom,mButtom);
            mTop = typedArray.getDimensionPixelSize(R.styleable.VideoChatWindowView_vwv_center_top,mTop);
            mArcRadius = typedArray.getDimensionPixelSize(R.styleable.VideoChatWindowView_vwv_arc_radius,mArcRadius);
            mBorderColor = typedArray.getColor(R.styleable.VideoChatWindowView_vwv_border_color,mBorderColor);
            mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.VideoChatWindowView_vwv_border_width,mBorderWidth);
            mArcOffset = typedArray.getDimensionPixelSize(R.styleable.VideoChatWindowView_vwv_arc_offset,mArcOffset);
            mStartColor = typedArray.getColor(R.styleable.VideoChatWindowView_arc_startColor, mStartColor);
            mEndColor = typedArray.getColor(R.styleable.VideoChatWindowView_arc_endColor,mEndColor);
            mSuppertBackground = typedArray.getBoolean(R.styleable.VideoChatWindowView_arc_suppert_background,false);
            typedArray.recycle();
        }
//        背景
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        //边框
        mBorderPaint = new Paint();
        mBorderPaint.setColor(Color.WHITE);
        mBorderPaint.setStrokeWidth(dip2px(1.5f));
        mBorderPaint.setStyle(Paint.Style.STROKE);
        //抠图
        mClipPaint = new Paint();
        mClipPaint.setAntiAlias(true);
//        mClipPaint.setColor(Color.RED);
        mClipPaint.setStyle(Paint.Style.FILL);
        mClipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initParms(w,h);
    }

    private void initParms(int width, int height) {
        mFRect=new RectF(0, 0, width, height);
        mPath.reset();
        //背景路径
        mPath.moveTo(0, 0);
        mPath.addRect(mFRect, Path.Direction.CCW);
        mPath.close();

        if (mSuppertBackground){
            LinearGradient sweepGradient = new LinearGradient(0,height/2,width,height/2,mStartColor,mEndColor, Shader.TileMode.CLAMP);
            mPaint.setShader(sweepGradient);
        }

        //抠图路径
        mCRect=new RectF(mLeft, mTop, width-mRight, height-mButtom);
        mClipPath.reset();
        mClipPath.moveTo(mLeft,mTop);
        mClipPath.addRoundRect(mCRect,dip2px(5),dip2px(5), Path.Direction.CCW);
        mClipPath.close();
        //圆形路径
        mCirclePath.reset();
        mCirclePath.moveTo(0,0);
        mCirclePath.addCircle(width/2,mTop+(height-mButtom-mTop)+mArcOffset,mArcRadius, Path.Direction.CCW);
        mCirclePath.close();
        //合并路径
        mClipPath.op(mCirclePath,Path.Op.DIFFERENCE);
//        Path mergepath = new Path();
//        mergepath.op(mClipPath,mCirclePath,Path.Op.DIFFERENCE);
//        mergepath.close();
//        mClipPath=mergepath;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Log.i(VIEW_LOG_TAG, "dispatchDraw");
//-------------------方法1：会有重复调用dispatchDraw方法问题
//        if (getChildCount()>0){
//            isChangeState=false;
//        if (mBitmap == null)
//            mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
//        if (mShader == null)
//            mShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//        if (mCapCanvas == null) {
//            mCapCanvas = new Canvas(mBitmap);
//        }
//            super.dispatchDraw(mCapCanvas);
//            mPaint.setShader(mShader);
//            mPath.moveTo(mStartPoint.x, mStartPoint.y);
//            mPath.quadTo(mControlPoint.x, mControlPoint.y, mEndPoint.x, mEndPoint.y);
//            canvas.drawPath(mPath, mPaint);
//        }else {
//            super.dispatchDraw(canvas);
//        }
//--------------------方法2 无法去掉锯齿
//        if (getChildCount()>0){
//            canvas.save();
//            mPath.moveTo(mStartPoint.x, mStartPoint.y);
//            mPath.quadTo(mControlPoint.x, mControlPoint.y, mEndPoint.x, mEndPoint.y);
//            canvas.clipPath(mPath);
//            super.dispatchDraw(canvas);
//            canvas.restore();
//        }else {
//            super.dispatchDraw(canvas);
//        }
//---------------------方法3
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), null, Canvas
                .ALL_SAVE_FLAG);
        if (mSuppertBackground){
            canvas.drawPath(mPath,mPaint);
        }
        canvas.drawPath(mClipPath,mBorderPaint);
//        canvas.drawPath(mCirclePath,mBorderPaint);
        super.dispatchDraw(canvas);
        //裁剪
        canvas.drawPath(mClipPath, mClipPaint);
        canvas.restore();
//        mClipPaint.setXfermode(null);

    }


    public void setEndColor(int endColor) {
        mEndColor = endColor;
    }

    public void setStartColor(int startColor) {
        mStartColor = startColor;
    }

    public void setSuppertBackground(boolean suppertBackground) {
        mSuppertBackground = suppertBackground;
    }


    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
