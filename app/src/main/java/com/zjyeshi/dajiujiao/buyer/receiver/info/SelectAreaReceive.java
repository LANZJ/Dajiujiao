package com.zjyeshi.dajiujiao.buyer.receiver.info;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;

/**
 * 选择地区广播
 * Created by wuhk on 2015/11/13.
 */
public abstract class SelectAreaReceive extends BroadcastReceiver {
    public static final String ACTION = App.instance.getApplicationContext().getPackageName() + SelectAreaReceive.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        closeActivity();
    }

    /**执行相应操作
     *
     */
    public abstract void closeActivity();
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
