package com.jopool.crow.imkit.listeners;

import android.content.Context;

import com.jopool.crow.imlib.entity.CWConversationMessage;

/**
 * Created by xuan on 16/9/19.
 */
public interface CustomMessageProvider {
    /**
     * 返回一个View,显示在聊天列表中,然后其他就按原message显示
     *
     * @param context
     * @param preMessage
     * @return
     */
    Object getCustomView(Context context, CWConversationMessage preMessage, CWConversationMessage message);
}
