package com.zjyeshi.dajiujiao.buyer.receiver.buy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;

/**
 * 获取收藏
 * Created by wuhk on 2015/11/11.
 */
public abstract class GetFollowShopReceiver extends BroadcastReceiver {
    public static final String ACTION = App.instance.getApplicationContext().getPackageName() + GetFollowShopReceiver.class.getSimpleName();
    public static String LAT = "lat";
    public static String LNG = "lng";

    @Override
    public void onReceive(Context context, Intent intent) {
        String lat = intent.getStringExtra(LAT);
        String lng = intent.getStringExtra(LNG);
        getShop(lat , lng);
    }

    public abstract void getShop(String lat , String lng);

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
    public static void notifyReceiver(String lat , String lng){
//        App.instance.sendBroadcast(new Intent(ACTION));
        Intent intent = new Intent(ACTION);
        intent.putExtra(LAT , lat);
        intent.putExtra(LNG , lng);
        App.instance.sendBroadcast(intent);
    }
}
