package com.jopool.crow.imkit.listeners.impl;

import android.content.Context;

import com.jopool.crow.imkit.listeners.CustomMessageProvider;
import com.jopool.crow.imlib.entity.CWConversationMessage;
import com.jopool.crow.imlib.enums.CWMessageContentType;

/**
 * Created by xuan on 16/9/19.
 */
public class DefaultCustomMessageProvider implements CustomMessageProvider{
    @Override
    public Object getCustomView(Context context, CWConversationMessage preMessage, CWConversationMessage message) {
        message.setMessageContentType(CWMessageContentType.TEXT);
        message.setContent("自定义消息,暂不支持显示");
        return message;
    }

}
