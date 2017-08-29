package com.jopool.crow.imapi;

import android.content.Context;

import com.jopool.crow.CWChat;
import com.jopool.crow.CWChatConfig;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.jopool.crow.imlib.soket.listeners.OnConnectListener;
import com.jopool.crow.imlib.soket.listeners.OnMessageReceiveListener;

/**
 * 开始基本方法
 * <p/>
 * Created by xuan on 16/11/7.
 */
public class CWApiStart {
    /**
     * 在Application中初始化,Jcrow的配置参数
     *
     * @param application application
     * @param config      配置文件
     */
    public CWApiStart init(Context application, CWChatConfig config) {
        CWChat.init(application, config);
        return this;
    }

    /**
     * 在必要的时候连接通讯服务器
     *
     * @param context                activity
     * @param user                   登录用户
     * @param messageReceiveListener 消息接受回调
     * @param connectListener        链接回调
     */
    public CWApiStart connect(Context context,
                              CWUser user,
                              OnMessageReceiveListener messageReceiveListener,
                              OnConnectListener connectListener) {
        CWChat.getInstance().startWork(context, user, messageReceiveListener, connectListener);
        return this;
    }

    /**
     * 启动聊天界面
     *
     * @param context activity
     * @param toType  聊天类型
     * @param toId    发送人id
     * @param title   标题
     */
    public CWApiStart openCV(Context context,
                             CWConversationType toType,
                             String toId,
                             String title) {
        CWChat.getInstance().getConversationDelegate().startConversation(context, toType, toId, title);
        return this;
    }

    /**
     * 启动单聊
     *
     * @param context activity
     * @param toId    发送人id
     * @param title   标题
     */
    public CWApiStart openUserCV(Context context,
                                 String toId,
                                 String title) {
        openCV(context, CWConversationType.USER, toId, title);
        return this;
    }

    /**
     * 启动单聊
     *
     * @param context activity
     * @param toId    发送人id
     */
    public CWApiStart openUserCV(Context context,
                                 String toId) {
        openCV(context, CWConversationType.USER, toId, null);
        return this;
    }

    /**
     * 启动群聊
     *
     * @param context activity
     * @param toId    发送人id
     * @param title   标题
     */
    public CWApiStart openGroupCV(Context context,
                                  String toId, String title) {
        openCV(context, CWConversationType.GROUP, toId, title);
        return this;
    }

}
