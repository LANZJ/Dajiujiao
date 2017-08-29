package com.zjyeshi.dajiujiao.buyer.receiver.order;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;

/**
 * Created by wuhk on 2016/1/14.
 */
public abstract class OrderCommentReceiver extends BroadcastReceiver {
    public static final String ACTION =App.instance.getApplicationContext().getPackageName() + OrderCommentReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        int position = intent.getIntExtra(PassConstans.POSITION , -1);
        changeStatus(position);
    }

    /**执行相应操作
     *
     */
    public abstract void changeStatus(int position);
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
    public static void notifyReceiver(int position){
        Intent intent = new Intent(ACTION);
        intent.putExtra(PassConstans.POSITION , position);
        App.instance.sendBroadcast(intent);
    }
}
