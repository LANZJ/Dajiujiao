package com.zjyeshi.dajiujiao.buyer.circle.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;

/**
 * Created by wuhk on 2015/11/19.
 */
public abstract  class OnlyRefrshReceiver extends BroadcastReceiver {
    public static final String ACTION = OnlyRefrshReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        refreshList();
    }

    /**执行相应操作
     *
     */
    public abstract void refreshList();
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
