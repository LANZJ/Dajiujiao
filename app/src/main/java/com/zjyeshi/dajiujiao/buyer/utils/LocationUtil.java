package com.zjyeshi.dajiujiao.buyer.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.List;

/**
 * 获取经纬度信息工具
 *
 * Created by wuhk on 2015/10/22.
 */
public class LocationUtil {
//    private int lat;
//    private int lng;
    private Location location;
    private String provider;

    public LocationUtil(Context context) {

        final LocationManager locationManager;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onLocationChanged(Location location) {
                // 更新当前设备的位置信息
            }
        };
        // 获取所有可用的位置提供器
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            // 当没有可用的位置提供器时，弹出Toast提示用户
            ToastUtil.toast("当前没有可用的位置提供器");
            return;
        }
        location = locationManager.getLastKnownLocation(provider);

        locationManager.requestLocationUpdates(provider, 5000, 1,locationListener );

    }
    public Location getLocation() {
        return location;
    }
}
