package com.zjyeshi.dajiujiao.buyer.receiver.info;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;

/**
 * 修改头像后发的通知
 *
 * Created by xuan on 15/11/25.
 */
public abstract class ChangeAvatorReceiver extends BroadcastReceiver {
    public static final String ACTION = App.instance.getApplicationContext().getPackageName() + ChangeAvatorReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        changeAvator();
    }

    /**执行相应操作
     *
     */
    public abstract void changeAvator();
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
        Intent intent = new Intent(ACTION);
        App.instance.sendBroadcast(intent);
    }

}
