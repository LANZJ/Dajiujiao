package com.jopool.crow.imlib.soket;

import android.content.Context;

import com.jopool.crow.imlib.entity.CWConversationMessage;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.jopool.crow.imlib.soket.listeners.OnBeforeConsumeConversationMesssgeListener;
import com.jopool.crow.imlib.soket.listeners.OnMessageReceiveListener;
import com.jopool.crow.imlib.soket.listeners.OnMessageSendListener;

/**
 * 操作soket接口规范
 *
 * @author xuan
 */
public abstract class CWImClient {
    protected Context context;

    /**
     * 接受消息回调监听
     */
    private OnMessageReceiveListener messageReceiveListener;
    /**
     * 处理聊天消息前调用的监听
     */
    private OnBeforeConsumeConversationMesssgeListener beforeConsumeConversationMesssgeListener;

    public CWImClient(Context context) {
        this.context = context;
    }

    public OnMessageReceiveListener getMessageReceiveListener() {
        return messageReceiveListener;
    }

    public void setMessageReceiveListener(
            OnMessageReceiveListener messageReceiveListener) {
        this.messageReceiveListener = messageReceiveListener;
    }

    public OnBeforeConsumeConversationMesssgeListener getBeforeConsumeConversationMesssgeListener() {
        return beforeConsumeConversationMesssgeListener;
    }

    public void setBeforeConsumeConversationMesssgeListener(OnBeforeConsumeConversationMesssgeListener beforeConsumeConversationMesssgeListener) {
        this.beforeConsumeConversationMesssgeListener = beforeConsumeConversationMesssgeListener;
    }

    /**
     * 发送消息，异步回调的方式
     *
     * @param context
     * @param message
     * @param l
     */
    public abstract void sendMessage(Context context,
                                     CWConversationMessage message, OnMessageSendListener l, CWConversationType toType);

    /**
     * 连接登陆
     *
     * @param config
     */
    public abstract void connect(CWImConfig config);

    /**
     * 断开连接
     */
    public abstract void disConnect();

    /**
     * 是否连接状态
     *
     * @return
     */
    public abstract boolean isConnected();

    /**
     * 是否是登陆状态
     *
     * @return
     */
    public abstract boolean isLogined();

}
