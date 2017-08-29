package com.jopool.crow.imkit.listeners.impl;

import com.jopool.crow.imkit.listeners.GetGroupUserSelectListProvider;
import com.jopool.crow.imlib.entity.CWSelectUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhk on 2016/11/4.
 */
public class DefaultGetGroupUserSelectProvider implements GetGroupUserSelectListProvider {
    @Override
    public void getSelectUserList(GetListCallback getListCallback) {
        getListCallback.callBack(new ArrayList<CWSelectUser>());
    }
}
