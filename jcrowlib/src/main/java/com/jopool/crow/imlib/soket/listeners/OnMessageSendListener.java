package com.jopool.crow.imlib.soket.listeners;

import com.jopool.crow.imlib.entity.CWConversationMessage;
import com.jopool.crow.imlib.soket.CWErrorCode;

/**
 * 发送消息回调
 * 
 * @author xuan
 */
public interface OnMessageSendListener {

	/**
	 * 发送成功
	 * 
	 * @param message
	 */
	void onSuccess(CWConversationMessage message);

	/**
	 * 发送失败
	 * 
	 * @param message
	 * @param errorCode
	 */
	void onError(CWConversationMessage message, CWErrorCode errorCode);

}
