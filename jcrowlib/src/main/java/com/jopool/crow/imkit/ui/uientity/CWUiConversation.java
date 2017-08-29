package com.jopool.crow.imkit.ui.uientity;

import com.jopool.crow.imlib.enums.CWConversationType;

import java.util.Date;

/**
 * 会话列表
 *
 * Created by xuan on 15/11/30.
 */
public class CWUiConversation {
    private String title;//会话标题
    private String detail;//会话详情
    private Date time;//时间
    private String url;//图片地址
    private int unreadNum;//未读消息
    private int priority;//优先级,1一般是置顶,0一般

    private String toId;//单聊表示对方id,群聊表示群组id
    private CWConversationType toType;//聊天会话类型

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUnreadNum() {
        return unreadNum;
    }

    public void setUnreadNum(int unreadNum) {
        this.unreadNum = unreadNum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public CWConversationType getToType() {
        return toType;
    }

    public void setToType(CWConversationType toType) {
        this.toType = toType;
    }

}
