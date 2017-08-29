package com.jopool.crow.imlib.soket.listeners;

/**
 * 消息接受监听
 * 
 * @author xuan
 */
public interface OnMessageReceiveListener {

	/**
	 * 收到消息回调
	 * 
	 * @param messageObj
	 */
	boolean onReceive(Object messageObj);
}
