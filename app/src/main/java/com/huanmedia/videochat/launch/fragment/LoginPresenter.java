package com.huanmedia.videochat.launch.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.location.Location;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huanmedia.ilibray.utils.AndroidUtil;
import com.huanmedia.ilibray.utils.AppValidationMgr;
import com.huanmedia.ilibray.utils.Installation;
import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.BuildConfig;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.utils.LocationHandler;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import mvp.presenter.Presenter;

/**
 * Create by Administrator
 * time: 2017/11/20.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class LoginPresenter extends Presenter<LoginView> {

    private final MainRepostiory mRepository;
    private Disposable hintWaitThreadSubscribe;
    private Disposable mTimeDown;
    private LocationHandler mLocationHandler;

    public LoginPresenter() {
        this.mRepository = new MainRepostiory();
    }

    @Override
    public void setView(@NonNull LoginView view) {
        super.setView(view);
        mLocationHandler = new LocationHandler();
        mLocationHandler.requestLocation(3);
    }

    /**
     * 用户登录
     *
     * @param prams
     */
    public void login(Map<String, String> prams) {
        if (Check.isEmpty(prams)) {
            getView().showError(0, "异常登录");
        } else {
            if (Check.isEmpty(prams.get("mobile"))) {
                getView().showError(0, "手机号不能为空！");
            } else if (Check.isEmpty(prams.get("code"))) {
                getView().showError(0, "验证码不能为空！");
            } else {
                prams.put("version", BuildConfig.VERSION_CODE + ";" + BuildConfig.VERSION_NAME);
                prams.put("os", 1 + "");
                prams.put("deviceid", Installation.id(getContext()));
                prams.put("channelid", BuildConfig.appChannel + "");
                String ipAddressIn = AndroidUtil.getIPAddressIn(getContext());
                String ssid = AndroidUtil.getSSID(getContext());
                if (ipAddressIn != null)
                    prams.put("wifiip", ipAddressIn + "");
                if (ssid != null)
                    prams.put("wifiname", ssid + "");
                Location location = mLocationHandler.getLocation();
                if (location != null) {
                    prams.put("longitude", location.getLongitude() + "");
                    prams.put("latiude", location.getLatitude() + "");
                }
                getView().showLoading("正在登录...");
                addDisposable(mRepository.syncTime().flatMap(response -> {
                    Map<String, String> result = new Gson().fromJson(response.getResult().toString(), new TypeToken<Map<String, String>>() {
                    }.getType());
                    String time = result.get("time");
                    UserManager.getInstance().saveTimeSync(time);
                    return mRepository.login(prams);
                }).subscribe(
                        userModle -> {
                            getView().hideLoading();
                            UserManager.getInstance().saveUser(userModle);
                            if (userModle.getExtdataflag() == 1) {
                                getView().loginSuccess(userModle);
                            } else {
                                getView().completeInfo(userModle);
                            }
                        }
                        ,
                        throwable -> {
                            if (!isNullView()) {
                                getView().hideLoading();
                                getView().showError(0, getGeneralErrorStr(throwable));
                            }
                        }
                ));
            }
        }
    }

    /**
     * 验证码获取<br/>
     *
     * @param loginTvSmscode
     * @param mobile         手机号码
     */
    public void getSmsCode(TextView loginTvSmscode, String mobile) {
        if (Check.isEmpty(mobile) || !AppValidationMgr.isPhone(mobile)) {
            getView().showError(0, "手机号码不正确!");
        } else {
            getView().showLoading("获取验证码...");
            timeDownStart(loginTvSmscode);
            addDisposable(mRepository.syncTime().flatMap(response -> {
                Map<String, String> result = new Gson().fromJson(response.getResult().toString(), new TypeToken<Map<String, String>>() {
                }.getType());
                String time = result.get("time");
                UserManager.getInstance().saveTimeSync(time);
                return mRepository.sendSms(mobile);
            }).subscribe(dataResponse -> {
                        getView().smsCodeReturn(dataResponse);
                        getView().hideLoading();
                    },
                    throwable -> {
                        getView().hideLoading();
                        getView().showError(0, getGeneralErrorStr(throwable));
                    }

            ));
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void timeDownStart(TextView tv) {
        if (mTimeDown != null && !mTimeDown.isDisposed()) {
            mTimeDown.dispose();
        }
        tv.setEnabled(false);
        mTimeDown = RxCountDown.countdown(60)
                .subscribe(aLong ->
                                tv.setText(String.format(getContext().getString(R.string.login_smsCodeTimedown), aLong))
                        , Throwable::printStackTrace
                        , () -> {
                            tv.setEnabled(true);
                            tv.setText(getContext().getString(R.string.login_replySmsCode));
                        });
    }

    public Disposable getTimeDown() {
        return mTimeDown;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (hintWaitThreadSubscribe != null)
            hintWaitThreadSubscribe.dispose();
        if (mTimeDown != null) {
            mTimeDown.dispose();
        }
        mLocationHandler.disableLocation();
    }

    /**
     * 提示文字显示于消失
     *
     * @param loginTvErrorhintLayout
     */
    public void watiingGone(LinearLayout loginTvErrorhintLayout) {
        if (hintWaitThreadSubscribe != null && !hintWaitThreadSubscribe.isDisposed()) {
            hintWaitThreadSubscribe.dispose();
            hintWaitThreadSubscribe = null;
        }
        loginTvErrorhintLayout.setVisibility(View.VISIBLE);
        hintWaitThreadSubscribe = Observable.timer(10, TimeUnit.SECONDS, Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    loginTvErrorhintLayout.animate().alpha(0).setDuration(200)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    loginTvErrorhintLayout.setVisibility(View.INVISIBLE);
                                    loginTvErrorhintLayout.setAlpha(1);
                                }
                            })
                            .start();
                })
                .subscribe();
    }
}
