package com.zjyeshi.dajiujiao.buyer.circle.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;

/**
 * Created by wuhk on 2015/11/22.
 */
public  abstract  class OnlyNotifyReceiver extends BroadcastReceiver {
    public static final String ACTION = OnlyNotifyReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        onlyNotify();
    }

    /**执行相应操作
     *
     */
    public abstract void onlyNotify();
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
