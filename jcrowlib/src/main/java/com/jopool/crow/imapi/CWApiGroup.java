package com.jopool.crow.imapi;

import android.content.Context;

import com.jopool.crow.CWChat;
import com.jopool.crow.imkit.listeners.GetGroupInfoProvider;
import com.jopool.crow.imkit.listeners.GetGroupUserSelectListProvider;
import com.jopool.crow.imkit.listeners.OnGroupUserClickListener;
import com.jopool.crow.imlib.entity.CWSelectUser;
import com.jopool.crow.imlib.enums.CWGroupSelectUserType;

import java.util.ArrayList;

/**
 * 群组相关的对外接口
 * <p/>
 * Created by xuan on 16/11/7.
 */
public class CWApiGroup {
    /**
     * 设置获取群聊信息
     *
     * @param provider
     */
    public CWApiGroup setGroupInfoProvider(GetGroupInfoProvider provider) {
        CWChat.getInstance().getProviderDelegate().setGetGroupInfoProvider(provider);
        return this;
    }

    /**
     * 设置获取人员选择列表
     *
     * @param getGroupUserSelectListProvider
     * @return
     */
    public CWApiGroup setGetGroupUserSelectListProvider(GetGroupUserSelectListProvider getGroupUserSelectListProvider) {
        CWChat.getInstance().getGroupDelegate().setGetGroupUserSelectListProvider(getGroupUserSelectListProvider);
        return this;
    }

    /**
     * 设置群聊详情头像点击事件
     *
     * @param onGroupUserClickListener
     * @return
     */
    public CWApiGroup setOnGroupUserClickListener(OnGroupUserClickListener onGroupUserClickListener) {
        CWChat.getInstance().getGroupDelegate().setOnGroupUserClickListener(onGroupUserClickListener);
        return this;
    }

    /**
     * 发起群聊时启动选择界面
     *
     * @param context
     * @return
     */
    public CWApiGroup openSelectWhenCreate(Context context) {
        CWChat.getInstance().getGroupDelegate().startSelectUserActivity(context, "", CWGroupSelectUserType.CREATE, new ArrayList<CWSelectUser>(),"");
        return this;
    }
}
