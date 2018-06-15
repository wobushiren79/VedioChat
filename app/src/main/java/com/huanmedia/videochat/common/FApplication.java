package com.huanmedia.videochat.common;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.huanmedia.ilibray.utils.SystemUtil;
import com.huanmedia.videochat.BuildConfig;
import com.huanmedia.videochat.common.local.LocationService;
import com.huanmedia.videochat.common.manager.ResourceManager;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.service.socket.WSConfig;
import com.huanmedia.videochat.common.service.socket.WSConnListener;
import com.huanmedia.videochat.common.service.socket.WebSocketManager;
import com.huanmedia.videochat.common.service.update.AllDialogShowStrategy;
import com.huanmedia.videochat.common.service.update.CustomApkFileCreator;
import com.huanmedia.videochat.common.service.update.CustomInstallNotifier;
import com.huanmedia.videochat.common.service.update.DialogDownloadCreator;
import com.huanmedia.videochat.common.service.update.DialogUpdateCreator;
import com.huanmedia.videochat.common.service.update.OkHttpDownloadWorker;
import com.huanmedia.videochat.common.service.update.OkhttpCheckWorker;
import com.huanmedia.videochat.common.utils.CrashHandler;
import com.huanmedia.videochat.common.utils.LocationHandler;
import com.huanmedia.videochat.repository.net.HostManager;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.model.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by ericYang on 2017/5/18.
 * Email:eric.yang@huanmedia.com
 * what?
 */

public class FApplication extends Application {
    private static Application mApplication;
    public static final String TAG = "FApplication";
    private static boolean sInitAiYa;
    public LocationService locationService;

    public static boolean isInitAiYa() {
        return sInitAiYa;
    }

    public static void setInitAiYa(boolean initAiYa) {
        sInitAiYa = initAiYa;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//
//        setUpLeakCanary();
        mApplication = this;
        String processName = getProcessName(this,
                android.os.Process.myPid());
        if (processName != null) {
            boolean defaultProcess = processName
                    .equals(getPackageName());
            if (defaultProcess) {
                //必要的初始化资源操作
                EventBus.builder().addIndex(new EventBusIndex()).installDefaultEventBus();
                initLocation();
                LitePal.initialize(this);
                initUpdate();
                crashConfig();
            }
        }
        initUMeng();
        initMap();
        initBugly();
        initVideo();
//        Takt.stock(this).seat(Seat.TOP_RIGHT).color(Color.WHITE).play();
    }

    /**
     * 初始化视频
     */
    private void initVideo() {
        VideoOptionModel videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp");
        List<VideoOptionModel> list = new ArrayList<>();
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_flags", "prefer_tcp");
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "allowed_media_types", "video"); //根据媒体类型来配置
        list.add(videoOptionModel);
//        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "timeout", 10000);
//        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "buffer_size", 0);
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "infbuf", 1);  // 无限读
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 1);
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 1);
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", 1);
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
        list.add(videoOptionModel);

        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
        list.add(videoOptionModel);

        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "analyzeduration", 1);
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 1);
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 5);
        list.add(videoOptionModel);

        GSYVideoManager.instance().setOptionModelList(list);
        GSYVideoManager.instance().setVideoType(this, GSYVideoType.IJKEXOPLAYER2);
        GSYVideoManager.instance().enableRawPlay(getApplication());
    }

    /**
     * 初始化BUGLY
     */
    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext());
    }

    /**
     * 初始化友盟
     */
    private void initUMeng() {
        if (!SystemUtil.getDeviceBrand().contains("Meizu")) {
            //友盟初始化
            UMConfigure.init(getApplicationContext(), UMConfigure.DEVICE_TYPE_PHONE, null);
            //友盟场景类型设置
            MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        }
    }

    /**
     * 初始化地图
     */
    private void initMap() {
        /***
         * 初始化定位sdk，建议在Application中创建
         */        //百度地图初始化
        locationService = new LocationService(getApplicationContext());
    }

    //    private void setUpLeakCanary(){
//        if (LeakCanary.isInAnalyzerProcess(this)||!BuildConfig.DEBUG) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        enabledStrictMode();
//        LeakCanary.install(this);
//    }
    private static void enabledStrictMode() {
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
//                .detectAll() //
//                .penaltyLog() //
//                .penaltyDeath() //
//                .build());
    }

    private void crashConfig() {
        CrashHandler.getInstance().init(this, BuildConfig.DEBUG);
        CrashHandler.setCrashTip("App有些任性，它自己休息了");
    }

    private void initUpdate() {
        UpdateConfig config = UpdateConfig.getConfig()
                .setUrl(HostManager.getServiceUrl() + "index/standard/version");
        config.getCheckEntity().setMethod("POST");
        config.setUpdateStrategy(new AllDialogShowStrategy());
        config.setDownloadNotifier(new DialogDownloadCreator())
                .setCheckNotifier(new DialogUpdateCreator())
                .setCheckWorker(OkhttpCheckWorker.class)
                .setInstallNotifier(new CustomInstallNotifier())
                .setFileCreator(new CustomApkFileCreator())
                .setDownloadWorker(OkHttpDownloadWorker.class);
        config.getCheckEntity().getParams().put("andVerNum", BuildConfig.VERSION_CODE + "");
        config.setUpdateParser(httpResponse -> {
            JSONObject object = new JSONObject(httpResponse);
            JSONObject data = object.getJSONObject("data");
            JSONObject version = data.getJSONObject("maxversion");
            Update update = new Update();
            // 此apk包的下载地址
            update.setUpdateUrl(version.optString("apkUrl"));
            // 此apk包的版本号
            update.setVersionCode(version.optInt("andVerNum"));
            // 此apk包的版本名称
            update.setVersionName(version.optString("version"));
            // 此apk包的更新内容
            update.setUpdateContent(version.optString("desc"));
            // 此apk包是否为强制更新
            String forced = version.optString("enforeUpdate");
            if (forced.equals("TRUE"))
                update.setForced(true);
            // 是否显示忽略此次版本更新按钮
//                        update.setIgnore(version.optBoolean("ignore_able",false));
            update.setMd5(version.optString("md5"));

            Long fileSize = version.optLong("fileSize", 0);
            update.setApkSize(fileSize);
            ResourceManager.getInstance().setNewVersion(update);
            return update;
        });
    }

    /**
     * 初始化用户位置信息
     */
    private void initLocation() {
        if (UserManager.getInstance().islogin()) {
            LocationHandler locationHandler = new LocationHandler();
            locationHandler.requestLocation(3);
        }
    }


    public static void initLocalSocket() {
        WebSocketManager.getInstance().destory();
        WebSocketManager.getInstance().connection(new WSConfig.Bulide()
                .setHost(HostManager.getSocketURI())
                .setParms(HostManager.defaultSocketPrams())
                .setConntimeOut(10 * 1000).bulide());
        WebSocketManager.getInstance().addConnectionListener(new WSConnListener() {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Observable.timer(200, TimeUnit.MILLISECONDS)
                        .observeOn(Schedulers.newThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(aLong -> WebSocketManager.getInstance().bindUser());
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                if (code != 1000 && UserManager.getInstance().islogin()) {
                    Observable.timer(3, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io()).subscribe(
                            aLong -> WebSocketManager.getInstance().reConnection()
                    );
                }
            }
        });
    }

    public static void destoryLocalSocket() {
        WebSocketManager.getInstance().destory();
    }


    public static Application getApplication() {
        return mApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
//        Takt.finish();
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        assert am != null;
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

}
