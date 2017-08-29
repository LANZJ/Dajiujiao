package com.zjyeshi.dajiujiao.buyer.receiver.info;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;

/**
 * Created by whk on 2015/11/21.
 */
public abstract class SelectAddressReceiver extends BroadcastReceiver {
    public static final String ACTION = App.instance.getApplicationContext().getPackageName() + SelectAddressReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        showAddress();
    }

    /**
     * ִ����Ӧ����
     */
    public abstract void showAddress();

    /**
     * ע��㲥
     */
    public void register() {
        App.instance.registerReceiver(this, new IntentFilter(ACTION));
    }

    /**
     * ȡ��㲥
     */
    public void unRegister() {
        App.instance.unregisterReceiver(this);
    }

    /**
     * ���͹㲥
     */
    public static void notifyReceiver() {
        Intent intent = new Intent(ACTION);
        App.instance.sendBroadcast(intent);
    }
}
