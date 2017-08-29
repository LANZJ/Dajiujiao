package com.jopool.crow.imkit.delegate;

import com.jopool.crow.imkit.listeners.CustomMessageProvider;
import com.jopool.crow.imkit.listeners.GetConversationTitleRightViewProvider;
import com.jopool.crow.imkit.listeners.GetGroupInfoProvider;
import com.jopool.crow.imkit.listeners.GetUserInfoProvider;
import com.jopool.crow.imkit.listeners.TopNoticeProvider;
import com.jopool.crow.imkit.listeners.impl.DefaulTopNoticeProvider;
import com.jopool.crow.imkit.listeners.impl.DefaultCustomMessageProvider;
import com.jopool.crow.imkit.listeners.impl.DefaultGetGroupInfoProvider;
import com.jopool.crow.imkit.listeners.impl.DefaultGetUserInfoProvider;

/**
 * GetXXXProviderd代理
 *
 * Created by xuan on 15/10/28.
 */
public class GetProviderDelegate {
    /**
     * 会话标题右边View添加
     */
    private GetConversationTitleRightViewProvider getConversationTitleRightViewProvider;

    public GetConversationTitleRightViewProvider getGetConversationTitleRightViewProvider() {
        return getConversationTitleRightViewProvider;
    }

    public void setGetConversationTitleRightViewProvider(GetConversationTitleRightViewProvider getConversationTitleRightViewProvider) {
        this.getConversationTitleRightViewProvider = getConversationTitleRightViewProvider;
    }

    /**
     * 消息顶部通知栏
     */
    private TopNoticeProvider topNoticeProvider;

    public TopNoticeProvider getTopNoticeProvider() {
        if (null == topNoticeProvider) {
            topNoticeProvider = new DefaulTopNoticeProvider();
        }
        return topNoticeProvider;
    }

    public void setTopNoticeProvider(TopNoticeProvider topNoticeProvider) {
        this.topNoticeProvider = topNoticeProvider;
    }

    /**
     * 群组信息提供者
     */
    private GetGroupInfoProvider getGroupInfoProvider;

    public GetGroupInfoProvider getGetGroupInfoProvider() {
        if (null == getGroupInfoProvider) {
            getGroupInfoProvider = new DefaultGetGroupInfoProvider();
        }
        return getGroupInfoProvider;
    }

    public void setGetGroupInfoProvider(GetGroupInfoProvider getGroupInfoProvider) {
        this.getGroupInfoProvider = getGroupInfoProvider;
    }

    /**
     * 用户信息提供者
     */
    private GetUserInfoProvider getUserInfoProvider;

    public GetUserInfoProvider getGetUserInfoProvider() {
        if (null == getUserInfoProvider) {
            getUserInfoProvider = new DefaultGetUserInfoProvider();
        }
        return getUserInfoProvider;
    }

    public void setGetUserInfoProvider(GetUserInfoProvider getUserInfoProvider) {
        this.getUserInfoProvider = getUserInfoProvider;
    }

    /**
     * 自定义消息提供者
     */
    private CustomMessageProvider customMessageProvider;

    public CustomMessageProvider getCustomMessageProvider() {
        if(null == customMessageProvider){
            customMessageProvider = new DefaultCustomMessageProvider();
        }
        return customMessageProvider;
    }

    public void setCustomMessageProvider(CustomMessageProvider customMessageProvider) {
        this.customMessageProvider = customMessageProvider;
    }

}
