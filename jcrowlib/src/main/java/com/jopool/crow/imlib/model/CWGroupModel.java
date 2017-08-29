package com.jopool.crow.imlib.model;

import android.content.Context;

import com.jopool.crow.imlib.db.dao.CWChatDaoFactory;
import com.jopool.crow.imlib.entity.CWCacheGroup;
import com.jopool.crow.imlib.entity.CWGroup;
import com.jopool.crow.imlib.task.CWGetMsgTask;
import com.jopool.crow.imlib.task.CWGetServerConversationListTask;
import com.jopool.crow.imlib.task.CWGroupGetGroupTask;
import com.jopool.crow.imlib.utils.CWValidator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 会话
 *
 * @author xuan
 */
public class CWGroupModel {
    private static CWGroupModel instance = new CWGroupModel();

    private CWGroupModel() {
    }

    public static CWGroupModel getInstance() {
        return instance;
    }

    /**
     * 查找
     *
     * @param groupId
     * @return
     */
    public CWGroup getByGroupId(String groupId) {
        CWGroup group = new CWGroup();
        CWCacheGroup cacheGroup = CWChatDaoFactory.getCacheGroupDao().findByGroupId(groupId);
        if (null != cacheGroup) {
            group.setId(cacheGroup.getGroupId());
            group.setName(cacheGroup.getGroupName());
        } else {
            group.setId(groupId);
            group.setName("");
        }
        return group;
    }

    /**
     * 缓存收到的消息的发送人信息
     *
     * @param getMsgResResultItemList
     */
    public void cacheGroupByMsg(List<CWGetMsgTask.GetMsgResResultItem> getMsgResResultItemList) {
        List<CWCacheGroup> cacheGroupList = new ArrayList<CWCacheGroup>();
        for (CWGetMsgTask.GetMsgResResultItem getMsgResResultItem : getMsgResResultItemList) {
            if (CWValidator.isEmpty(getMsgResResultItem.getToGroupId())) {
                continue;//只有toGroupId非空才判定是群组消息,才需要缓存群组信息
            }
            CWCacheGroup cacheGroup = new CWCacheGroup();
            cacheGroup.setGroupId(getMsgResResultItem.getToGroupId());
            cacheGroup.setGroupName(getMsgResResultItem.getToGroupName());
            cacheGroup.setLastUpdateTime(new Date());
            cacheGroupList.add(cacheGroup);
        }
        CWChatDaoFactory.getCacheGroupDao().insertOrReplaceBatch(cacheGroupList);
    }

    /**
     * 群组缓存
     *
     * @param getGroupData
     */
    public void cacheGroupByGroup(CWGroupGetGroupTask.GetGroupData getGroupData) {
        List<CWCacheGroup> cacheGroupList = new ArrayList<CWCacheGroup>();
        CWCacheGroup cacheGroup = new CWCacheGroup();
        cacheGroup.setGroupId(getGroupData.getId());
        cacheGroup.setGroupName(getGroupData.getName());
        cacheGroup.setLastUpdateTime(new Date());
        cacheGroupList.add(cacheGroup);
        CWChatDaoFactory.getCacheGroupDao().insertOrReplaceBatch(cacheGroupList);
    }

    /**
     * 群组缓存
     *
     * @param appGroup
     */
    public void cacheGroupByConversation(CWGetServerConversationListTask.CWServerConversationList.CWServerConversation.ToAppGroup appGroup) {
        List<CWCacheGroup> cacheGroupList = new ArrayList<CWCacheGroup>();
        CWCacheGroup cacheGroup = new CWCacheGroup();
        cacheGroup.setGroupId(appGroup.getId());
        cacheGroup.setGroupName(appGroup.getName());
        cacheGroup.setLastUpdateTime(new Date());
        cacheGroupList.add(cacheGroup);
        CWChatDaoFactory.getCacheGroupDao().insertOrReplaceBatch(cacheGroupList);
    }

}
