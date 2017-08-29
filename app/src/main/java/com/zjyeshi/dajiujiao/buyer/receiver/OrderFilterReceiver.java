package com.zjyeshi.dajiujiao.buyer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;

/**
 * Created by wuhk on 2016/8/25.
 */
public abstract class OrderFilterReceiver extends BroadcastReceiver {
    public static final String ACTION =  App.instance.getApplicationContext().getPackageName() + OrderFilterReceiver.class.getSimpleName();
    public static final String MEMBERID = "member_id";
    @Override
    public void onReceive(Context context, Intent intent) {
        String memberId = intent.getStringExtra(MEMBERID);
        filter(memberId);
    }

    public abstract void filter(String memberId);

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
    public static void notifyReceiver(String memberId){
        Intent intent = new Intent(ACTION);
        intent.putExtra(MEMBERID , memberId);
        App.instance.sendBroadcast(intent);
    }
}
