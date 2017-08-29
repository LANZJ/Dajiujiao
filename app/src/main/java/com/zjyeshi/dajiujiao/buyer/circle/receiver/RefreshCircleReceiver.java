package com.zjyeshi.dajiujiao.buyer.circle.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.Evaluate;

/**
 * Created by wuhk on 2015/11/19.
 */
public abstract class RefreshCircleReceiver extends BroadcastReceiver {
    public static final String ACTION = RefreshCircleReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        int position = Integer.parseInt(intent.getStringExtra(PassConstans.POSITION));
        Evaluate evaluate = (Evaluate)intent.getSerializableExtra(PassConstans.EVALUATE);
        refreshCircle(position , evaluate);
    }

    /**执行相应操作
     *
     */
    public abstract void refreshCircle(int position , Evaluate evaluate);
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
    public static void notifyReceiver(String position , Evaluate evaluate){
        Intent intent = new Intent(ACTION);
        intent.putExtra(PassConstans.POSITION , position);
        intent.putExtra(PassConstans.EVALUATE , evaluate);
        App.instance.sendBroadcast(intent);
    }
}
