package com.jopool.crow.imapi;

import com.jopool.crow.CWChat;
import com.jopool.crow.imkit.delegate.CWUserDelegate;
import com.jopool.crow.imkit.listeners.GetUserInfoProvider;
import com.jopool.crow.imkit.listeners.LoadUserInfoProvider;
import com.jopool.crow.imkit.listeners.OnPortraitClickListener;

/**
 * 内容提供者
 * <p/>
 * Created by xuan on 16/11/7.
 */
public class CWApiUser {
    private CWUserDelegate userDelegate = new CWUserDelegate();

    /**
     * 设置获取用户信息
     *
     * @param provider
     */
    public CWApiUser setUserInfoProvider(GetUserInfoProvider provider) {
        CWChat.getInstance().getProviderDelegate().setGetUserInfoProvider(provider);
        return this;
    }

    /**
     * 头像点击监听
     *
     * @param l 头像点击监听
     * @return
     */
    public CWApiUser setOnPortraitClickListener(OnPortraitClickListener l) {
        CWChat.getInstance().getConversationDelegate().setPortraitClickListener(l);
        return this;
    }

    /**
     * 设置加在用户信息代理
     *
     * @param loadUserInfoProvider
     * @return
     */
    public CWApiUser setLoadUserInfoProvider(LoadUserInfoProvider loadUserInfoProvider) {
        userDelegate.setLoadUserInfoProvider(loadUserInfoProvider);
        return this;
    }

    /**
     * 获取加在用户信息代理
     *
     * @return
     */
    public LoadUserInfoProvider getLoadUserInfoProvider(){
        return userDelegate.getLoadUserInfoProvider();
    }

}
