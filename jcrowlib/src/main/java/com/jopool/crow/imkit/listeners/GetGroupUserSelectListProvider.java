package com.jopool.crow.imkit.listeners;

import com.jopool.crow.imlib.entity.CWSelectUser;
import com.jopool.crow.imlib.entity.CWUser;

import java.util.List;

/**
 * 选择群成员的列表
 * Created by wuhk on 2016/11/4.
 */
public interface GetGroupUserSelectListProvider {
    /**
     * 获取群聊添加成员列表
     *
     * @return
     */
    void getSelectUserList(GetListCallback getListCallback);

    interface GetListCallback{
        void callBack(List<CWSelectUser> list);
    }
}
