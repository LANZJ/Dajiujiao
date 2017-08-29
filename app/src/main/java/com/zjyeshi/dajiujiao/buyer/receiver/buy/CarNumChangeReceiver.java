package com.zjyeshi.dajiujiao.buyer.receiver.buy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;

/**
 * 购物车数量和价格变化广播
 *
 * Created by wuhk on 2015/10/15.
 */
public abstract class CarNumChangeReceiver extends BroadcastReceiver {
    public static final String ACTION = App.instance.getApplicationContext().getPackageName() + CarNumChangeReceiver.class.getSimpleName();

    private static final String PARAM_IS_ADD_ORDER = "param.is.add.order";

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isAddOrder = intent.getBooleanExtra(PARAM_IS_ADD_ORDER , false);
        changeCarnum(isAddOrder);
    }

    /**改变数量
     *
     */
    public abstract void changeCarnum(boolean isAddOrder);

    /**注册广播
     *
     * @param context
     */
    public void register(Context context){
        context.registerReceiver(this , new IntentFilter(ACTION));
    }

    /**取消广播
     *
     * @param context
     */
    public void unRegister(Context context){
        context.unregisterReceiver(this);
    }

    /**发送广播
     *
     * @param context
     */
    public static void notifyReceiver(Context context){
        Intent intent = new Intent(ACTION);
        intent.putExtra(PARAM_IS_ADD_ORDER , false);
        context.sendBroadcast(intent);
    }

    public static void notifyReceiverWhenAddOrder(Context context){
        Intent intent = new Intent(ACTION);
        intent.putExtra(PARAM_IS_ADD_ORDER ,  true);
        context.sendBroadcast(intent);
    }
}
