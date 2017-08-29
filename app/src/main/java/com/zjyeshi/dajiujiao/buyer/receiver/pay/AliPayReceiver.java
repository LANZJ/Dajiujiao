package com.zjyeshi.dajiujiao.buyer.receiver.pay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;

/**
 * Created by wuhk on 2015/12/18.
 */
public abstract class AliPayReceiver extends BroadcastReceiver {
    public static final String ACTION = App.instance.getApplicationContext().getPackageName() + AliPayReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String result = intent.getStringExtra("result");
        aliResult(result);
    }

    /**执行相应操作
     *
     */
    public abstract void aliResult(String result);
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
    public static void notifyReceiver(String result){
        Intent intent = new Intent(ACTION);
        intent.putExtra("result" , result);
        App.instance.sendBroadcast(intent);
    }
}
