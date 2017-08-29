package com.jopool.crow.imkit.listeners.impl;

import com.alibaba.fastjson.JSON;
import com.jopool.crow.CWChat;
import com.jopool.crow.imkit.listeners.LoadUserInfoProvider;
import com.jopool.crow.imlib.constants.UrlConstants;
import com.jopool.crow.imlib.entity.CWCacheUser;
import com.jopool.crow.imlib.task.BaseRes;
import com.jopool.crow.imlib.utils.CWHttpUtil;
import com.jopool.crow.imlib.utils.CWLogUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 在本地没有缓存时,使用这个加在器加在用户信息,注意这个方法线程同步请
 *
 * @author xuan
 */
public class DefaultLoadUserInfoProvider implements LoadUserInfoProvider {
    @Override
    public CWCacheUser loadUserById(String userId) {
        //
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("userId", userId);
        try {
            CWHttpUtil.CWResponse response = CWHttpUtil.post(CWChat.getInstance().getConfig().getApiPrefix() + UrlConstants.GET_USER, paramMap);
            if (200 == response.getStatusCode()) {
                UserResp resp = JSON.parseObject(response.getResultStr(), UserResp.class);
                //设置用户返回数据
                UserResp userResp = resp.getResult();
                CWCacheUser cacheUser = CWCacheUser.obtainNew(userId, userResp.getUserName(), userResp.getUserLogo());
                cacheUser.setCreationTime(userResp.getCreationTime());
                return cacheUser;
            }
        } catch (Exception e) {
            CWLogUtil.e(e);
        }
        return null;
    }

    public static class UserResp extends BaseRes<UserResp> {
        private String id;
        private String userId;
        private String userName;
        private String userLogo;
        private Date modifyTime;
        private Date creationTime;

        public Date getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(Date creationTime) {
            this.creationTime = creationTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Date getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(Date modifyTime) {
            this.modifyTime = modifyTime;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserLogo() {
            return userLogo;
        }

        public void setUserLogo(String userLogo) {
            this.userLogo = userLogo;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }

}
