package com.zjyeshi.dajiujiao.buyer.receiver.buy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;

/**
 * Created by wuhk on 2016/12/27.
 */
public abstract class BuyCarSelectChangeReceiver extends BroadcastReceiver {
    private static final String ACTION = App.instance.getApplicationContext().getPackageName() + BuyCarSelectChangeReceiver.class.getSimpleName();

    private static final String PARAM_TYPE = "param.type";

    public static final String SELECT_REFRESH = "select_refresh";//增减，选择变化调用
    public static final String DELETE_REFRESH = "delete_refresh";//购物车商品删除时调用
    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(PARAM_TYPE);
        changeSelect(type);
    }

    /**改变数量
     *
     */
    public abstract void changeSelect(String type);

    /**注册广播
     *
     */
    public void register(){
        App.instance.registerReceiver(this , new IntentFilter(ACTION));
    }

    /**取消广播
     *
     */
    public void unRegister(){
        App.instance.unregisterReceiver(this);
    }

    /**发送广播
     *
     */
    public static void notifyReceiver(String str){
        Intent intent = new Intent(ACTION);
        intent.putExtra(PARAM_TYPE , str);
        App.instance.sendBroadcast(intent);
    }
}
