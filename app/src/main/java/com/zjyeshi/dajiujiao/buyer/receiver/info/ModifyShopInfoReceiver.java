package com.zjyeshi.dajiujiao.buyer.receiver.info;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;

/**
 * 更改商店信息广播
 * Created by wuhk on 2015/11/13.
 */
public abstract class ModifyShopInfoReceiver extends BroadcastReceiver {
    public static final String ACTION = App.instance.getApplicationContext().getPackageName() + ModifyShopInfoReceiver.class.getSimpleName();

    private static final String PARAM_CONTENT = "param.content";
    private static final String PARAM_MODIFY_TYPE = "param.modify.type";

    @Override
    public void onReceive(Context context, Intent intent) {
        String str = intent.getStringExtra(PARAM_CONTENT);
        int type = intent.getIntExtra(PARAM_MODIFY_TYPE , 99);

        doModify(str , type);
    }

    /**执行相应操作
     *
     */
    public abstract void doModify(String str , int type);
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
    public static void notifyReceiver(String str , int type){
        Intent intent = new Intent(ACTION);
        intent.putExtra(PARAM_CONTENT , str);
        intent.putExtra(PARAM_MODIFY_TYPE , type);
        App.instance.sendBroadcast(intent);
    }
}
