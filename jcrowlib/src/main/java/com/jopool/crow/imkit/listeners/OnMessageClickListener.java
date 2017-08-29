package com.jopool.crow.imkit.listeners;

import android.content.Context;
import android.view.View;

import com.jopool.crow.imlib.entity.CWConversationMessage;

/**
 * 聊天消息点击事件
 * 
 * @author xuan
 */
public interface OnMessageClickListener {

	/**
	 * 消息体点击
	 * 
	 * @param context
	 * @param view
	 * @param message
	 */
	void onMessageClick(Context context, View view,
						CWConversationMessage message);

	/**
	 * 重发按钮点击
	 * 
	 * @param context
	 * @param view
	 * @param message
	 */
	void onResendClick(Context context, View view, CWConversationMessage message);

}
