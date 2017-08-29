package com.zjyeshi.dajiujiao.buyer.circle.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;

/**
 * Created by wuhk on 2015/11/23.
 */
public abstract class DeleteEvaluateReceiver extends BroadcastReceiver {
    public static final String ACTION = DeleteEvaluateReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String one  = intent.getStringExtra(PassConstans.EVALUATEPOS);
        String two = intent.getStringExtra(PassConstans.POSITION);
       doDelete(one , two);
    }

    /**执行相应操作
     *
     */
    public abstract void doDelete(String evaluatePos , String dataPos);
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
    public static void notifyReceiver(String evaluatePos , String dataPos){
        Intent intent = new Intent(ACTION);
        intent.putExtra(PassConstans.POSITION ,dataPos);
        intent.putExtra(PassConstans.EVALUATEPOS , evaluatePos);
        App.instance.sendBroadcast(intent);
    }
}
