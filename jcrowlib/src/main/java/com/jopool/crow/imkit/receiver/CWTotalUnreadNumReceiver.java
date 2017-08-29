package com.jopool.crow.imkit.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.jopool.crow.CWChat;

/**
 * 刷新未读消息接受器
 *
 * @author xuan
 */
public abstract class CWTotalUnreadNumReceiver extends BroadcastReceiver {
    public static final String ACTION = CWTotalUnreadNumReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        int unreadNum = CWChat.getInstance().getTotalUnreadNum();
        totalUnreadNum(unreadNum);
    }

    /**
     * 子类自己实现，用来刷新未读消息
     *
     * @param totalUnreadNum
     */
    public abstract void totalUnreadNum(int totalUnreadNum);

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
