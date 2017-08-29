package com.zjyeshi.dajiujiao.buyer.receiver.buy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;

/**
 * 分类选择广播
 *
 * Created by wuhk on 2015/10/28.
 */
public abstract class GoodsSortChangeReceiver extends BroadcastReceiver {
    public static final String ACTION = App.instance.getApplicationContext().getPackageName() + GoodsSortChangeReceiver.class.getSimpleName();
    public static String CATEGORYID = "categoryId";
    @Override
    public void onReceive(Context context, Intent intent) {
        String id = intent.getStringExtra(CATEGORYID);
        changeGoodsList(id);
    }
    /**改变商品列表
     *
     */
    public abstract void changeGoodsList(String id);
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
    public static void notifyReceiver(Context context , String categoryId){
        Intent intent = new Intent(ACTION);
        intent.putExtra(CATEGORYID , categoryId);
        context.sendBroadcast(intent);
    }
}
