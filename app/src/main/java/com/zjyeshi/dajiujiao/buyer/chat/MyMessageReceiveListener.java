package com.zjyeshi.dajiujiao.buyer.chat;

import android.app.PendingIntent;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.jopool.crow.imlib.soket.listeners.OnMessageReceiveListener;
import com.jopool.jppush.common.message.CommonMessage;
import com.xuan.bigapple.lib.utils.log.LogUtils;
import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.activity.frame.FrameActivity;
import com.zjyeshi.dajiujiao.buyer.chat.consumer.BasePush;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.UserEnum;
import com.zjyeshi.dajiujiao.buyer.activity.order.MyOrderNewActivity;
import com.zjyeshi.dajiujiao.buyer.receiver.ContactsChangeReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.LoadNewRemindReceiver;
import com.zjyeshi.dajiujiao.buyer.utils.NotificationUtil;


/**
 * 除了聊天消息，其他消息接受在这里处理
 *
 * @author xuan
 */
public class MyMessageReceiveListener implements OnMessageReceiveListener {
	public static final String JP_DXNOTICEMESSAGE = "JP:DXNOTICEMESSAGE";

	@Override
	public boolean onReceive(Object messageObj) {
		if (messageObj instanceof CommonMessage) {
			CommonMessage conversationMessage = (CommonMessage) messageObj;

			if (JP_DXNOTICEMESSAGE.equals(conversationMessage.getMessageType())) {
				RemindContent remindMessage = JSON.parseObject(conversationMessage.getContent() , RemindContent.class);
				if (remindMessage.getType().equals(BasePush.PUSH_NEWREMIND_PUSH)){
					//发起改变提醒显示的广播
					LoadNewRemindReceiver.notifyReceiver();
					return true;
				}else if (remindMessage.getType().equals(BasePush.PUSH_NEW_ORDER)){
					//自己定义一个通知点击
					Intent intent = new Intent();
					intent.setClass(com.zjyeshi.dajiujiao.buyer.App.instance , MyOrderNewActivity.class);
					intent.putExtra(MyOrderNewActivity.USER_TYPE , LoginEnum.SELLER.toString());
					if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)){
						intent.putExtra(MyOrderNewActivity.ROLE , "new");
					}
					NotificationUtil.showNotification(remindMessage.getTitle() ,remindMessage.getContent() , intent);
					LoadNewRemindReceiver.notifyReceiver();
				}else if (remindMessage.getType().equals(BasePush.PUSH_ORDER_REBACK)){
					//自己定义一个通知
					Intent intent = new Intent();
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setClass(App.instance, FrameActivity.class);
					NotificationUtil.showNotification(remindMessage.getTitle() ,remindMessage.getContent() , intent);
				}else if (remindMessage.getType().equals(BasePush.PUSH_CONTACTS_CHANGE_PUSH)){
					//刷新通讯录
					ContactsChangeReceiver.notifyReceiver();
				}
			} else {
				LogUtils.e("未知MessageType");
			}
		} else {
			LogUtils.e("没有实现该消息的接受支持");
		}

		return false;
	}

}
