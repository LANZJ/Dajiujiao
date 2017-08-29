package com.jopool.crow.imlib.model;

import com.jopool.crow.imlib.db.dao.CWChatDaoFactory;
import com.jopool.crow.imlib.entity.CWCacheUser;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.task.CWGetMsgTask;
import com.jopool.crow.imlib.task.CWGetServerConversationListTask;
import com.jopool.crow.imlib.task.CWGroupGetGroupTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户相关
 *
 * @author xuan
 */
public class CWUserModel {
    private static CWUserModel instance = new CWUserModel();

    private CWUserModel() {
    }

    public static CWUserModel getInstance() {
        return instance;
    }

    /**
     * 查找用户
     *
     * @param userId
     * @return
     */
    public CWUser getByUserId(String userId) {
        CWUser user = new CWUser();
        CWCacheUser cacheUser = CWChatDaoFactory.getCacheUserDao().findByUserId(userId);
        if (null != cacheUser) {
            user.setUserId(cacheUser.getUserId());
            user.setToken(cacheUser.getUserId());
            user.setName(cacheUser.getUserName());
            user.setUrl(cacheUser.getUserLogo());
        } else {
            user.setUserId(userId);
            user.setToken(userId);
            user.setName(userId);
        }
        return user;
    }

    /**
     * 缓存收到的消息的发送人信息
     *
     * @param getMsgResResultItemList
     */
    public void cacheUserByMsg(List<CWGetMsgTask.GetMsgResResultItem> getMsgResResultItemList) {
        List<CWCacheUser> cacheUserList = new ArrayList<CWCacheUser>();
        for (CWGetMsgTask.GetMsgResResultItem getMsgResResultItem : getMsgResResultItemList) {
            CWCacheUser cacheUser = new CWCacheUser();
            cacheUser.setUserId(getMsgResResultItem.getFromUserId());
            cacheUser.setUserName(getMsgResResultItem.getFromUserName());
            cacheUser.setUserLogo(getMsgResResultItem.getFromUserLogo());
            cacheUser.setCreationTime(getMsgResResultItem.getCreationTime());
            cacheUser.setLastUpdateTime(new Date());
            cacheUserList.add(cacheUser);
        }
        CWChatDaoFactory.getCacheUserDao().insertOrReplaceBatch(cacheUserList);
    }

    /**
     * 从群组信息缓存用户
     *
     * @param getGroupData
     */
    public void cacheUserByGroup(CWGroupGetGroupTask.GetGroupData getGroupData) {
        List<CWGroupGetGroupTask.GetGroupUserData> getGroupUserDataList = getGroupData.getGroupUserList();
        List<CWCacheUser> cacheUserList = new ArrayList<CWCacheUser>();
        for (CWGroupGetGroupTask.GetGroupUserData getGroupUserData : getGroupUserDataList) {
            CWCacheUser cacheUser = new CWCacheUser();
            cacheUser.setUserId(getGroupUserData.getUserId());
            cacheUser.setUserName(getGroupUserData.getUserName());
            cacheUser.setUserLogo(getGroupUserData.getUserLogo());
            cacheUser.setCreationTime(new Date(getGroupData.getCreationTime()));
            cacheUser.setLastUpdateTime(new Date());
            cacheUserList.add(cacheUser);
        }
        CWChatDaoFactory.getCacheUserDao().insertOrReplaceBatch(cacheUserList);
    }

    /**
     * 缓存用户
     *
     * @param appUser
     */
    public void cacheUserByConversation(CWGetServerConversationListTask.CWServerConversationList.CWServerConversation.ToAppUser appUser) {
        List<CWCacheUser> cacheUserList = new ArrayList<CWCacheUser>();
        CWCacheUser cacheUser = new CWCacheUser();
        cacheUser.setUserId(appUser.getUserId());
        cacheUser.setUserName(appUser.getUserName());
        cacheUser.setUserLogo(appUser.getUserLogo());
        cacheUser.setCreationTime(new Date());
        cacheUser.setLastUpdateTime(new Date());
        cacheUserList.add(cacheUser);
        CWChatDaoFactory.getCacheUserDao().insertOrReplaceBatch(cacheUserList);
    }

    /**
     * 缓存用户
     *
     * @param cacheUser
     */
    public void cacheUser(CWCacheUser cacheUser) {
        List<CWCacheUser> cacheUserList = new ArrayList<CWCacheUser>();
        cacheUserList.add(cacheUser);
        CWChatDaoFactory.getCacheUserDao().insertOrReplaceBatch(cacheUserList);
    }

}
