package com.zjyeshi.dajiujiao.buyer.circle.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.Member;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;

/**
 * Created by wuhk on 2015/11/19.
 */
public abstract  class RefreshInCommentReceiver extends BroadcastReceiver {
    public static final String ACTION = RefreshInCommentReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        int position = Integer.parseInt(intent.getStringExtra(PassConstans.POSITION));
        Member member = (Member)intent.getSerializableExtra(PassConstans.MEMBER);
        refreshInComment(position , member);
    }

    /**执行相应操作
     *
     */
    public abstract void refreshInComment(int position , Member member);
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
    public static void notifyReceiver(String position , Member member){
        Intent intent = new Intent(ACTION);
        intent.putExtra(PassConstans.POSITION , position);
        intent.putExtra(PassConstans.MEMBER , member);
        App.instance.sendBroadcast(intent);
    }
}
