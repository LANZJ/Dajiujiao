package com.jopool.crow.imkit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jopool.crow.CWChat;
import com.jopool.crow.imkit.adapter.itemview.CWChatMsgCenterView;
import com.jopool.crow.imkit.adapter.itemview.CWChatMsgLeftView;
import com.jopool.crow.imkit.adapter.itemview.CWChatMsgRightView;
import com.jopool.crow.imlib.entity.CWConversationMessage;
import com.jopool.crow.imlib.enums.CWMessageContentType;
import com.jopool.crow.imlib.enums.CWMessageType;

import java.util.List;

/**
 * 消息列表适配器
 * 
 * @author xuan
 */
public class ConversationMessageAdapter extends CWBaseAdapter {
	private final Context context;
	private final List<CWConversationMessage> dataList;

	public ConversationMessageAdapter(Context context,
			List<CWConversationMessage> dataList) {
		this.context = context;
		this.dataList = dataList;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		CWConversationMessage message = dataList.get(position);
		CWConversationMessage preMessage = null;
		if (position > 0) {
			preMessage = dataList.get(position - 1);
		}

		if(CWMessageContentType.CUSTOM.equals(message.getMessageContentType())){
			//自定义消息,判断用户是否自行处理
			Object object = CWChat.getInstance().getProviderDelegate().getCustomMessageProvider().getCustomView(context, preMessage, message);
			if(null != object && object instanceof View){
				return (View)object;
			}
			//如果用户没有返回自定义View,就按原消息继续显示
		}

		CWMessageType messageType = message.getMessageType();
		if (CWMessageType.CENTER.equals(messageType)) {
			view = new CWChatMsgCenterView(context).loadData(message);
		} else if (CWMessageType.LEFT.equals(messageType)) {
			view = new CWChatMsgLeftView(context).loadData(preMessage, message);
		} else if (CWMessageType.RIGHT.equals(messageType)) {
			view = new CWChatMsgRightView(context)
					.loadData(preMessage, message);
		} else {
			message.setContent("不支持该消息提醒，请升级到最新版本");
			view = new CWChatMsgCenterView(context).loadData(message);
		}

		return view;
	}

}
