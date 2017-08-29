package com.zjyeshi.dajiujiao.buyer.chat.consumer;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.frame.BaseFrameActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.WorkActivity;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.activity.frame.FrameActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.MyMessageActivity;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.enums.UserEnum;
import com.zjyeshi.dajiujiao.buyer.utils.LogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.NotificationUtil;

/**
 * 聊天消息推送
 * Created by wuhk on 2015/12/4.
 */
public class PushNewChatMessage extends BasePush {
    public static final String PARAM_ID = "param.id";
    public static final String PARAM_NAME = "param.name";

    private PushNewChatMessage.Content content;

    public static class Content {
        private String conversationType;
        private String toId;

        public String getConversationType() {
            return conversationType;
        }

        public void setConversationType(String conversationType) {
            this.conversationType = conversationType;
        }

        public String getToId() {
            return toId;
        }

        public void setToId(String toId) {
            this.toId = toId;
        }
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    @Override
    protected void consume(Context context , String title , String description) {

        if (!Constants.isAlive) {
            //应用没启动,先跳转到相应的启动界面，在根据传入的type，进行相应处理
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //现在不直接进入聊天界面,就不传Id和name了
            intent.putExtra(BasePush.PARAM_NOTICE_TYPE, PUSH_NEW_CHAT_MESSAGE);
            if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
                intent.setClass(context.getApplicationContext(), WorkActivity.class);
            } else {
                intent.setClass(context.getApplicationContext(), FrameActivity.class);
            }
            LogUtil.e("关掉应用之后打开");
            NotificationUtil.showNotification(title , description , intent);
        } else {
            //应用启动过，直接打开相应界面,启动过，就先不跳了
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
                BaseFrameActivity.fromBackground = true;
                intent.setClass(context.getApplicationContext(), MyMessageActivity.class);
            } else {
                //调到消息tab页面
                BaseFrameActivity.fromBackground = true;
                FrameActivity.tab1Checked = true;
                intent.setClass(context.getApplicationContext(), FrameActivity.class);
            }
            LogUtil.e("没有关掉应用之后打开");
            NotificationUtil.showNotification(title , description , intent);
        }

    }
//    @Override
//    protected void consume(Context context) {
//
//        if (!Constants.isAlive) {
//            //应用没启动,先跳转到相应的启动界面，在根据传入的type，进行相应处理
//            Intent intent = new Intent();
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            //现在不直接进入聊天界面,就不传Id和name了
//            intent.putExtra(PARAM_TYPE, PUSH_NEW_CHAT_MESSAGE);
//            if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
//                intent.setClass(context.getApplicationContext(), WorkActivity.class);
//            } else {
//                intent.setClass(context.getApplicationContext(), FrameActivity.class);
//            }
//            context.getApplicationContext().startActivity(intent);
//        } else {
//            //应用启动过，直接打开相应界面,启动过，就先不跳了
//            Intent intent = new Intent();
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
//                BaseFrameActivity.fromBackground = true;
//                intent.setClass(context.getApplicationContext(), MyMessageActivity.class);
//            } else {
//                //调到消息tab页面
//                BaseFrameActivity.fromBackground = true;
//                FrameActivity.tab1Checked = true;
//                intent.setClass(context.getApplicationContext(), FrameActivity.class);
//            }
//            context.startActivity(intent);
//        }
//
//    }

}
