package com.jopool.crow.imlib.gettui;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.jopool.crow.imlib.task.BindChannelIdTask;
import com.jopool.crow.imlib.utils.CWLogUtil;

/**
 * Created by wuhk on 2017/2/16.
 */

public class CWGeTuiDefaultIntentService extends GTIntentService {
    public CWGeTuiDefaultIntentService() {
    }

    @Override
    public void onReceiveServicePid(Context context, int i) {

    }

    @Override
    public void onReceiveClientId(Context context, String s) {
        CWLogUtil.e("onReceiveClientId -> " + "ClientId = " + s);
        //绑定个推推送
        BindChannelIdTask bindTask = new BindChannelIdTask(context);
        bindTask.execute(s);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {

        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String cid = msg.getClientId();

        String data = new String(payload);
        CWLogUtil.e("--------"+ data);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {

    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {

    }
}
