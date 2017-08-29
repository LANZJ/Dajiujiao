package com.jopool.crow.imkit.delegate;

import com.jopool.crow.imkit.listeners.LoadUserInfoProvider;
import com.jopool.crow.imkit.listeners.impl.DefaultLoadUserInfoProvider;

/**
 * User相关代理
 * Created by wuhk on 2016/11/4.
 */
public class CWUserDelegate {

    /**
     * 群聊用户选择列表提供
     */
    private LoadUserInfoProvider loadUserInfoProvider;

    public LoadUserInfoProvider getLoadUserInfoProvider() {
        if (null == loadUserInfoProvider) {
            loadUserInfoProvider = new DefaultLoadUserInfoProvider();
        }
        return loadUserInfoProvider;
    }

    public void setLoadUserInfoProvider(LoadUserInfoProvider loadUserInfoProvider) {
        this.loadUserInfoProvider = loadUserInfoProvider;
    }
}
