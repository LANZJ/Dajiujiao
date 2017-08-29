package com.zjyeshi.dajiujiao.buyer.receiver.info;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginStatusEnum;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;

/**
 * Created by wuhk on 2015/11/23.
 */
public abstract class ChangePersonalReceiver extends BroadcastReceiver {
    public static final String ACTION =App.instance.getApplicationContext().getPackageName() + ChangePersonalReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String loginStatus = intent.getStringExtra(PassConstans.LOGINSTATUS);
        changeInfo(loginStatus);
    }

    /**执行相应操作
     *
     */
    public abstract void changeInfo(String loginstatus);
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
    public static void notifyReceiver(LoginStatusEnum loginStatusEnum){
        Intent intent = new Intent(ACTION);
        intent.putExtra(PassConstans.LOGINSTATUS ,loginStatusEnum.toString());
        App.instance.sendBroadcast(intent);
    }
}
