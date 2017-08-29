package com.zjyeshi.dajiujiao.buyer.circle.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;

/**
 * 设置listView显示置顶位置
 * Created by wuhk on 2015/11/19.
 */
public abstract  class SetListViewReceiver extends BroadcastReceiver {
    public static final String ACTION = SetListViewReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        int position = Integer.parseInt(intent.getStringExtra(PassConstans.POSITION));
        setListView(position);
    }

    /**执行相应操作
     *
     */
    public abstract void setListView(int position);
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
    public static void notifyReceiver(String position){
        Intent intent = new Intent(ACTION);
        intent.putExtra(PassConstans.POSITION , position);
        App.instance.sendBroadcast(intent);
    }
}
