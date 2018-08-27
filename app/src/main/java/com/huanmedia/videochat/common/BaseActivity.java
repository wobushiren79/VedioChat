package com.huanmedia.videochat.common;

import android.app.ActivityManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OSUtils;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.manager.ActivitManager;
import com.huanmedia.videochat.common.manager.ResourceManager;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.navigation.Navigator;
import com.huanmedia.videochat.common.widget.SimpleToolBar;
import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * Base {@link android.app.Activity} class for every Activity in this application.
 */
public abstract class BaseActivity extends AppCompatActivity implements SimpleToolBar.OnToolbarClick, EasyPermissions.PermissionCallbacks {
    Navigator navigator = ResourceManager.getInstance().getNavigator();
    //    private boolean mFirstLoad = true;
    private SimpleToolBar mSimpleToolBar;
    public ImmersionBar mImmersionBar;
    private static final String NAVIGATIONBAR_IS_MIN = "navigationbar_is_min";
    private Unbinder mUnbinder;
    private Fragment mCurrentFragment;
    private MaterialDialog fouceDialog;

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    public boolean isImmersionBarEnabled() {
        return true;
    }

    @Override
    public void onActionBarClick(View view) {
        //toolbar 点击事件
    }

    /**
     * 华为手机隐藏导航栏监听
     */
    private ContentObserver mNavigationStatusObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            int navigationBarIsMin = Settings.System.getInt(getContentResolver(),
                    NAVIGATIONBAR_IS_MIN, 0);
            if (navigationBarIsMin == 1) {
                //导航键隐藏了
                mImmersionBar.transparentNavigationBar().init();
            } else {
                //导航键显示了
                mImmersionBar.navigationBarColor(android.R.color.black) //隐藏前导航栏的颜色
                        .fullScreen(false)
                        .init();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置近期任务栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(getString(R.string.app_name), BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo), getResources().getColor(R.color.color_f64d99));
            this.setTaskDescription(taskDesc);
        }

        ActivitManager.getAppManager().addActivity(this);
        if (0 != getLayoutId()) {
            setContentView(getLayoutId());
        }
        mUnbinder = ButterKnife.bind(this);
        if (OSUtils.isEMUI3_1() && isImmersionBarEnabled()) {
            //第一种
            getContentResolver().registerContentObserver(Settings.System.getUriFor
                    (NAVIGATIONBAR_IS_MIN), true, mNavigationStatusObserver);
            //第二种,禁止对导航栏的设置
            //mImmersionBar.navigationBarEnable(false).init();
        }
        initConfig();
        initView();
        initData();
    }

    protected abstract int getLayoutId();


    /**
     * 初始化界面
     */
    protected void initView() {
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 初始化配置信息
     */
    protected void initConfig() {
        if (isSimpleToolbarEnable()) {
            mSimpleToolBar = new SimpleToolBar(this);
            mSimpleToolBar.setClick(this);
        }

        if (isImmersionBarEnabled()) {
            statusBarConfig();
        }
    }

    protected ImmersionBar defaultBarConfig() {
        return ImmersionBar.with(this)
                .transparentStatusBar()
                .navigationBarEnable(false);
//                .statusBarDarkFont(true);
//                .statusBarColor(R.color.colorPrimaryDark);
    }

    protected View getTitlebarView() {
        if (mSimpleToolBar != null) {
            return mSimpleToolBar.getToolbarContent();
        }
        return null;
    }

    protected void statusBarConfig() {
        mImmersionBar = defaultBarConfig();
        if (getTitlebarView() != null) {
            mImmersionBar.titleBar(getTitlebarView());
        }
        mImmersionBar.init();
    }


    public SimpleToolBar getSimpleToolBar() {
        return mSimpleToolBar;
    }


    @Override
    protected void onResume() {
        super.onResume();
//        if (mFirstLoad) {
//            mFirstLoad = false;
//            initConfig();
//            initView();
//            initData();
//        }
        if (isShowFouceDialog())
            showFouceExitDialog();
        MobclickAgent.onResume(this);
    }

    public Navigator getNavigator() {
        return navigator;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivitManager.getAppManager().removeActivity(this);
        if (mUnbinder != null)
            mUnbinder.unbind();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
        if (fouceDialog != null) {
            fouceDialog.dismiss();
        }
    }

    public void showFouceExitDialog() {
        String msg = UserManager.getInstance().getOverallMsg();
        if (msg == null) return;
        runOnUiThread(() -> {
            Map<String, String> contents = new Gson().fromJson(msg, new TypeToken<Map<String, String>>() {
            }.getType());
            if (fouceDialog == null)
                try {
                    fouceDialog = new MaterialDialog.Builder(this)
                            .title(Check.checkReplace(contents.get("title")))
                            .content(Check.checkReplace(contents.get("msg")))
                            .dismissListener(dialog -> {
                                UserManager.getInstance().removeOverallAffirm();
                                UserManager.getInstance().exit();
                            })
                            .negativeText("确定")
                            .show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public boolean isSimpleToolbarEnable() {
        return false;
    }

    protected void addFragmentAndShow(int content, Fragment fragment, String tag) {
        addFragmentAndShow(content, fragment, tag, false, false);
    }

    protected void addFragmentAndShow(int content, Fragment targetFragment, String tag, boolean isAddBack, boolean removeCurrent) {
        addFragmentAndShow(content, targetFragment, tag, isAddBack, removeCurrent, null);
    }

    protected void addFragmentAndShow(int content, Fragment targetFragment, String tag, boolean isAddBack, boolean removeCurrent, int[] amimRes) {
        FragmentTransaction fmanager = getSupportFragmentManager().beginTransaction();
        if (mCurrentFragment != null) {
            if (removeCurrent) {
                fmanager.remove(mCurrentFragment);
                mCurrentFragment = null;
            }
        }
        setFragmentAnim(amimRes, fmanager);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (isAddBack) {
            if (fragment != null) {
                if (mCurrentFragment != null) {
                    fmanager.hide(mCurrentFragment);
                }
                fmanager.show(fragment);

            } else {
                fmanager.add(content, targetFragment, tag).addToBackStack(tag).show(targetFragment);
            }
        } else {
            if (fragment != null) {
                if (mCurrentFragment != null) {
                    fmanager.hide(mCurrentFragment);
                }
                fmanager.show(fragment);
            } else {
                fmanager.add(content, targetFragment, tag).show(targetFragment);
            }
        }

        if (fmanager != null) {
            fmanager.commit();
        }
        mCurrentFragment = targetFragment;
    }

    private void setFragmentAnim(int[] amimRes, FragmentTransaction fmanager) {
        if (amimRes != null) {
            fmanager.setCustomAnimations(amimRes[0], amimRes[1], amimRes[2], amimRes[3]);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    public boolean isShowFouceDialog() {
        return true;
    }

    /**
     * 检测权限
     *
     * @param perms
     */
    public boolean checkPermission(String[] perms) {
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "使用该功能需要相应权限", 0, perms);
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //将请求结果传递EasyPermission库处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 请求权限成功。
     * 可以弹窗显示结果，也可执行具体需要的逻辑操作
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //ToastUtils.showToastShortInCenter(this,"请求权限成功");
    }

    /**
     * 请求权限失败
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        /**
         * 若是在权限弹窗中，用户勾选了'NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。
         * 这时候，需要跳转到设置界面去，让用户手动开启。
         */
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
