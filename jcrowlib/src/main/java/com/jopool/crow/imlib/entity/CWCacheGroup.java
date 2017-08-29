package com.jopool.crow.imlib.entity;

import java.util.Date;

/**
 * Created by xuan on 16/11/2.
 */
public class CWCacheGroup {
    public static final String GROUP_ID = "group_id";
    public static final String GROUP_NAME = "group_name";
    public static final String LAST_UPDATE_TIME = "last_update_time";

    private String groupId;
    private String groupName;
    private Date lastUpdateTime;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
