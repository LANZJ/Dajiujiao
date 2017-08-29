package com.jopool.crow.imkit.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.jopool.crow.CWChat;
import com.jopool.crow.imlib.entity.CWConversationMessage;

/**
 * 单个会话未读消息变动
 * 
 * @author xuan
 */
public abstract class CWConversationUnreadNumReceiver extends BroadcastReceiver {
	public static final String DG_CONVERSATION_UNREADNUM = "dg.conversation.unreadnum";

	public static final String PARAM_TO_ID = "param.to.id";

	@Override
	public void onReceive(Context context, Intent intent) {
		String toId = intent.getStringExtra(PARAM_TO_ID);
		int unreadNum = CWChat.getInstance().getConversationUnreadedNum(toId);
		CWConversationMessage lastMessage = CWChat.getInstance()
				.getLastMessageByToId(toId);
		conversationUnreadNum(toId, unreadNum, lastMessage);
	}

	/**
	 * 子类自己实现，用来刷新未读消息
	 * 
	 * @param totalUnreadNum
	 */
	public abstract void conversationUnreadNum(String toId, int totalUnreadNum,
			CWConversationMessage lastMessage);

	/**
	 * 注册广播接受
	 * 
	 * @param context
	 */
	public void register(Context context) {
		context.registerReceiver(this, new IntentFilter(
				DG_CONVERSATION_UNREADNUM));
	}

	/**
	 * 取消广播接受
	 * 
	 * @param context
	 */
	public void unregister(Context context) {
		context.unregisterReceiver(this);
	}

	/**
	 * 发送通知
	 * 
	 * @param context
	 * @param toId
	 */
	public static void notifyReceiver(Context context, String toId) {
		Intent intent = new Intent(DG_CONVERSATION_UNREADNUM);
		intent.putExtra(PARAM_TO_ID, toId);
		context.sendBroadcast(intent);
		// 单个会话未读消息变了，总的消息数那也要变
		CWTotalUnreadNumReceiver.notifyReceiver(context);
		//刷新会话列表
		CWRefreshConversationListReceiver.notifyReceiver(context);
	}

}
