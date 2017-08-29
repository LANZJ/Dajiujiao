package com.jopool.crow.imapi;

import com.jopool.crow.CWChat;
import com.jopool.crow.imkit.listeners.OnMessageClickListener;
import com.jopool.crow.imkit.listeners.OnPortraitClickListener;
import com.jopool.crow.imkit.listeners.OnRecordVoiceListener;

/**
 * 监听事件设置
 * <p/>
 * Created by xuan on 16/11/7.
 */
public class CWApiListener {
    /**
     * 图像点击事件
     *
     * @param l 头像点击监听
     * @return
     */
    public CWApiListener setOnPortraitClickListener(OnPortraitClickListener l) {
        CWChat.getInstance().getConversationDelegate().setPortraitClickListener(l);
        return this;
    }

    /**
     * 消息点击事件
     *
     * @param l 消息点击监听
     * @return
     */
    public CWApiListener setOnMessageLongClickListener(OnMessageClickListener l) {
        CWChat.getInstance().getConversationDelegate().setOnMessageClickListener(l);
        return this;
    }

    /**
     * 消息长按事件
     *
     * @param l 消息长按监听
     * @return
     */
    public CWApiListener setOnMessageClickListener(OnMessageClickListener l) {
        CWChat.getInstance().getConversationDelegate().setOnMessageClickListener(l);
        return this;
    }

    /**
     * 设置录音事件
     *
     * @param l 录音监听
     * @return
     */
    public CWApiListener setOnRecordVoiceListener(OnRecordVoiceListener l) {
        CWChat.getInstance().getConversationDelegate().setOnRecordVoiceListener(l);
        return this;
    }

}
