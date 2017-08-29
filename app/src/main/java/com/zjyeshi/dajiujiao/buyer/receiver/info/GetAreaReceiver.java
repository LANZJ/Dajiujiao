package com.zjyeshi.dajiujiao.buyer.receiver.info;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;

/**
 * Created by wuhk on 2015/11/22.
 */
public abstract class GetAreaReceiver extends BroadcastReceiver {
    public static final String ACTION = App.instance.getApplicationContext().getPackageName() + GetAreaReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String speName = intent.getStringExtra(PassConstans.SPENAME);
        String speCode = intent.getStringExtra(PassConstans.SPECODE);
        showArea(speName ,speCode);
    }

    /**执行相应操作
     *
     */
    public abstract void showArea(String speName , String speCode);
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
    public static void notifyReceiver(String speName , String speCode){
        Intent intent = new Intent(ACTION);
        intent.putExtra(PassConstans.SPENAME , speName);
        intent.putExtra(PassConstans.SPECODE , speCode);
        App.instance.sendBroadcast(intent);
    }
}
