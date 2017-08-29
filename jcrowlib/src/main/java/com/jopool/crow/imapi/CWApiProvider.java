package com.jopool.crow.imapi;

import com.jopool.crow.CWChat;
import com.jopool.crow.imkit.listeners.CustomMessageProvider;
import com.jopool.crow.imkit.listeners.GetConversationTitleRightViewProvider;
import com.jopool.crow.imkit.listeners.GetUserInfoProvider;
import com.jopool.crow.imkit.listeners.TopNoticeProvider;

/**
 * 内容提供者
 *
 * Created by xuan on 16/11/7.
 */
public class CWApiProvider {

    /**
     * 设置聊天界面右边布局
     *
     * @param provider
     */
    public CWApiProvider setCVTitleRightViewProvider(GetConversationTitleRightViewProvider provider){
        CWChat.getInstance().getProviderDelegate().setGetConversationTitleRightViewProvider(provider);
        return this;
    }

    /**
     * 设置聊天界面顶部通知提示布局
     *
     * @param provider
     */
    public CWApiProvider setCVTopNoticeProvider(TopNoticeProvider provider){
        CWChat.getInstance().getProviderDelegate().setTopNoticeProvider(provider);
        return this;
    }

    /**
     * 设置获取用户信息
     *
     * @param provider
     */
    public CWApiProvider setUserInfoProvider(GetUserInfoProvider provider){
        CWChat.getInstance().getProviderDelegate().setGetUserInfoProvider(provider);
        return this;
    }

    /**
     * 设置自定义消息处理
     *
     * @param provider
     */
    public CWApiProvider setCustomMessageProvider(CustomMessageProvider provider){
        CWChat.getInstance().getProviderDelegate().setCustomMessageProvider(provider);
        return this;
    }

}
