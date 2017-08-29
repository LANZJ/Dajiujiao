package com.jopool.crow.imkit.delegate;

import android.content.Context;
import android.content.Intent;

import com.jopool.crow.CWChat;
import com.jopool.crow.imkit.activity.CWConversationActivity;
import com.jopool.crow.imkit.listeners.CWConversationPriorityProvider;
import com.jopool.crow.imkit.listeners.GetConversationTitleRightViewProvider;
import com.jopool.crow.imkit.listeners.OnMessageClickListener;
import com.jopool.crow.imkit.listeners.OnMessageLongClickListener;
import com.jopool.crow.imkit.listeners.OnPortraitClickListener;
import com.jopool.crow.imkit.listeners.OnRecordVoiceListener;
import com.jopool.crow.imkit.listeners.impl.DefaultConversationPriorityProvider;
import com.jopool.crow.imkit.listeners.impl.DefaultOnMessageClickListener;
import com.jopool.crow.imkit.listeners.impl.DefaultOnMessageLongClickListener;
import com.jopool.crow.imkit.listeners.impl.DefaultOnPortraitClickListener;
import com.jopool.crow.imkit.listeners.impl.DefaultOnRecordVoiceListener;
import com.jopool.crow.imlib.enums.CWConversationType;

/**
 * Conversation相关的代理
 *
 * Created by xuan on 15/10/28.
 */
public class ConversationDelegate {
    /**
     * 启动聊天界面
     *
     * @param context
     * @param conversationType
     * @param toId
     * @param title
     * @param rightViewProvider
     */
    public void startConversation(Context context,
                                  CWConversationType conversationType, String toId, String title, GetConversationTitleRightViewProvider rightViewProvider) {

        CWChat.getInstance().getProviderDelegate().setGetConversationTitleRightViewProvider(rightViewProvider);

        Intent intent = new Intent();
        intent.setClass(context, CWConversationActivity.class);
        intent.putExtra(CWConversationActivity.PARAM_TOID, toId);
        intent.putExtra(CWConversationActivity.PARAM_CONVERSATION_TYPE,
                conversationType.getValue());
        intent.putExtra(CWConversationActivity.PARAM_TITLE, title);
        context.startActivity(intent);
    }

    /**
     * 启动聊天界面
     *
     * @param context
     * @param conversationType
     * @param toId
     * @param title
     */
    public void startConversation(Context context,
                                  CWConversationType conversationType, String toId, String title) {
        startConversation(context, conversationType, toId, title, null);
    }

    /**
     * 启动单聊
     *
     * @param context
     * @param toId
     * @param title
     * @param rightViewProvider
     */
    public void startPrivateConversation(Context context,
                                         String toId, String title, GetConversationTitleRightViewProvider rightViewProvider){
        CWChat.getInstance().getProviderDelegate().setGetConversationTitleRightViewProvider(rightViewProvider);
        //
        Intent intent = new Intent();
        intent.setClass(context, CWConversationActivity.class);
        intent.putExtra(CWConversationActivity.PARAM_TOID, toId);
        intent.putExtra(CWConversationActivity.PARAM_CONVERSATION_TYPE,
                CWConversationType.USER.getValue());
        intent.putExtra(CWConversationActivity.PARAM_TITLE, title);
        context.startActivity(intent);
    }

    /**
     * 启动群聊
     *
     * @param context
     * @param toId
     * @param title
     * @param rightViewProvider
     */
    public void startGroupConversation(Context context,
                                       String toId, String title, GetConversationTitleRightViewProvider rightViewProvider){
        CWChat.getInstance().getProviderDelegate().setGetConversationTitleRightViewProvider(rightViewProvider);

        Intent intent = new Intent();
        intent.setClass(context, CWConversationActivity.class);
        intent.putExtra(CWConversationActivity.PARAM_TOID, toId);
        intent.putExtra(CWConversationActivity.PARAM_CONVERSATION_TYPE,
                CWConversationType.GROUP.getValue());
        intent.putExtra(CWConversationActivity.PARAM_TITLE, title);
        context.startActivity(intent);
    }

    /**
     * 头像点击事件
     */
    private OnPortraitClickListener portraitClickListener;

    public OnPortraitClickListener getPortraitClickListener() {
        if (null == portraitClickListener) {
            portraitClickListener = new DefaultOnPortraitClickListener();
        }

        return portraitClickListener;
    }

    public void setPortraitClickListener(OnPortraitClickListener portraitClickListener) {
        this.portraitClickListener = portraitClickListener;
    }

    /**
     * 消息长按事件
     */
    private OnMessageLongClickListener onMessageLongClickListener;

    public OnMessageLongClickListener getOnMessageLongClickListener() {
        if (null == onMessageLongClickListener) {
            onMessageLongClickListener = new DefaultOnMessageLongClickListener();
        }
        return onMessageLongClickListener;
    }

    public void setOnMessageLongClickListener(OnMessageLongClickListener onMessageLongClickListener) {
        this.onMessageLongClickListener = onMessageLongClickListener;
    }

    /**
     * 消息单击事件
     */
    private OnMessageClickListener onMessageClickListener;

    public OnMessageClickListener getOnMessageClickListener() {
        if (null == onMessageClickListener) {
            onMessageClickListener = new DefaultOnMessageClickListener();
        }
        return onMessageClickListener;
    }

    public void setOnMessageClickListener(OnMessageClickListener onMessageClickListener) {
        this.onMessageClickListener = onMessageClickListener;
    }

    /**
     * 录音事件
     */
    private OnRecordVoiceListener onRecordVoiceListener;

    public OnRecordVoiceListener getOnRecordVoiceListener() {
        if (null == onRecordVoiceListener) {
            onRecordVoiceListener = new DefaultOnRecordVoiceListener();
        }
        return onRecordVoiceListener;
    }

    public void setOnRecordVoiceListener(OnRecordVoiceListener onRecordVoiceListener) {
        this.onRecordVoiceListener = onRecordVoiceListener;
    }

    private CWConversationPriorityProvider conversationPriorityProvider;

    public CWConversationPriorityProvider getConversationPriorityProvider() {
        if(null == conversationPriorityProvider){
            conversationPriorityProvider = new DefaultConversationPriorityProvider();
        }
        return conversationPriorityProvider;
    }

    public void setConversationPriorityProvider(CWConversationPriorityProvider conversationPriorityProvider) {
        this.conversationPriorityProvider = conversationPriorityProvider;
    }
}
