package com.xuan.bigdog.lib.location;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

/**
 * 百度定位工具类
 * 
 * @author xuan
 * 
 */
public class DGLocationUtils {
	private static LocationClient locationClient;

	public static LocationClient getLocationClient() {
		return locationClient;
	}

	public static void setLocationClient(LocationClient locationClient) {
		DGLocationUtils.locationClient = locationClient;
	}

	/**
	 * 初始化
	 * 
	 * @param application
	 * @param l
	 */
	public static void init(Context application, BDLocationListener l) {
		if (null != locationClient) {
			locationClient.stop();
			locationClient = null;
		}

		locationClient = new LocationClient(application);
		locationClient.registerLocationListener(l);
	}

	/** 创建一个Option */
	private static LocationClientOption createLocationClientOption() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 高精度
		option.setCoorType("bd09ll");// gcj02：国测局加密坐标值，bd09ll:百度加密坐标值，bd09：百度加密墨卡托坐标
		//option.setAddrType("all");
		option.setIsNeedAddress(true);// 是否需要反地理编码
		return option;
	}

	/**
	 * 开始定位
	 */
	public static void start() {
		checkNull();
		LocationClientOption option = createLocationClientOption();
		locationClient.setLocOption(option);
		locationClient.start();
	}

	/**
	 * 循环定位
	 * 
	 * @param interval
	 *            单位毫秒
	 */
	public static void startLoopLocation(int interval) {
		checkNull();
		LocationClientOption option = createLocationClientOption();
		option.setScanSpan(interval);
		locationClient.setLocOption(option);
		locationClient.start();
	}

	/**
	 * 结束定位
	 */
	public static void stop() {
		if (null != locationClient) {
			locationClient.stop();
			locationClient = null;
		}
	}

	/**
	 * 判断是否启动着
	 * 
	 * @return
	 */
	public static boolean isStarted() {
		checkNull();
		return locationClient.isStarted();
	}

	// 检查空
	private static void checkNull() {
		if (null == locationClient) {
			throw new NullPointerException("Call BGLocationUtils.init fisrt.");
		}
	}

}
