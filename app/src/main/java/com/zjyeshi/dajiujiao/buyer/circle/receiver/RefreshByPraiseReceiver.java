package com.zjyeshi.dajiujiao.buyer.circle.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;
import com.zjyeshi.dajiujiao.buyer.circle.itementity.CircleContentEntity;

/**
 * Created by wuhk on 2015/11/19.
 */
public abstract  class RefreshByPraiseReceiver extends BroadcastReceiver {
    public static final String ACTION = RefreshByPraiseReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        int position = Integer.parseInt(intent.getStringExtra(PassConstans.POSITION));
        CircleContentEntity contentEntity = (CircleContentEntity)intent.getSerializableExtra(PassConstans.CONTENTENTITY);
        refreshByPraise(position, contentEntity);
    }

    /**执行相应操作
     *
     */
    public abstract void refreshByPraise(int position , CircleContentEntity contentEntity);
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
    public static void notifyReceiver(String position , CircleContentEntity contentEntity){
        Intent intent = new Intent(ACTION);
        intent.putExtra(PassConstans.POSITION , position);
        intent.putExtra(PassConstans.CONTENTENTITY , contentEntity);
        App.instance.sendBroadcast(intent);
    }
}
