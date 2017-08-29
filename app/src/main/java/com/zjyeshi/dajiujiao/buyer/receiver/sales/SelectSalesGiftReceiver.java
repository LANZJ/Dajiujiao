package com.zjyeshi.dajiujiao.buyer.receiver.sales;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;

/**
 * Created by wuhk on 2017/4/27.
 */

public abstract class SelectSalesGiftReceiver extends BroadcastReceiver {
    private static final String ACTION = App.instance.getApplicationContext().getPackageName() + SelectSalesGiftReceiver.class.getSimpleName();

    private static final String PARAM_GIFT_ID = "param.gift.id";
    private static final String PARAM_GIFT_NAME = "param.gift.name";

    @Override
    public void onReceive(Context context, Intent intent) {
        String giftId = intent.getStringExtra(PARAM_GIFT_ID);
        String giftName = intent.getStringExtra(PARAM_GIFT_NAME);
        dealWith(giftId , giftName);
    }

    public abstract void dealWith(String giftId , String giftName);


    public void register() {
        IntentFilter filter = new IntentFilter(ACTION);
        App.instance.registerReceiver(this, filter);
    }

    public void unregister() {
        App.instance.unregisterReceiver(this);
    }

    public static void notifyReceiver(String giftId , String giftName) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(PARAM_GIFT_ID , giftId);
        intent.putExtra(PARAM_GIFT_NAME , giftName);
        App.instance.sendBroadcast(intent);
    }
}
