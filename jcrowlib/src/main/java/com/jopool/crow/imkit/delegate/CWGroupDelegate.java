package com.jopool.crow.imkit.delegate;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.jopool.crow.imkit.activity.group.CWGroupDetailActivity;
import com.jopool.crow.imkit.activity.group.CWGroupDetailFragment;
import com.jopool.crow.imkit.activity.group.CWGroupUserSelectActivity;
import com.jopool.crow.imkit.activity.group.CWGroupUserSelectFragment;
import com.jopool.crow.imkit.listeners.GetGroupUserSelectListProvider;
import com.jopool.crow.imkit.listeners.OnGroupUserClickListener;
import com.jopool.crow.imkit.listeners.impl.DefaultGetGroupUserSelectProvider;
import com.jopool.crow.imkit.listeners.impl.DefaultOnGroupUserClickListner;
import com.jopool.crow.imlib.entity.CWSelectUser;
import com.jopool.crow.imlib.enums.CWGroupSelectUserType;

import java.util.ArrayList;
import java.util.List;

/**
 * Group相关代理
 * Created by wuhk on 2016/11/4.
 */
public class CWGroupDelegate {

    /**
     * 启动选择成员界面
     *
     * @param context
     * @param groupId
     */
    public void startSelectUserActivity(Context context, String groupId, CWGroupSelectUserType selectUserType, List<CWSelectUser> inGroupMemberList,String createId) {
        Intent intent = new Intent();
        intent.setClass(context, CWGroupUserSelectActivity.class);
        intent.putExtra(CWGroupUserSelectFragment.PARAM_GROUP_ID, groupId);
        intent.putExtra(CWGroupUserSelectFragment.PARAM_SELECT_USER_TYPE, selectUserType.getValue());
        String inGroupMemberJsonStr = JSON.toJSONString(inGroupMemberList);
        intent.putExtra(CWGroupUserSelectFragment.PARAM_IN_GROUP_MEMBER, inGroupMemberJsonStr);
        intent.putExtra(CWGroupUserSelectFragment.PARAM_SELECT_USEID,createId);
        //部分手机不添加会崩溃
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 新建群聊启动选择界面
     *
     * @param context
     */
    public void startSelectWhenCreate(Context context) {
        startSelectUserActivity(context, "", CWGroupSelectUserType.CREATE, new ArrayList<CWSelectUser>(),"");
    }

    /**
     * 添加成员启动选择界面
     *
     * @param context
     * @param groupId
     * @param inGroupMemberList
     */
    public void startSelectWhenAdd(Context context, String groupId, List<CWSelectUser> inGroupMemberList,String createId) {
        startSelectUserActivity(context, groupId, CWGroupSelectUserType.ADD, inGroupMemberList,createId);
    }

    /**
     * 删除成员启动选择界面
     *
     * @param context
     * @param groupId
     * @param inGroupMemberList
     */
    public void startSelectWhenRemove(Context context, String groupId, List<CWSelectUser> inGroupMemberList,String createId) {
        startSelectUserActivity(context, groupId, CWGroupSelectUserType.REMOVE, inGroupMemberList,createId);
    }

    /**
     * 启动群聊信息界面
     *
     * @param context
     */
    public void startGroupDetailActivity(Context context, String groupId) {
        Intent intent = new Intent();
        intent.setClass(context, CWGroupDetailActivity.class);
        intent.putExtra(CWGroupDetailFragment.PARAM_GROUP_ID, groupId);
        context.startActivity(intent);
    }

    /**
     * 启动修改群名界面
     *
     * @param context
     * @param name
     */
    public void startModifyGroupNameActivity(Context context, String name, String groupId) {
//        Intent intent = new Intent();
//        intent.setClass(context, CWGroupModifyNameActivity.class);
//        intent.putExtra(CWGroupModifyNameFragment.PARAM_GROUP_NAME, name);
//        intent.putExtra(CWGroupModifyNameFragment.PARAM_GROUP_ID, groupId);
//        context.startActivity(intent);
    }

    /**
     * 群聊用户选择列表提供
     */
    private GetGroupUserSelectListProvider getGroupUserSelectListProvider;

    public GetGroupUserSelectListProvider getGetGroupUserSelectListProvider() {
        if (null == getGroupUserSelectListProvider) {
            getGroupUserSelectListProvider = new DefaultGetGroupUserSelectProvider();
        }
        return getGroupUserSelectListProvider;
    }

    public void setGetGroupUserSelectListProvider(GetGroupUserSelectListProvider getGroupUserSelectListProvider) {
        this.getGroupUserSelectListProvider = getGroupUserSelectListProvider;
    }

    /**
     * 群组用户头像点击事件
     */
    private OnGroupUserClickListener onGroupUserClickListener;

    public OnGroupUserClickListener getOnGroupUserClickListener() {
        if (null == onGroupUserClickListener) {
            onGroupUserClickListener = new DefaultOnGroupUserClickListner();
        }
        return onGroupUserClickListener;
    }

    public void setOnGroupUserClickListener(OnGroupUserClickListener onGroupUserClickListener) {
        this.onGroupUserClickListener = onGroupUserClickListener;
    }
}
