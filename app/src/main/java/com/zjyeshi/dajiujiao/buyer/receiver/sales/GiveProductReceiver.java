package com.zjyeshi.dajiujiao.buyer.receiver.sales;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;

/**
 * Created by wuhk on 2017/5/9.
 */

public abstract class GiveProductReceiver extends BroadcastReceiver {
    private static final String ACTION = App.instance.getApplicationContext().getPackageName() + GiveProductReceiver.class.getSimpleName();

    private static final String PARAM_WINE_ID = "param.wine.id";
    private static final String PARAM_WINE_NAME = "param.wine.name";

    @Override
    public void onReceive(Context context, Intent intent) {
        String wineId = intent.getStringExtra(PARAM_WINE_ID);
        String wineName = intent.getStringExtra(PARAM_WINE_NAME);
        dealWith(wineId , wineName);
    }


    public abstract void dealWith(String wineId , String wineName);


    public void register() {
        IntentFilter filter = new IntentFilter(ACTION);
        App.instance.registerReceiver(this, filter);
    }

    public void unregister() {
        App.instance.unregisterReceiver(this);
    }

    public static void notifyReceiver(String wineId , String wineName) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(PARAM_WINE_ID , wineId);
        intent.putExtra(PARAM_WINE_NAME , wineName);
        App.instance.sendBroadcast(intent);
    }
}
