package com.zjyeshi.dajiujiao.buyer.chat.consumer;

import android.content.Context;

/**
 * 推送消息类型基类
 * Created by wuhk on 2015/12/4.
 */
public abstract class BasePush {
    //消息类型
    public static final String PUSH_NEW_CHAT_MESSAGE= "new-chat-message"; //聊天消息
    public static final String PUSH_NEW_ORDER= "NEW_ORDER"; //新订单
    public static final String PUSH_NEWREMIND_PUSH= "NEWREMIND_PUSH";//考勤等新消息提醒
    public static final String PUSH_ORDER_REBACK= "ORDER_REBACK";//
    public static final String PUSH_CONTACTS_CHANGE_PUSH= "CONTACTS_CHANGE_PUSH";//通讯录有变动

    public static final String PARAM_NOTICE_TYPE = "param.notice.type";

    private String type;
    private String serverTime;
    private String version;
    private String message;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 消费
     *
     * @param context
     */
    protected abstract void consume(Context context , String title , String description);
}
