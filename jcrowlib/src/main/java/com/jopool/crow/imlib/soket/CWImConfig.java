package com.jopool.crow.imlib.soket;

import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.soket.listeners.OnConnectListener;

/**
 * 一些常量参数
 * 
 * @author xuan
 */
public class CWImConfig {
	private String host;
	private int port;
	private String appKey;

	private OnConnectListener onConnectListener;// 连接回调
	private CWUser user;// 登陆的用户信息

	/**
	 * 获取参数配置对象
	 * 
	 * @param host
	 * @param port
	 * @param appKey
	 * @param onConnectListener
	 * @param user
	 * @return
	 */
	public static CWImConfig obtainJpImConfig(String host, int port,
			String appKey, OnConnectListener onConnectListener, CWUser user) {
		CWImConfig config = new CWImConfig();
		config.setHost(host);
		config.setPort(port);
		config.setAppKey(appKey);
		config.setOnConnectListener(onConnectListener);
		config.setUser(user);
		return config;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public OnConnectListener getOnConnectListener() {
		return onConnectListener;
	}

	public void setOnConnectListener(OnConnectListener onConnectListener) {
		this.onConnectListener = onConnectListener;
	}

	public CWUser getUser() {
		return user;
	}

	public void setUser(CWUser user) {
		this.user = user;
	}

}
