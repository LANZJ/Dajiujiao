package com.zjyeshi.dajiujiao.buyer.chat;

import android.content.Context;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.jopool.crow.imlib.task.BindChannelIdTask;
import com.jopool.crow.imlib.utils.CWLogUtil;
import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.chat.consumer.Invoker;
import com.zjyeshi.dajiujiao.buyer.chat.consumer.MyGTMessage;
import com.zjyeshi.dajiujiao.buyer.chat.consumer.UnReadNumPush;
import com.zjyeshi.dajiujiao.buyer.utils.AppUnreadUtil;
import com.zjyeshi.dajiujiao.buyer.utils.LogUtil;

/**
 * Created by wuhk on 2017/2/15.
 */

public class MyGTIntentService extends GTIntentService {

    public MyGTIntentService() {
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

        byte[] payload = msg.getPayload();
        String data = new String(payload);
        MyGTMessage message = JSON.parseObject(data , MyGTMessage.class);
        //通知三星角标变化
        UnReadNumPush push = JSON.parseObject(message.getCustom_content(), UnReadNumPush.class);
        AppUnreadUtil.sendBageNumberUseMySet(context, push.getContent().getCONTENTKEY_TOTAL_UNREAD_COUNT());
        LogUtil.e("标题[" + message.getTitle() +"]" + "内容["+ message.getDescription() +"]" + "customerString["+ message.getCustom_content()+"]");
        //处理相应事件
        Invoker.consume(context , message.getTitle() , message.getDescription() , message.getCustom_content());
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {

    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {

    }
}
