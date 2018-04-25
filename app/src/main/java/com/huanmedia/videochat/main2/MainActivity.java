package com.huanmedia.videochat.main2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPActivity;
import com.huanmedia.videochat.common.event.CoinChangeEvent;
import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.service.notifserver.HuaWeiPushHelper;
import com.huanmedia.videochat.common.service.notifserver.PushServiceManager;
import com.huanmedia.videochat.common.utils.UMengUtils;
import com.huanmedia.videochat.common.widget.NoviceGuidanceView;
import com.huanmedia.videochat.main2.datamodel.TabMode;
import com.huanmedia.videochat.main2.weight.ConditionEntity;
import com.huanmedia.videochat.main2.adapter.MainPageFragmentAdapter;
import com.huanmedia.videochat.main2.fragment.FriendFragment;
import com.huanmedia.videochat.main2.fragment.HomeFragment;
import com.huanmedia.videochat.main2.fragment.MainInteractionListener;
import com.huanmedia.videochat.main2.fragment.MyFragment;
import com.huanmedia.videochat.main2.weight.NoScrollViewPager;
import com.huanmedia.videochat.repository.entity.VideoChatEntity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.lzh.framework.updatepluginlib.UpdateBuilder;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

/**
 * 新版本主界面
 * -- 发现
 * --- 主界面-----ViewPageFragment -- 好友
 * mainPage ----                                -- 我的
 * --- 匹配
 */
public class MainActivity extends BaseMVPActivity<MainPresenter> implements MainView, MainInteractionListener, HomeFragment.HomeInterActionListener {

    @BindView(R.id.main_vp_page)
    NoScrollViewPager mMainVpPage;
    @BindView(R.id.main_commonTablayout)
    CommonTabLayout mMainCommonTablayout;
    @BindView(R.id.view_noviceguidance)
    NoviceGuidanceView guidanceView;

    private ArrayList<CustomTabEntity> mTabs;
    private Fragment[] mFragments;
    private Disposable rxGetCOmmonTablayoutHight;
    private boolean autoLogin;
    private UpdateBuilder mUpdata;
    private HuaWeiPushHelper mHuaweiPushHelper;
    private MainPageFragmentAdapter mAdapter;

