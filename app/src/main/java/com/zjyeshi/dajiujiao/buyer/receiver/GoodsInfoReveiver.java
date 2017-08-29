package com.zjyeshi.dajiujiao.buyer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.entity.good.AllGoodInfo;

/**
 * Created by wuhk on 2016/12/14.
 */
public abstract class GoodsInfoReveiver extends BroadcastReceiver {
    private static final String ACTION = App.instance.getApplicationContext().getPackageName() + GoodsInfoReveiver.class.getSimpleName();
    public static final String PARAM_GOOD_INFO = "param_good_info";

    @Override
    public void onReceive(Context context, Intent intent) {
        String jsonStr = intent.getStringExtra(PARAM_GOOD_INFO);
        AllGoodInfo allGoodInfo = JSON.parseObject(jsonStr , AllGoodInfo.class);
        changeData(allGoodInfo);
    }

    public abstract void changeData(AllGoodInfo allGoodInfo);

    public void register() {
        IntentFilter filter = new IntentFilter(ACTION);
        App.instance.registerReceiver(this, filter);
    }

    public void unregister() {
        App.instance.unregisterReceiver(this);
    }

    public static void notifyReceiver(String allGoodInfoStr) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(PARAM_GOOD_INFO , allGoodInfoStr);
        App.instance.sendBroadcast(intent);
    }
}
