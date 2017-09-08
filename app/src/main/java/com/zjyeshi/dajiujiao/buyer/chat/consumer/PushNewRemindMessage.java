package com.zjyeshi.dajiujiao.buyer.chat.consumer;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.activity.frame.FrameActivity;
import com.zjyeshi.dajiujiao.buyer.activity.frame.fragment.MianFramActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.WorkActivity;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.enums.UserEnum;
import com.zjyeshi.dajiujiao.buyer.utils.NotificationUtil;

/**
 * 新提醒
 * Created by wuhk on 2016/7/8.
 */
public class PushNewRemindMessage extends BasePush {
    @Override
    protected void consume(Context context , String title , String description) {
        //百度推送处理
        //此类消息在线通过聊天消息方式，收到百度推送通知肯定是离线状态，直接启动应用
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)){
            intent.setClass(context.getApplicationContext() , WorkActivity.class);
        }else{
          //  FrameActivity.tab3Checked = true;
            intent.setClass(context.getApplicationContext(), MianFramActivity.class);
        }
        NotificationUtil.showNotification(title , description , intent);
    }
//    @Override
//    protected void consume(Context context) {
//        //百度推送处理
//        //此类消息在线通过聊天消息方式，收到百度推送通知肯定是离线状态，直接启动应用
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)){
//            intent.setClass(context.getApplicationContext() , WorkActivity.class);
//        }else{
//            FrameActivity.tab3Checked = true;
//            intent.setClass(context.getApplicationContext(), FrameActivity.class);
//        }
//        context.getApplicationContext().startActivity(intent);
//    }
}
