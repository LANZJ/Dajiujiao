package com.zjyeshi.dajiujiao.buyer.chat.consumer;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigapple.lib.utils.log.LogUtils;
import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.activity.frame.FrameActivity;
import com.zjyeshi.dajiujiao.buyer.utils.NotificationUtil;

/**
 * Created by wuhk on 2015/12/4.
 */
public class Invoker {

    /**
     * 处理个推透传消息推送
     *
     * @param context
     * @param customContentString
     */
    public static void consume(Context context, String title , String description ,  String customContentString) {
//
        // 如果customContentString空，点击直接打开应用好了
        if (Validators.isEmpty(customContentString)) {
            // 如果customContentString空，点击直接打开应用好了
            Intent intent = new Intent();
            intent.setClass(context.getApplicationContext(), FrameActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            NotificationUtil.showNotification(title , description , intent);

        } else {
            // 非空的话就根据协议处理对应的逻辑
            try {
                org.json.JSONObject obj = new org.json.JSONObject(
                        customContentString);
                String type = obj.getString("type");
                BasePush bc = null;
                if (BasePush.PUSH_NEW_CHAT_MESSAGE.equals(type)) {
                    bc = JSON.parseObject(customContentString,
                            PushNewChatMessage.class);
                }else  if (BasePush.PUSH_NEW_ORDER.equals(type)){
                    bc = JSON.parseObject(customContentString,
                            PushNewNoticeMessage.class);
                }else if (BasePush.PUSH_NEWREMIND_PUSH.equals(type)){
                    bc = JSON.parseObject(customContentString ,
                            PushNewRemindMessage.class);
                }else if (BasePush.PUSH_ORDER_REBACK.equals(type)){
                    bc = JSON.parseObject(customContentString ,
                            PushNewRemindMessage.class);
                }else if (BasePush.PUSH_CONTACTS_CHANGE_PUSH.equals(type)){
                    bc = JSON.parseObject(customContentString , PushNewRemindMessage.class);
                }
                bc.consume(context , title , description);
            } catch (Exception e) {
                LogUtils.e(e.getMessage(), e);
            }
        }
    }

//    /**
//     * 处理百度推送
//     *
//     * @param context
//     * @param customContentString
//     */
//    public static void consume(Context context, String customContentString) {
//        if (Validators.isEmpty(customContentString)) {
//            // 如果customContentString空，点击直接打开应用好了
//            Intent intent = new Intent();
//            intent.setClass(context.getApplicationContext(), FrameActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.getApplicationContext().startActivity(intent);
//        } else {
//            // 非空的话就根据协议处理对应的逻辑
//            try {
//                org.json.JSONObject obj = new org.json.JSONObject(
//                        customContentString);
//                String type = obj.getString("type");
//                BasePush bc = null;
//                if (BasePush.PUSH_NEW_CHAT_MESSAGE.equals(type)) {
//                    bc = JSON.parseObject(customContentString,
//                            PushNewChatMessage.class);
//                }else  if (BasePush.PUSH_NEW_ORDER.equals(type)){
//                    bc = JSON.parseObject(customContentString,
//                            PushNewNoticeMessage.class);
//                }else if (BasePush.PUSH_NEWREMIND_PUSH.equals(type)){
//                    bc = JSON.parseObject(customContentString ,
//                            PushNewRemindMessage.class);
//                }else if (BasePush.PUSH_ORDER_REBACK.equals(type)){
//                    bc = JSON.parseObject(customContentString ,
//                            PushNewRemindMessage.class);
//                }else if (BasePush.PUSH_CONTACTS_CHANGE_PUSH.equals(type)){
//                    bc = JSON.parseObject(customContentString , PushNewRemindMessage.class);
//                }
//                bc.consume(context);
//            } catch (Exception e) {
//                LogUtils.e(e.getMessage(), e);
//            }
//        }
//    }
}
