package com.zjyeshi.dajiujiao.buyer.chat;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.utils.log.LogUtils;
import com.zjyeshi.dajiujiao.buyer.chat.consumer.Invoker;
import com.zjyeshi.dajiujiao.buyer.chat.consumer.UnReadNumPush;
import com.zjyeshi.dajiujiao.buyer.task.work.NewRemindTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.NewRemindData;
import com.zjyeshi.dajiujiao.buyer.utils.AppUnreadUtil;

import extended.PushReceiver;

/**
 * 百度推送通知接受在这里处理
 *
 * @author xuan
 */
public class MyPushReceiver extends PushReceiver {

    @Override
    public void onNotificationClicked(Context context, String title,
                                      String description, String customContentString) {
        // 收到推送消息，进行业务处理
//        LogUtils.e("标题[" + title + "]描述[" + description + "]自定义内容["
//                + customContentString + "]");
//        // 处理消费
//        Invoker.consume(context, customContentString);
    }


    //消息到达
    @Override
    public void onNotificationArrived(final Context context, String title, String description, String customContentString) {
        super.onNotificationArrived(context, title, description, customContentString);
//        UnReadNumPush push = JSON.parseObject(customContentString, UnReadNumPush.class);
//        AppUnreadUtil.sendBageNumberUseMySet(context, push.getContent().getCONTENTKEY_TOTAL_UNREAD_COUNT());
    }
}
