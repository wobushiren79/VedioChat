package com.huanmedia.videochat.common.manager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.JsonSyntaxException;
import com.huanmedia.ilibray.utils.GsonUtils;
import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.ilibray.utils.data.cipher.Base64Cipher;
import com.huanmedia.videochat.common.FApplication;
import com.huanmedia.videochat.launch.StartActivity;
import com.huanmedia.videochat.repository.entity.UserEntity;
import com.huanmedia.videochat.repository.net.codes;
import com.huanmedia.videochat.video.CallingActivity;

import java.math.BigDecimal;
import java.util.Map;

import io.agora.propeller.model.ConstantApp;
import mvp.data.store.DataKeeper;

/**
 * 用户信息管理
 * Create by Administrator
 * time: 2017/11/15.
 * Email:eric.yang@huanmedia.com
 * version: 1.0
 */

public class UserManager {
    private volatile static UserManager instance;
    private static final String KEY_USER = "key_user";
    private static final String KEY_OVERALL_DIALOG_MSG = "overall_dialog_msg";
    private static final String KEY_NORED_MSG = "no_red_msg";
    private UserEntity mCurrentUser;
    private String sId;
    private DataKeeper mDataKeeper;
    private static final String DIFFERTIME="differTime";

    public static UserManager getInstance() {
        if (instance == null) {
            synchronized (UserManager.class) {
                if (instance == null) {
                    instance = new UserManager();
                }
            }
        }
        return instance;
    }

    private UserManager() {
        mDataKeeper = new DataKeeper(FApplication.getApplication(), DataKeeper.USERCONFIG);
        String jsonUser = (String) mDataKeeper.get(KEY_USER, new Base64Cipher());
        try {
            if (!Check.isEmpty(jsonUser))
            mCurrentUser=GsonUtils.getDefGsonBulder().create().fromJson(jsonUser,UserEntity.class);
        } catch (JsonSyntaxException e) {
            mDataKeeper.put(KEY_USER,null,new Base64Cipher());
        }
        if (mCurrentUser != null) {
            sId = mCurrentUser.getToken();
        }
    }

    public void saveUser(UserEntity currentUser) {
        if (currentUser==null) throw new RuntimeException("用户数据异常 ，用户数据保存不能NULL");
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(FApplication.getApplication());
        pref.edit().putInt(ConstantApp.PrefManager.PREF_PROPERTY_UID, (int) currentUser.getId()).apply();
        mCurrentUser = currentUser;
        this.sId = currentUser.getToken();
        mDataKeeper.put(KEY_USER, GsonUtils.getDefGsonBulder().create().toJson(mCurrentUser), new Base64Cipher());
    }
    public void saveUser() {
        if (mCurrentUser==null) throw new RuntimeException("用户数据异常 ，用户数据保存不能NULL");
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(FApplication.getApplication());
        pref.edit().putInt(ConstantApp.PrefManager.PREF_PROPERTY_UID, (int) mCurrentUser.getId()).apply();
        this.sId = mCurrentUser.getToken();
        mDataKeeper.put(KEY_USER, GsonUtils.getDefGsonBulder().create().toJson(mCurrentUser), new Base64Cipher());
    }

    public UserEntity getCurrentUser() {
        return mCurrentUser;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public long getId() {
        return mCurrentUser == null ? 0 : mCurrentUser.getId();
    }

    public boolean islogin() {
        return mCurrentUser != null && (!Check.isEmpty(mCurrentUser.getAutoString()));
    }
    public void outLogin(Map<String,Object> data) {
        if (data!=null)
        setOverallAffirm(data);
        mCurrentUser = null;
        this.sId = null;
        mDataKeeper.put(KEY_USER, null);
        instance=null;
        mDataKeeper=null;
    }
    public boolean isComplete() {
        return mCurrentUser != null && (mCurrentUser.getExtdataflag() == 1);
    }

    private int getTimeSync(String time) {
        long localTime = Long.parseLong(codes.getTimeTen());
        long remoteTime = Long.parseLong(time);
        return (int) (remoteTime-localTime);
    }

    public void saveTimeSync(String time) {
     mDataKeeper.putInt(DIFFERTIME,getTimeSync(time));
    }
    /**
     * 获取服务器同步时间
     * @return
     */
    public int timeSync(){
      return mDataKeeper.getInt(DIFFERTIME,0);
    }

    /**
     * 修正后的十位时间搓
     * @return
     */
    public  String reviseSyncTime(){
        long time= System.currentTimeMillis();
        time = Long.parseLong(String.valueOf(time).substring(0, 10));
        time=time + UserManager.getInstance().timeSync();
        return String.valueOf(time);
    }

    public String getOverallMsg() {
        return mDataKeeper.get(KEY_OVERALL_DIALOG_MSG, (String) null);
    }
    public void removeOverallAffirm() {
        mDataKeeper.put(KEY_OVERALL_DIALOG_MSG, null);
    }

    /**
     * 全局对话框类容
     * @param s {@code {title="title" ,msg="msg"}}
     */
    public void setOverallAffirm(Map<String,Object>  s) {
        if (s==null)return;
        mDataKeeper.put(KEY_OVERALL_DIALOG_MSG, GsonUtils.getDefGsonBulder().create().toJson(s));
    }

    public double getExchangeRate() {
        return 0.01;
    }

    public BigDecimal getRealMoney(int icon) {
        BigDecimal exchangecoin= new BigDecimal(icon);
        BigDecimal rate= new BigDecimal(UserManager.getInstance().getExchangeRate());
        return exchangecoin.multiply(rate).setScale(2,BigDecimal.ROUND_HALF_DOWN);
    }

    public String getRealMoneyStr(int icon) {
        return (getRealMoney(icon).toString());
    }

    public int getSystemMsgMaxid() {
        return mDataKeeper.getInt("maxSystemId",0);
    }

    public void setSystemMsgMaxId(int id) {
         mDataKeeper.putInt("maxSystemId",id);
    }

    public int getNoRedMsg() {
        return mDataKeeper.getInt(KEY_NORED_MSG, 0);
    }
    public void setNoRedMsg(int count) {
        mDataKeeper.putInt(KEY_NORED_MSG, count);
    }

    public void exit() {
        if (ActivitManager.getAppManager().existsActivity(CallingActivity.class)){
            ActivitManager.getAppManager().finishActivity(CallingActivity.class);
            RxCountDown.delay(1).subscribe(integer -> {
                Activity currentActivity = ActivitManager.getAppManager().currentActivity();
                Intent intent = new Intent(currentActivity, StartActivity.class);
                currentActivity.startActivity(intent);
                ActivitManager.getAppManager().finishNotSpecifiedActivity(StartActivity.class);
            });
        }else {
            Activity currentActivity = ActivitManager.getAppManager().currentActivity();
            Intent intent = new Intent(currentActivity, StartActivity.class);
            currentActivity.startActivity(intent);
            ActivitManager.getAppManager().finishNotSpecifiedActivity(StartActivity.class);
        }
    }
}
