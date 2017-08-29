package com.zjyeshi.dajiujiao.buyer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.jopool.crow.CWChat;
import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.frame.BaseFrameActivity;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.LogUtil;

/**
 * Created by wuhk on 2017/1/11.
 */

public class ScreenBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            // 开屏
            LogUtil.e("收到开屏广播");

        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            // 锁屏
            LogUtil.e("收到锁屏广播");
            if (LoginedUser.getLoginedUser().isLogined()){
                if (CWChat.getInstance().getImClient().isConnected()){
                    LogUtil.e("我要断开连接了");
                    CWChat.getInstance().getImClient().disConnect();
                }
            }
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
            // 解锁
            LogUtil.e("收到解锁广播");
            if (!BaseActivity.fromBackground && !BaseFrameActivity.fromBackground){
                if (LoginedUser.getLoginedUser().isLogined()){
                    if (!CWChat.getInstance().getImClient().isConnected()){
                        LogUtil.e("我重新连接了");
                        ExtraUtil.connectChat(context);
                    }
                }
            }
        }
    }

    public void register() {
        IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        App.instance.registerReceiver(this, filter);
    }

    public void unRegister() {
       App.instance.unregisterReceiver(this);
    }
}
