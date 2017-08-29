package com.jopool.crow.imapi;

import android.content.Context;

import com.jopool.crow.CWChat;
import com.jopool.crow.imkit.listeners.CustomMessageProvider;
import com.jopool.crow.imkit.listeners.OnMessageClickListener;
import com.jopool.crow.imkit.listeners.OnRecordVoiceListener;
import com.jopool.crow.imlib.enums.CWConversationType;

/**
 * 消息发送接口
 * <p/>
 * Created by xuan on 16/11/7.
 */
public class CWApiMessage {

    /**
     * 发送文本消息
     *
     * @param context          activity
     * @param toId             发送人id
     * @param text             发送内容
     * @param conversationType 发送类型
     */
    public CWApiMessage sendTextMessage(Context context, String toId, String text, CWConversationType conversationType) {
        CWChat.getInstance().getSendMsgDelegate().sendTextMessage(context, toId, text, conversationType);
        return this;
    }

    /**
     * 发送图片消息
     *
     * @param context          activity
     * @param toId             发送人id
     * @param fromFileName     本地图片地址
     * @param conversationType 发送类型
     */
    public CWApiMessage sendImageMessage(Context context, String toId, String fromFileName, CWConversationType conversationType) {
        CWChat.getInstance().getSendMsgDelegate().sendImageMessage(context, toId, fromFileName, conversationType);
        return this;
    }

    /**
     * 发送语音消息
     *
     * @param context          activity
     * @param toId             发送人id
     * @param voiceFileName    语音文件地址
     * @param voiceLength      录音长度
     * @param conversationType 发送类型
     */
    public CWApiMessage sendVoiceMessage(Context context, String toId, String voiceFileName, long voiceLength, CWConversationType conversationType) {
        CWChat.getInstance().getSendMsgDelegate().sendVoiceMessage(context, toId, voiceFileName, voiceLength, conversationType);
        return this;
    }

    /**
     * 发送网址消息
     *
     * @param context          activity
     * @param toId             发送人id
     * @param title            网址标题
     * @param url              网址地址
     * @param conversationType 发送类型
     */
    public CWApiMessage sendUrlMessage(Context context, String toId, String title, String url, CWConversationType conversationType) {
        CWChat.getInstance().getSendMsgDelegate().sendUrlMessage(context, toId, title, url, conversationType);
        return this;
    }

    /**
     * 设置自定义消息处理
     *
     * @param provider
     */
    public CWApiMessage setCustomMessageProvider(CustomMessageProvider provider) {
        CWChat.getInstance().getProviderDelegate().setCustomMessageProvider(provider);
        return this;
    }

    /**
     * 消息点击事件
     *
     * @param l 消息点击监听
     * @return
     */
    public CWApiMessage setOnMessageLongClickListener(OnMessageClickListener l) {
        CWChat.getInstance().getConversationDelegate().setOnMessageClickListener(l);
        return this;
    }

    /**
     * 消息长按事件
     *
     * @param l 消息长按监听
     * @return
     */
    public CWApiMessage setOnMessageClickListener(OnMessageClickListener l) {
        CWChat.getInstance().getConversationDelegate().setOnMessageClickListener(l);
        return this;
    }

    /**
     * 设置录音事件
     *
     * @param l 录音监听
     * @return
     */
    public CWApiMessage setOnRecordVoiceListener(OnRecordVoiceListener l) {
        CWChat.getInstance().getConversationDelegate().setOnRecordVoiceListener(l);
        return this;
    }

}
