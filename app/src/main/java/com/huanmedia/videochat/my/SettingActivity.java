package com.huanmedia.videochat.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.videochat.BuildConfig;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPActivity;
import com.huanmedia.videochat.common.FApplication;
import com.huanmedia.videochat.common.manager.ResourceManager;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;
import com.huanmedia.videochat.repository.net.HostManager;
import com.kyleduo.switchbutton.SwitchButton;
import com.orhanobut.logger.Logger;

import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.base.CheckCallback;
import org.lzh.framework.updatepluginlib.model.Update;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import mvp.data.store.DataKeeper;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class SettingActivity extends BaseMVPActivity implements CompoundButton.OnCheckedChangeListener {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.setting_outLogin_ll)
    LinearLayout mSettingOutLoginLl;
    @BindView(R.id.setting_tv_account)
    TextView mSettingTvAccount;
    @BindView(R.id.setting_notification_sb)
    Switch mSettingNotificationSb;
    @BindView(R.id.setting_sound_sb)
    Switch mSettingSoundSb;
    DataKeeper mDataKeeper = new DataKeeper(FApplication.getApplication(), DataKeeper.DEFULTFILE);
    public static final String SETTING_KEY_NO_NOTIFICATION = "setting_key_no_notification";
    public static final String SETTING_KEY_SOUND = "setting_key_sound";
    @BindView(R.id.setting_debug_et_network)
    EditText mSettingDebugEtNetwork;
    @BindView(R.id.setting_debug_tv_network_current)
    TextView mSettingDebugTvNetworkCurrent;
    @BindView(R.id.setting_debug_ll)
    LinearLayout mSettingDebugLl;
    private Badge mBadeg;
    private HintDialog mHintDialog;
    private HintDialog mLoadingDialog;
    private UpdateBuilder mUpdata;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SettingActivity.class);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void initView() {
//        bindService(new Intent(this, UpdateService.class), connectionService, Service.BIND_AUTO_CREATE);
        initToolbar();
        if (UserManager.getInstance().getCurrentUser() != null) {
            mSettingTvAccount.setText(String.format("ID:%s", UserManager.getInstance().getCurrentUser().getMobile()));
        }
        mSettingNotificationSb.setChecked(mDataKeeper.get(SETTING_KEY_NO_NOTIFICATION, false));
        mSettingSoundSb.setChecked(mDataKeeper.get(SETTING_KEY_SOUND, true));
        mSettingNotificationSb.setOnCheckedChangeListener(this);
        mSettingSoundSb.setOnCheckedChangeListener(this);
        mSettingDebugTvNetworkCurrent.setText(String.format("当前网络：%d", HostManager.getNetWorkType()));
        if (BuildConfig.netWorkType != 3) {
            mSettingDebugLl.setVisibility(View.VISIBLE);
        }
        if (isNewVersion()) {
            setVersionBadeg(" ");
        }

    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    protected ImmersionBar defaultBarConfig() {
        return super.defaultBarConfig().statusBarDarkFont(true);
    }

    @Override
    protected View getTitlebarView() {
        return mToolbar;
    }

    public void outLogin() {
        new MaterialDialog.Builder(this)
                .title("确认退出？")
                .positiveText("确认")
                .negativeText("取消")
                .positiveColorRes(R.color.base_yellow)
                .negativeColorRes(R.color.base_gray)
                .onPositive((dialog, which) -> {
                    Map<String, String> prmas = new HashMap<>();
                    prmas.put("mobile", UserManager.getInstance().getCurrentUser().getMobile());
                    new MainRepostiory().outLogin(prmas).subscribe(response -> {
                    }, Throwable::printStackTrace, () -> Logger.e("异常退出"));
                    UserManager.getInstance().outLogin(null);
                    UserManager.getInstance().exit();
                })
                .show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.setting_notification_sb:
                mDataKeeper.put(SETTING_KEY_NO_NOTIFICATION, isChecked);
                break;
            case R.id.setting_sound_sb:
                mDataKeeper.put(SETTING_KEY_SOUND, isChecked);
                break;
        }
    }

    @OnClick({R.id.setting_other_feedback_rl, R.id.setting_outLogin_ll, R.id.setting_debug_btn_network_ok, R.id.setting_other_user_agreement_rl_desable, R.id.setting_checkNew_rl, R.id.setting_other2_aboutUs_rl, R.id.setting_debug_btn_monitor})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting_outLogin_ll:
                outLogin();
                break;
            case R.id.setting_other_feedback_rl:
                getNavigator().navtoFeedBack(this);
                break;
            case R.id.setting_other_user_agreement_rl_desable:
                getNavigator().navtoWebActivity(this, HostManager.getServiceUrl() + "index/h5page/about", "用户协议");
                break;
            case R.id.setting_checkNew_rl:
                if (mUpdata == null) {
                    mUpdata = UpdateBuilder.create();
                    mUpdata.setCheckCallback(new CheckCallback() {
                        @Override
                        public void onCheckStart() {
                            showLoading();
                        }

                        @Override
                        public void hasUpdate(Update update) {
                            hideLoading();
                        }

                        @Override
                        public void noUpdate() {
                            hideLoading();
                            showHint(HintDialog.HintType.WARN, "已是最新版本");
                        }

                        @Override
                        public void onCheckError(Throwable t) {
                            showHint(HintDialog.HintType.WARN, "检测更新失败");
                            hideLoading();
                        }

                        @Override
                        public void onUserCancel() {
                            showHint(HintDialog.HintType.WARN, "取消更新");
                            hideLoading();
                        }

                        @Override
                        public void onCheckIgnore(Update update) {
                            showHint(HintDialog.HintType.WARN, "已忽略该版本");
                            hideLoading();
                        }
                    });
                }
                mUpdata.check();
                break;
            case R.id.setting_other2_aboutUs_rl:
                getNavigator().navtoAboutUs(this);
                break;
            case R.id.setting_debug_btn_network_ok://动态切换网络
                if (!mSettingDebugEtNetwork.getText().toString().equals("")) {
                    try {
                        HostManager.setNetWorkType(Integer.parseInt(mSettingDebugEtNetwork.getText().toString()));
                    } catch (Exception e) {
                        showHint(HintDialog.HintType.WARN, "网络参数错误");
                    }
                }
                break;
            case R.id.setting_debug_btn_monitor:
                getNavigator().navtoMonitor(this);
                break;
        }
    }


    public void showHint(int type, String hint) {
        if (mHintDialog == null) {
            mHintDialog = new HintDialog(this, HintDialog.HintType.WARN);
            mHintDialog.showToast(HintDialog.LENGTH_SHORT);
            mHintDialog.setTitleText(hint);
        } else {
            if (mHintDialog.getType() != type) {
                mHintDialog.setType(type);
            }
            mHintDialog.setTitleText(hint);
            mHintDialog.showToast(HintDialog.LENGTH_SHORT);
        }
    }


    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new HintDialog(this, HintDialog.HintType.LOADING);
            mLoadingDialog.show();
            mLoadingDialog.setTitleText(getString(R.string.loading));
        } else if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    private void setVersionBadeg(String text) {
        if (mBadeg == null) {
            mBadeg = new QBadgeView(SettingActivity.this)
                    .bindTarget(findViewById(R.id.setting_other2_checkNew_enter_tv))
                    .setBadgeGravity(Gravity.END | Gravity.TOP)
                    .setBadgePadding(0, false)
                    .setBadgeTextSize(8, true);
        }
        mBadeg.setBadgeText(text);
    }

    private boolean isNewVersion() {
        if (ResourceManager.getInstance().getNewVersion() != null) {
            if (ResourceManager.getInstance().getNewVersion().getVersionCode() > BuildConfig.VERSION_CODE) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
//        if (connectionService != null)
//            unbindService(connectionService);
        super.onDestroy();
    }
}
