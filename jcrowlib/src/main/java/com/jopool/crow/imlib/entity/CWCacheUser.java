package com.jopool.crow.imlib.entity;

import java.util.Date;

/**
 * Created by xuan on 16/11/2.
 */
public class CWCacheUser {
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_LOGO = "user_logo";
    public static final String EXT = "ext";
    public static final String CREATION_TIME = "creation_time";
    public static final String LAST_UPDATE_TIME = "last_update_time";

    private String userId;
    private String userName;
    private String userLogo;
    private String ext;
    private Date creationTime;
    private Date lastUpdateTime;

    public static CWCacheUser obtainNew(String userId, String userName, String userLogo){
        CWCacheUser cacheUser = new CWCacheUser();
        cacheUser.setUserId(userId);
        cacheUser.setUserName(userName);
        cacheUser.setUserLogo(userLogo);
        cacheUser.setExt("");
        cacheUser.setCreationTime(new Date());
        cacheUser.setLastUpdateTime(new Date());
        return cacheUser;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
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

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
