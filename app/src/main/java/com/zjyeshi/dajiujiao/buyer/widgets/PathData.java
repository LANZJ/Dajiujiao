package com.zjyeshi.dajiujiao.buyer.widgets;

/**
 * Created by wuhk on 2016/12/15.
 */
public class PathData {
    private String name;
    private String avatar;
    private long creationiTime;
    private boolean showCheck;//显示图片，默认打钩
    private boolean refused;//是否拒绝，默认通过

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getCreationiTime() {
        return creationiTime;
    }

    public void setCreationiTime(long creationiTime) {
        this.creationiTime = creationiTime;
    }

    public boolean isShowCheck() {
        return showCheck;
    }

    public void setShowCheck(boolean showCheck) {
        this.showCheck = showCheck;
    }

    public boolean isRefused()
    {
        return refused;
    }
    public void setRefused(boolean refused) {
        this.refused = refused;
    }
}
