package com.huanmedia.videochat.common.service.notifserver;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import com.huanmedia.ilibray.utils.data.assist.Check;
import com.igexin.sdk.PushManager;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by ericYang on 2017/9/12.
 * Email:eric.yang@huanmedia.com
 * what?
 */

public class PushServiceManager {
    private static final String TAG = "PushServiceManager";
    private volatile static  PushServiceManager INSTANCE;
    private final Application mContext;
    private String APP_ID="2882303761517711866";
    private String APP_KEY="5951771135866";
    public static final String SYS_EMUI = "sys_emui";
    public static final String SYS_MIUI = "sys_miui";
    public static final String SYS_FLYME = "sys_flyme";
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
    private static final String KEY_EMUI_VERSION = "ro.build.version.emui";
    private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";


    public PushServiceManager(Application context) {
        this.mContext = context;
    }
    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = mContext.getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public void initPushService() {
        if (getSystem().equals(SYS_MIUI)){
            if (shouldInit()){
                MiPushClient.registerPush(mContext, APP_ID, APP_KEY);
            }
            LoggerInterface newLogger = new LoggerInterface() {

                @Override
                public void setTag(String tag) {
                    // ignore
                }

                @Override
                public void log(String content, Throwable t) {
                    Log.d(TAG, content, t);
                }

                @Override
                public void log(String content) {
                    Log.d(TAG, content);
                }
            };
            Logger.setLogger(mContext, newLogger);
        }else {
            PushManager.getInstance().initialize(mContext, DemoPushService.class);
            PushManager.getInstance().registerPushIntentService(mContext, DemoIntentService.class);//启动推送服务
        }
    }

    public static PushServiceManager getInstance(Application application) {
        if (INSTANCE==null){
            synchronized (PushServiceManager.class){
                if (INSTANCE==null){
                    INSTANCE = new PushServiceManager(application);
                }
            }
        }
        return INSTANCE;
    }

    public static String getSystem(){
        String SYS = "";
        try {
//            Properties prop= new Properties();
//            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            if(!Check.isEmpty(getSystemProperty(KEY_MIUI_VERSION_CODE))
                    || !Check.isEmpty(getSystemProperty(KEY_MIUI_VERSION_NAME))
                    || !Check.isEmpty(getSystemProperty(KEY_MIUI_INTERNAL_STORAGE))){
                SYS = SYS_MIUI;//小米
            }else if(!Check.isEmpty(getSystemProperty(KEY_EMUI_API_LEVEL))
                    ||!Check.isEmpty(getSystemProperty(KEY_EMUI_VERSION))
                    ||!Check.isEmpty(getSystemProperty(KEY_EMUI_CONFIG_HW_SYS_VERSION))){
                SYS = SYS_EMUI;//华为
            }else if(getMeizuFlymeOSFlag().toLowerCase().contains("flyme")){
                SYS = SYS_FLYME;//魅族
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return SYS;
    }
    public static String getSystemProperty(String propName){
        String line;
        BufferedReader input = null;
        try
        {
            java.lang.Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        }
        catch (IOException ex)
        {
            Log.e(TAG, "Unable to read sysprop " + propName, ex);
            return null;
        }
        finally
        {
            if(input != null)
            {
                try
                {
                    input.close();
                }
                catch (IOException e)
                {
                    Log.e(TAG, "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }



    public static String getMeizuFlymeOSFlag() {
        return getSystemProperty("ro.build.display.id", "");
    }

    private static String getSystemProperty(String key, String defaultValue) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("get", String.class, String.class);
            return (String)get.invoke(clz, key, defaultValue);
        } catch (Exception e) {
        }
        return defaultValue;
    }
}
