package com.jopool.crow.imkit.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by xuan on 15/12/29.
 */
public abstract class CWRefreshConversationListReceiver extends BroadcastReceiver {
    public static final String ACTION = CWRefreshConversationListReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        refreshConversationList();
    }

    /**
     * 子类自己实现，用来刷新会话列表
     */
    public abstract void refreshConversationList();

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
