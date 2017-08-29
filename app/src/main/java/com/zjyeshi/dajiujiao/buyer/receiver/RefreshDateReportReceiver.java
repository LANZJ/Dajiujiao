package com.zjyeshi.dajiujiao.buyer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;

/**
 * Created by wuhk on 2016/10/1.
 */
public abstract class RefreshDateReportReceiver extends BroadcastReceiver {
    private static final String ACTION = App.instance.getApplicationContext().getPackageName() + RefreshDateReportReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        refresh();
    }

    public abstract void refresh();

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
