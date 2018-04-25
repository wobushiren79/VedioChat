package com.huanmedia.videochat.repository.net;

import com.huanmedia.ilibray.utils.Installation;
import com.huanmedia.videochat.BuildConfig;
import com.huanmedia.videochat.common.FApplication;
import com.huanmedia.videochat.common.manager.ResourceManager;
import com.huanmedia.videochat.common.manager.UserManager;

import java.util.HashMap;
import java.util.Map;

import mvp.data.store.DataKeeper;

/**
 * Create by Administrator
 * time: 2017/11/22.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class HostManager {
    //    public final static  String SERVICE="http://short.im.rockhippo.cn/";
    private static  String SERVICE;
    private static  String DOMAINNAME;
    private static  String HTML;
    private static  String PROTOCOL="http://";
    private static int netWorkType;
    /**
     * socket连接地址
     */
//    public final static String SOCKETURL="ws://long.im.rockhippo.cn:7272?appid=1&account=admin&sid=%s&uid=%d&token=%s";
    private   static String SOCKETURL;
   public static String getServiceUrl(){
        if (SERVICE==null){
            SERVICE=PROTOCOL+String.format(getDomainName(),"mmapi")+"/";//远程
        }
        return SERVICE;
    }
    public static String getHtmlUrl(){
        if (HTML==null){
            HTML=PROTOCOL+String.format(getDomainName(),"mmimg")+"/";//远程
        }
        return HTML;
    }
     public static String getSocketURI(){
        if (SOCKETURL==null){
            SOCKETURL ="ws://"+String.format(getDomainName(),"mmpush")+(getNetWorkType()==2?":8273":":8272");//测试环境会用8273端口
//            SOCKETURL ="ws://192.168.23.27"+":8272";
        }
        return SOCKETURL;
    }
    public static Map<String, String> defaultSocketPrams(){
        Map<String, String> parms = new HashMap<>();
        parms.put("appid", 1 + "");
        parms.put("time", UserManager.getInstance().reviseSyncTime());
        parms.put("token", UserManager.getInstance().getCurrentUser().getToken());
        parms.put("deviceid", Installation.id(FApplication.getApplication()));
        return parms;
    }

    public static void setNetWorkType(int netWorkType) {
        HostManager.netWorkType = netWorkType;
        ResourceManager.getInstance().reset();
        new DataKeeper(FApplication.getApplication(),DataKeeper.DEFULTFILE).putInt("debug_network",getNetWorkType());
        SOCKETURL=null;
        DOMAINNAME=null;
        HTML=null;
        SERVICE=null;
        UserManager.getInstance().outLogin(null);
        UserManager.getInstance().exit();
    }

    public static int getNetWorkType() {
        if (netWorkType==0){
            netWorkType=new DataKeeper(FApplication.getApplication(),DataKeeper.DEFULTFILE).getInt("debug_network",0);
        }
        return  netWorkType ==0? BuildConfig.netWorkType: netWorkType;
    }

    public static String  getDomainName(){
        if (DOMAINNAME==null){
            int type =getNetWorkType();
            switch (type){
                case 1://开发
                    DOMAINNAME ="%s-dev.lzwifi.com";
                    break;
                case 2://测试
                    DOMAINNAME ="%s-test.lzwifi.com";
                    break;
                case 3://生产
                    DOMAINNAME ="%s.lzwifi.com";
                    break;
            }
        }
        if (DOMAINNAME==null){
            throw  new RuntimeException("非法的编译类型，检查Gradle文件buildTypes 是否在ApiService中有注册");
        }
        return DOMAINNAME;
    }


}
