package com.jopool.crow.imkit.listeners;

import android.content.Context;
import android.view.View;

import com.jopool.crow.imlib.entity.CWConversationMessage;

/**
 * 消息长按事件
 * 
 * @author xuan
 */
public interface OnMessageLongClickListener {

	/**
	 * 长按
	 * 
	 * @param context
	 * @param view
	 * @param message
	 */
	boolean onMessageLongClick(Context context, View view,
							   CWConversationMessage message);
}
