package com.jopool.crow.imlib.soket.listeners;

import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.soket.CWErrorCode;

/**
 * 连接监听
 * 
 * @author xuan
 */
public interface OnConnectListener {

	/**
	 * 连接成功
	 * 
	 * @param user
	 */
	void onSuccess(CWUser user);

	/**
	 * 连接失败
	 * 
	 * @param errorCode
	 */
	void onError(CWErrorCode errorCode);

}
