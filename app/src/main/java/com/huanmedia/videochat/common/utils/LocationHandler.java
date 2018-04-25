package com.huanmedia.videochat.common.utils;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.ilibray.utils.data.assist.Network;
import com.huanmedia.videochat.common.FApplication;
import com.orhanobut.logger.Logger;

import io.reactivex.disposables.Disposable;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Create by Administrator
 * time: 2017/11/30.
 * Email:eric.yang@huanmedia.com
 * version: TODO 需要将定位修改为国内的定位SDK 因为默认位置经常无法获取数据
 */

@SuppressWarnings("MissingPermission")
public class LocationHandler {
    private final Context mContext;
    private int mMaxCount;
    private  int mCount;
    private LocationManager mLocationManager;
    String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private Disposable delayScribe;

    /**
     * 定位辅助类
     */
    public LocationHandler() {
        mContext = FApplication.getApplication();
        if (EasyPermissions.hasPermissions(mContext, perms)) {
            mLocationManager = (LocationManager) FApplication.getApplication().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Logger.i( "当前GPS位置改变%d",mCount);
            if (mMaxCount>0){
                mCount++;
                if (mCount>=mMaxCount){
                    disableLocation();
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                //GPS状态为可见时
                case LocationProvider.AVAILABLE:
                    Logger.i( "当前GPS状态为可见状态");
                    break;
                //GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
                    Logger.i( "当前GPS状态为服务区外状态");
                    break;
                //GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Logger.i( "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            Logger.i( "当前GPS状态为暂停服务状态");
            disableLocation();
        }
    };

    public void disableLocation() {
      disableLocation(false);
    }
    public void disableLocation(boolean isDelay) {
        if (isDelay){
            delayScribe.dispose();
            delayScribe=null;
        }
        if (mLocationManager != null) mLocationManager.removeUpdates(mLocationListener);
    }

    public Location getLocation() {
        if (mLocationManager != null) {
            String serviceString = Context.LOCATION_SERVICE;// 获取的是位置服务
            LocationManager locationManager = (LocationManager) FApplication.getApplication().getSystemService(serviceString);// 调用getSystemService()方法来获取LocationManager对象
            assert locationManager != null;
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            return location;
        } else {
            Logger.i("没有定位权限");
        }
        return null;
    }

    /**
     * @param count 定位次数
     *                 upCount < 0  需要手动取消
     */
    public void requestLocation(int count) {
        mMaxCount=count;
        if (mLocationManager != null) {
            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,
                        mLocationListener);
            } else if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                if (Network.isAvailable(mContext)) {
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1,
                            mLocationListener);
                }
            }
        }
        if (delayScribe!=null && !delayScribe.isDisposed()){
            delayScribe.dispose();
        }
        delayScribe= RxCountDown.delay(15).subscribe(
                integer -> disableLocation(true)
        );
    }
}
