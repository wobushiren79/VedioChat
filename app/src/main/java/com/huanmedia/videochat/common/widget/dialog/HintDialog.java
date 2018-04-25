package com.huanmedia.videochat.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.manager.ActivitManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.reactivex.disposables.Disposable;

import static com.huanmedia.videochat.common.widget.dialog.HintDialog.HintType.LOADING;
import static com.huanmedia.videochat.common.widget.dialog.HintDialog.HintType.SUCCEED;
import static com.huanmedia.videochat.common.widget.dialog.HintDialog.HintType.TOAST;
import static com.huanmedia.videochat.common.widget.dialog.HintDialog.HintType.WARN;


/**
 * Create by Administrator
 * time: 2017/12/5.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class HintDialog extends Dialog {
    public static final int LENGTH_SHORT = 0, LENGTH_LONG = 1;
    private Disposable countDown;
    private Animation mCircleAnim;
    private int warn = R.drawable.pop_hint;
    private int success = R.drawable.pop_pass;
    private int loading = R.drawable.pop_buffer;
    private static final int LONG_DURATION_TIMEOUT = 6;
    private static final int SHORT_DURATION_TIMEOUT = 2;

    @IntDef({LENGTH_SHORT, LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }

    /**
     * int LOADING = 1, TOAST = 2,WARN = 3, SUCCEED = 4;
     */
    @IntDef({LOADING, TOAST, WARN, SUCCEED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HintType {
        int LOADING = 1, TOAST = 2, WARN = 3, SUCCEED = 4;
    }

    private ImageView mImageVeiw;
    private TextView mTitle;
    private @HintType
    int type;
    private String mTitleText;
    @DrawableRes
    private int mBitmapSrc;

    public HintDialog(@NonNull Context context, @HintType int type) {
        super(context, R.style.customDialog_hint);
        this.type = type;
        setOnKeyListener((dialog, keyCode, event) -> {
            if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0)
            {
                dismiss();
                ActivitManager.getAppManager().finishActivity();
            }
            return true;
        });
    }

    public @HintType
    int getType() {
        return type;
    }

    public void setType(@HintType int type) {
        this.type = type;
        initView();
    }

    public void setTitleText(String titleText) {
        mTitleText = titleText;
        if (mTitle != null && !Check.isEmpty(titleText)) {
            mTitle.setText(titleText);
        }
    }

    public void setBitmapSrc(@DrawableRes int bitmapSrc) {
        mBitmapSrc = bitmapSrc;
        if (mImageVeiw != null && bitmapSrc != 0) {
            mImageVeiw.setImageResource(bitmapSrc);
        }
    }

    public ImageView getImageVeiw() {
        return mImageVeiw;
    }

    public TextView getTitle() {
        return mTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_hint);
        if (getWindow()!=null && type!=HintType.LOADING){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
        setCanceledOnTouchOutside(false);

        initView();
    }

    private void initView() {
        mImageVeiw = findViewById(R.id.dialog_hint_iv);
        if (mBitmapSrc == 0) {
            switch (type) {
                case HintType.WARN:
                    setBitmapSrc(warn);
                    break;
                case HintType.SUCCEED:
                    setBitmapSrc(success);
                    break;
                case HintType.LOADING:
                    setCanceledOnTouchOutside(true);
                    setBitmapSrc(loading);
                    break;
            }
        } else {
            setBitmapSrc(mBitmapSrc);
        }
        mTitle = findViewById(R.id.dialog_hint_tv_describe);
        setTitleText(mTitleText);
    }

    @Override
    public void dismiss() {
        if (type == LOADING) {
            if (mCircleAnim != null)
                mCircleAnim.cancel();

            if (countDown != null && !countDown.isDisposed())
                countDown.dispose();
        }
        super.dismiss();
    }

    @Override
    public void show() {
        super.show();
        if (type == LOADING) {
            if (mCircleAnim == null) {
                mCircleAnim = AnimationUtils.loadAnimation(getContext(), R.anim.dialog_loading_anim);
                mCircleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            }
            mImageVeiw.startAnimation(mCircleAnim);
        }
    }


    /**
     * 延迟显示
     *
     * @param time 延迟多少秒
     */
    public void showDelay(int time) {
        if (countDown != null && !countDown.isDisposed()) {
            countDown.dispose();
        }
        countDown = RxCountDown.delay(time).subscribe(
                integer -> {
                    show();
                }
                , Throwable::printStackTrace
        );
    }

    /**
     * 延迟显示
     *
     * @param duration 延迟多少秒
     */
    public void showToast(@Duration int duration) {
        if (countDown != null && !countDown.isDisposed()) {
            countDown.dispose();
        }
        countDown = RxCountDown.delay(duration ==
                LENGTH_LONG ? LONG_DURATION_TIMEOUT : SHORT_DURATION_TIMEOUT).subscribe(
                integer -> dismiss()
                , Throwable::printStackTrace
        );
        show();
    }
}