    public static Intent getCallingIntent(Context context, boolean autoLogin) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("autoLogin", autoLogin);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        guidanceView.setShowData(NoviceGuidanceView.GuidanceType.FIND);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mHuaweiPushHelper != null) {
            mHuaweiPushHelper.destory();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //华为移动服务错误处理回调
        if (data != null)
            mHuaweiPushHelper.errorResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        HomeFragment homeFragment = (HomeFragment) mAdapter.getItem(0);
        if (mMainVpPage.getCurrentItem() == 0 && (homeFragment.getCurrentItem() == 1 || homeFragment.getCurrentItem() == -1)) {
            super.onBackPressed();
        } else {
            mMainVpPage.setCurrentItem(0);
            homeFragment.setCurrentItem(1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainAction(Intent action) {
        if (action == null || action.getAction() == null) return;
        switch (action.getAction()) {
            case EventBusAction.ACTION_SYSTEM_MESSAGE://系统消息更新
                int msgCount = 0;
                if (Objects.equals(action.getAction(), EventBusAction.ACTION_SYSTEM_MESSAGE)) {
                    msgCount = action.getIntExtra("msgCount", 0);
                    if (msgCount == 0) {
                        mMainCommonTablayout.hideMsg(2);
                    } else {
                        mMainCommonTablayout.showDot(2);
                    }
                }
                break;
            case EventBusAction.ACTION_USERINFO_UPDATE://用户数据更改主动更新
                getBasePresenter().upUserData();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAction(Intent action) {
        if (action == null || action.getAction() == null) return;
        switch (action.getAction()) {
            case EventBusAction.ACTION_COIN_CHANGED://金币更变

                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onCallingResult(VideoChatEntity entity) {
        if (entity.get_location_videoType() == ConditionEntity.VideoType.MATCH) {
            if (Check.isNull(entity.getCallid())) {//没有匹配成功需要调用强制退出搜索
                getBasePresenter().forceOutSearch();
            } else {//匹配成功需要关闭会话
                getBasePresenter().chatEnd(Integer.parseInt(entity.getCallid()), entity.get_endFlag());
            }
        } else {
            getBasePresenter().chatEnd(Integer.parseInt(entity.getCallid()), entity.get_endFlag());
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpDataCoin(CoinChangeEvent typeMode) {//支付成功
        getBasePresenter().coinChange();
    }


    @Override
    public boolean isImmersionBarEnabled() {
        return true;
    }

    @Override
    protected void initView() {
        checkNewVersion();
        mTabs = new ArrayList<>();
        mTabs.add(new TabMode("发现", R.drawable.tab_home_found_sel, R.drawable.tab_home_found_nor));
        mTabs.add(new TabMode("萌友", R.drawable.tab_home_friend_sel, R.drawable.tab_home_friend_nor));
        mTabs.add(new TabMode("我", R.drawable.tab_home_my_sel, R.drawable.tab_home_my_nor));

        mFragments = new Fragment[]{HomeFragment.newInstance(), FriendFragment.newInstance(), MyFragment.newInstance()};
        mAdapter = new MainPageFragmentAdapter(getSupportFragmentManager(), mFragments);
        mMainVpPage.setAdapter(mAdapter);
        mMainVpPage.setOffscreenPageLimit(3);
        mMainVpPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mMainCommonTablayout.setCurrentTab(position);
                switch (position) {
                    case 0:
                        guidanceView.setShowData(NoviceGuidanceView.GuidanceType.FIND);
                        break;
                    case 1:
                        guidanceView.setShowData(NoviceGuidanceView.GuidanceType.FRIEND);
                        break;
                    case 2:
                        guidanceView.setShowData(NoviceGuidanceView.GuidanceType.MY);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mMainCommonTablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mMainVpPage.setCurrentItem(position, true);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        mMainCommonTablayout.setTabData(mTabs);
        rxGetCOmmonTablayoutHight = RxCountDown.delay2(200).subscribe(integer ->
        {
            rxGetCOmmonTablayoutHight.dispose();
        }, throwable -> {
            rxGetCOmmonTablayoutHight.dispose();
            throwable.printStackTrace();
        });
    }

    /**
     * 版本检测
     */
    private void checkNewVersion() {
        mUpdata = UpdateBuilder.create();
//        mUpdata.checkWithDaemon(1000 * 5);
        mUpdata.check();
    }

    /**
     * 初始化推送服务
     * onCreate中初始化
     */
    private void initPushService() {
        if (PushServiceManager.getSystem().equals(PushServiceManager.SYS_EMUI) && HuaWeiPushHelper.suppertPush()) {
            //创建华为移动服务client实例用以使用华为push服务
            //需要指定api为HuaweiId.PUSH_API
            //连接回调以及连接失败监听
            mHuaweiPushHelper = new HuaWeiPushHelper(this);

        } else {
            PushServiceManager.getInstance(getApplication()).initPushService();
        }
    }

    @Override
    protected void initData() {
        super.initData();
        autoLogin = getIntent().getBooleanExtra("autoLogin", true);
        if (UserManager.getInstance().islogin()) {
            getBasePresenter().autoLogin(autoLogin);
        }
        //友盟统计红人日活
        UMengUtils.RedManActive(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPageChange(float offset) {
        //首页页面滑动
        ViewGroup.LayoutParams lp = mMainCommonTablayout.getLayoutParams();
        boolean change = (int) (getResources().getDimensionPixelOffset(R.dimen.dimen_104dp) * (offset)) != lp.height;
        if (change) {
            lp.height = (int) (getResources().getDimensionPixelOffset(R.dimen.dimen_104dp) * (offset));
            mMainCommonTablayout.setLayoutParams(lp);
        }

    }

    @Override
    public void enableMainPageSliding(boolean isEnable) {
        mMainVpPage.setNoScroll(!isEnable);
    }

    @Override
    public void onPageSelected(int position) {

        if (position == 0) {
            guidanceView.setShowData(NoviceGuidanceView.GuidanceType.MATCH);
        }
    }


    @Override
    public Context context() {
        return this;
    }
}
