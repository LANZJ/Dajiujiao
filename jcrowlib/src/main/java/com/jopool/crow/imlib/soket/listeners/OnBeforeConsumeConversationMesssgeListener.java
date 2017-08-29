package com.jopool.crow.imlib.soket.listeners;

import com.jopool.crow.imlib.entity.CWConversationMessage;

/**
 * 在消费聊天消息前
 *
 * Created by xuan on 15/11/3.
 */
public interface OnBeforeConsumeConversationMesssgeListener {
    /**
     * 消费聊天消息前
     *
     * @param cwMessage
     * @return
     */
    boolean onBeforeConsumeConversationMesssge(CWConversationMessage cwMessage);
}
