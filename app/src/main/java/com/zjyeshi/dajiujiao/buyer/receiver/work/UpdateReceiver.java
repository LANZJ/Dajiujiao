package com.zjyeshi.dajiujiao.buyer.receiver.work;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;

/**
 * 工作台 更新数据
 *
 * Created by zhum on 2016/6/22.
 */
public abstract class UpdateReceiver extends BroadcastReceiver {
    public static final String ACTION = App.instance.getApplicationContext().getPackageName() + UpdateReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        aliResult();
    }

    /**执行相应操作
     *
     */
    public abstract void aliResult();
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
