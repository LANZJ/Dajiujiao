package com.zjyeshi.dajiujiao.buyer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.task.work.data.NewRemindData;
import com.zjyeshi.dajiujiao.buyer.task.work.NewRemindTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

/**
 * Created by wuhk on 2016/7/11.
 */
public class LoadNewRemindReceiver extends BroadcastReceiver {
    private static final String ACTION = App.instance.getApplicationContext().getPackageName() + LoadNewRemindReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        loadRemind(context);
    }

    public void register() {
        IntentFilter filter = new IntentFilter(ACTION);
        App.instance.registerReceiver(this, filter);
    }

    public void unregister() {
        App.instance.unregisterReceiver(this);
    }

    public static void notifyReceiver() {
        Intent intent = new Intent(ACTION);
        App.instance.sendBroadcast(intent);
    }

    private void loadRemind(Context context){
        NewRemindTask newRemindTask = new NewRemindTask(context);
        newRemindTask.setShowProgressDialog(false);
        newRemindTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NewRemindData>() {
            @Override
            public void failCallback(Result<NewRemindData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        newRemindTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NewRemindData>() {
            @Override
            public void successCallback(Result<NewRemindData> result) {
                ChangeRemindShowReceiver.notifyReceiver();
            }
        });

        newRemindTask.execute();
    }
}
