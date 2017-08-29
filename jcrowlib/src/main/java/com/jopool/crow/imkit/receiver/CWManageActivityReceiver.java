package com.jopool.crow.imkit.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.jopool.crow.CWChat;
import com.jopool.crow.imkit.activity.CWConversationActivity;

/**
 * 管理Activity广播
 *
 * Created by xuan on 15/11/9.
 */
public abstract class CWManageActivityReceiver extends BroadcastReceiver {
    public static final String ACTION = CWManageActivityReceiver.class.getCanonicalName();
    public static final String PARAM_OPERATION_TYPE = "param.operation.type";
    public static final String PARAM_OPERATION_TYPE_FINISH = "param.operation.type.finish";

    public static final String PARAM_ACTIVITY_NAME = "param.activity.name";

    @Override
    public void onReceive(Context context, Intent intent) {
        String operationType = intent.getStringExtra(PARAM_OPERATION_TYPE);
        if(PARAM_OPERATION_TYPE_FINISH.equals(operationType)){
            String activityName = intent.getStringExtra(PARAM_ACTIVITY_NAME);
            onFinish(activityName);
        }
    }

    protected abstract void onFinish(String activityName);

    /**
     * 注册广播接受
     */
    public void register() {
        CWChat.getApplication().registerReceiver(this, new IntentFilter(ACTION));
    }

    /**
     * 取消广播接受
     */
    public void unregister() {
        CWChat.getApplication().unregisterReceiver(this);
    }

    /**
     * 发送通知
     */
    public static void finishActivity(String activityName) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(PARAM_OPERATION_TYPE, PARAM_OPERATION_TYPE_FINISH);
        intent.putExtra(PARAM_ACTIVITY_NAME, activityName);
        CWChat.getApplication().sendBroadcast(intent);
    }

    /**
     * 关掉聊天界面
     */
    public static void finishConversationActivity(){
        finishActivity(CWConversationActivity.class.getSimpleName());
    }
}
