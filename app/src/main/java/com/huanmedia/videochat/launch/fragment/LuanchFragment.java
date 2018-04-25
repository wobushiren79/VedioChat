package com.huanmedia.videochat.launch.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.common.manager.ActivitManager;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.launch.StartActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import mvp.data.store.DataKeeper;

/**
 * @author Eric
 * @description 启动页面
 * @email yb498869020@hotmail.com
 * Create by ericYang on 2017/11/10.
 */
public class LuanchFragment extends BaseFragment {
    public static final String TAG = "LuanchFragment";
    private Disposable disposable;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_luanch;
    }

    @Override
    protected void initView(View view) {
        disposable = Observable.timer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(aLong -> {
                    boolean showFirstGuide = new DataKeeper(getContext(), DataKeeper.DEFULTFILE).get("showFirstGuide", true);
                    if (showFirstGuide) {
                        EventBus.getDefault().post(new Intent(EventBusAction.SCHEME_ACTION + "://" + EventBusAction.LUANCH_HOST + FirstGuideFragment.TAG));
                    } else {
                        if (UserManager.getInstance().islogin()) {
                            if (UserManager.getInstance().isComplete()) {
                                getNavigator().navToMain(getContext(), true);
                                ActivitManager.getAppManager().finishActivity(StartActivity.class);
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
                }, Throwable::printStackTrace, () -> {
                    if (null != disposable) {
                        disposable.dispose();
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
