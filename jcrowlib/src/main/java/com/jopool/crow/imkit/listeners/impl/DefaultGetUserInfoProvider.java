package com.jopool.crow.imkit.listeners.impl;

import com.jopool.crow.imkit.listeners.GetUserInfoProvider;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.model.CWUserModel;

/**
 * 用户信息提供者默认实现
 *
 * @author xuan
 */
public class DefaultGetUserInfoProvider implements GetUserInfoProvider {
    @Override
    public CWUser getUserById(String userId) {
        return CWUserModel.getInstance().getByUserId(userId);
    }

}
