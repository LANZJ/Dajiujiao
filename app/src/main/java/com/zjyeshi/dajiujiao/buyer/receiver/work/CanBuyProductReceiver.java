package com.zjyeshi.dajiujiao.buyer.receiver.work;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;

/**
 * Created by wuhk on 2016/12/14.
 */
public  abstract  class CanBuyProductReceiver extends BroadcastReceiver {
    public static final String ACTION = App.instance.getApplicationContext().getPackageName() + CanBuyProductReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        reload();
    }

    /**执行相应操作
     *
     */
    public abstract void  reload();
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
