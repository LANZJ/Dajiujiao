package com.zjyeshi.dajiujiao.buyer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;

/**
 * 改变提醒显示
 * Created by wuhk on 2016/7/8.
 */
public abstract class ChangeRemindShowReceiver extends BroadcastReceiver {
    private static final String ACTION = App.instance.getApplicationContext().getPackageName() + ChangeRemindShowReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        change();
    }

    public abstract void change();

    public void register() {
        IntentFilter filter = new IntentFilter(ACTION);
        App.instance.registerReceiver(this, filter);
    }

    public void unregister() {
        App.instance.unregisterReceiver(this);
    }

    public static void notifyReceiver() {
        Intent intent = new Intent(ACTION);
        App.instance.sendBroadcast(intent);
    }
}
