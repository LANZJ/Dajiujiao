package com.jopool.crow.imkit.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 人员选择点击事件广播
 * Created by wuhk on 2016/11/7.
 */
public abstract class CWGroupSelectUserClickReceiver extends BroadcastReceiver {
    public static final String ACTION = CWGroupSelectUserClickReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        refreshSelectedUser();
    }

    /**
     * 子类自己实现，用来刷新会话列表
     */
    public abstract void refreshSelectedUser();

    /**
     * 注册广播接受
     *
     * @param context
     */
    public void register(Context context) {
        context.registerReceiver(this, new IntentFilter(ACTION));
    }

    /**
     * 取消广播接受
     *
     * @param context
     */
    public void unregister(Context context) {
        context.unregisterReceiver(this);
    }

    /**
     * 发送通知
     *
     * @param context
     */
    public static void notifyReceiver(Context context) {
        context.sendBroadcast(new Intent(ACTION));
    }
}
