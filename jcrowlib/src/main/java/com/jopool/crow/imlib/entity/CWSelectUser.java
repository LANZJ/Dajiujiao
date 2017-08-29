package com.jopool.crow.imlib.entity;

/**
 * 选择人员实体类
 * Created by wuhk on 2016/11/3.
 */
public class CWSelectUser {
    private String userId;
    private String userName;
    private String userLogo;
    private boolean selected;//选择时是否选中
    private boolean groupMember; //是否已经是群成员

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isGroupMember() {
        return groupMember;
    }

    public void setGroupMember(boolean groupMember) {
        this.groupMember = groupMember;
    }

    public void transFromCWUser(CWUser user){
        setUserId(user.getUserId());
        setUserName(user.getName());
        setUserLogo(user.getUrl());
    }
}
