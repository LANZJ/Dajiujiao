package com.zjyeshi.dajiujiao.buyer.receiver.sales;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.receiver.ChangeRemindShowReceiver;

/**
 * Created by wuhk on 2017/4/27.
 */

public abstract class SelectSalesJoinReceiver extends BroadcastReceiver {
    private static final String ACTION = App.instance.getApplicationContext().getPackageName() + SelectSalesJoinReceiver.class.getSimpleName();
    private static final String PARAM_SELELCT_IDS = "param.select.ids";
    private static final String PARAM_SELELCT_LIST = "param.select.list";

    public static final String JOIN_SHOP = "join.shop";
    public static final String JOIN_PRODUCT = "join.product";

    @Override
    public void onReceive(Context context, Intent intent) {
        String str = intent.getStringExtra(PARAM_SELELCT_IDS);
        String listJson = intent.getStringExtra(PARAM_SELELCT_LIST);
        change(str , listJson);
    }

    public abstract void change(String str , String listJson);


    public void register() {
        IntentFilter filter = new IntentFilter(ACTION);
        App.instance.registerReceiver(this, filter);
    }

    public void unregister() {
        App.instance.unregisterReceiver(this);
    }

    public static void notifyReceiver(String str , String listJson) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(PARAM_SELELCT_IDS , str);
        intent.putExtra(PARAM_SELELCT_LIST , listJson);
        App.instance.sendBroadcast(intent);
    }
}
