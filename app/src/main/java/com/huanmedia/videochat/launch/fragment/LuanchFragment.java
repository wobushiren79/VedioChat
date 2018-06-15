package com.huanmedia.videochat.launch.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.common.manager.ActivitManager;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.common.utils.UMengUtils;
import com.huanmedia.videochat.launch.StartActivity;
import com.huanmedia.videochat.mvp.entity.results.AdsLuanchResults;
import com.huanmedia.videochat.mvp.presenter.info.AdsLuanchPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.info.IAdsLuanchPresenter;
import com.huanmedia.videochat.mvp.view.info.IAdsLuanchView;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import mvp.data.store.DataKeeper;
import mvp.data.store.glide.GlideApp;

/**
 * @author Eric
 * @description 启动页面
 * @email yb498869020@hotmail.com
 * Create by ericYang on 2017/11/10.
 */
public class LuanchFragment extends BaseFragment implements IAdsLuanchView {

    @BindView(R.id.ll_luanch_ads_jump)
    LinearLayout mLLLuanchAdsJump;
    @BindView(R.id.ll_luanch_ads)
    FrameLayout mLLLuanchAds;
    @BindView(R.id.iv_luanch_ads)
    ImageView mIVAds;
    @BindView(R.id.tv_time)
    TextView mTVTime;

    public static final String TAG = "LuanchFragment";
    private Disposable disposable;
    private Disposable mLuanchAdsDis;

    private boolean mHasLuanchAds = false;
    private String mLuanchImageUrl = "";
    private String mLuanchJumpUrl = "";

    private IAdsLuanchPresenter mAdsLuanchPresenter;
    private boolean showFirstGuide;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_luanch;
    }

    @Override
    protected void initView(View view) {
        showFirstGuide = new DataKeeper(getContext(), DataKeeper.DEFULTFILE).get("showFirstGuide", true);
        mAdsLuanchPresenter = new AdsLuanchPresenterImpl(this);
        mAdsLuanchPresenter.getAdsLuanchInfo();
        disposable = Observable
                .timer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(aLong -> {
                    if (mHasLuanchAds) {
                        startLuanchAds();
                    } else {
                        start();
                    }
                }, Throwable::printStackTrace, () -> {
                    if (null != disposable) {
                        disposable.dispose();
                    }
                });
    }

    /**
     * 开始倒计时
     */
    private void startLuanchAds() {
        mLLLuanchAds.setVisibility(View.VISIBLE);
        Glide
                .with(getContext())
                .load(mLuanchImageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        start();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mLLLuanchAdsJump.setVisibility(View.VISIBLE);
                        mLLLuanchAdsJump.setOnClickListener(view -> {
                            mLuanchAdsDis.dispose();
                            start();
                        });
                        mLuanchAdsDis = RxCountDown
                                .countdown(5)
                                .subscribe(integer ->
                                        {
                                            mTVTime.setText("( " + integer + " )");
                                            if (integer == 0) {
                                                start();
                                            }
                                        }
                                );
                        return false;
                    }
                })
                .into(mIVAds);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLuanchAdsDis != null)
            mLuanchAdsDis.dispose();
        if (disposable != null)
            disposable.dispose();
    }

    /**
     * 开始功能
     */
    private void start() {
        mIVAds.setOnClickListener(null);
        if (showFirstGuide) {
            EventBus.getDefault().post(new Intent(EventBusAction.SCHEME_ACTION + "://" + EventBusAction.LUANCH_HOST + FirstGuideFragment.TAG));
        } else {
            if (UserManager.getInstance().islogin()) {
                if (UserManager.getInstance().isComplete()) {
                    getNavigator().navToMain(getContext(), true);
                    getActivity().finish();
                } else {
                    Intent intent = new Intent(EventBusAction.SCHEME_ACTION + "://" + EventBusAction.LUANCH_HOST + CompleteInformationFragment.TAG);
                    intent.putExtra("isAddBack", false);
                    intent.putExtra("isRemoveCurrent", true);
                    EventBus.getDefault().post(intent);
                }
            } else {
                EventBus.getDefault().post(new Intent(EventBusAction.SCHEME_ACTION + "://" + EventBusAction.LUANCH_HOST + LoginFragment.TAG));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void getLuanchInfoAdsSuccess(AdsLuanchResults request) {
        mHasLuanchAds = true;
    }

    @Override
    public void getLuanchInfoAdsFail(String msg) {

    }

    @Override
    public void setLuanchImage(String imageUrl) {
        mLuanchImageUrl = imageUrl;
    }

    @Override
    public void setJumpUrl(String jumpUrl) {
        mLuanchJumpUrl = jumpUrl;
        mIVAds.setOnClickListener(view -> {
            if (mLuanchAdsDis != null)
                mLuanchAdsDis.dispose();
            getNavigator().navtoWebActivityForResult(getActivity(), jumpUrl, null, 1);
            UMengUtils.LuanchAdsClick(getContext(), jumpUrl);
        });
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShortInCenter(getContext(), toast);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            start();
        }
    }
}
