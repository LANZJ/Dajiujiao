package com.zjyeshi.dajiujiao.buyer.chat.consumer;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.WorkActivity;
import com.zjyeshi.dajiujiao.buyer.activity.order.MyOrderNewActivity;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.UserEnum;
import com.zjyeshi.dajiujiao.buyer.activity.frame.FrameActivity;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.utils.NotificationUtil;

/**
 * 新订单
 * Created by wuhk on 2016/7/1.
 */
public class PushNewNoticeMessage extends BasePush {

    @Override
    protected void consume(Context context , String title , String description) {
        if (!Constants.isAlive){
            //程序没启动,先跳转到相应的启动界面，在根据传入的type，进行相应处理
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(BasePush.PARAM_NOTICE_TYPE , BasePush.PUSH_NEW_ORDER);
            if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)){
                intent.setClass(context.getApplicationContext() , WorkActivity.class);
            }else{
                FrameActivity.tab3Checked = true;
                intent.setClass(context.getApplicationContext(), FrameActivity.class);
            }
            NotificationUtil.showNotification(title , description , intent);
        }else{
            //应用启动过，直接打开相应界面
            Intent intent = new Intent();
            intent.setClass(context.getApplicationContext() , MyOrderNewActivity.class);
            intent.putExtra(MyOrderNewActivity.USER_TYPE , LoginEnum.SELLER.toString());
            if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)){
                intent.putExtra(MyOrderNewActivity.ROLE , "new");
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            NotificationUtil.showNotification(title , description , intent);
        }

    }

//    @Override
//    protected void consume(Context context) {
//        if (!Constants.isAlive){
//            //程序没启动,先跳转到相应的启动界面，在根据传入的type，进行相应处理
//            Intent intent = new Intent();
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra("type" , "NEW_ORDER");
//            if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)){
//                intent.setClass(context.getApplicationContext() , WorkActivity.class);
//            }else{
//                FrameActivity.tab3Checked = true;
//                intent.setClass(context.getApplicationContext(), FrameActivity.class);
//            }
//            context.getApplicationContext().startActivity(intent);
//        }else{
//            //应用启动过，直接打开相应界面
//            Intent intent = new Intent();
//            intent.setClass(context.getApplicationContext() , MyOrderNewActivity.class);
//            intent.putExtra(MyOrderNewActivity.USER_TYPE , LoginEnum.SELLER.toString());
//            if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)){
//                intent.putExtra(MyOrderNewActivity.ROLE , "new");
//            }
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.getApplicationContext().startActivity(intent);
//        }
//
//    }
}
