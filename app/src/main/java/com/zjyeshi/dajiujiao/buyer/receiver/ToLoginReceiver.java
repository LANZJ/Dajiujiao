package com.zjyeshi.dajiujiao.buyer.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.activity.login.LoginActivity;

/**
 * 通知去登录
 *
 * Created by xuan on 15/11/5.
 */
public class ToLoginReceiver extends BroadcastReceiver {
    public static final String ACTION = App.instance.getApplicationContext().getPackageName() + ToLoginReceiver.class.getSimpleName();

    private Activity activity;
    public ToLoginReceiver(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }

    /**
     * 注册广播
     */
    public void register(){
        App.instance.registerReceiver(this , new IntentFilter(ACTION));
    }

    /**
     * 取消广播
     */
    public void unRegister(){
        App.instance.unregisterReceiver(this);
    }

    /**
     * 发送广播
     */
    public static void notifyReceiver(){
        App.instance.sendBroadcast(new Intent(ACTION));
    }

}
