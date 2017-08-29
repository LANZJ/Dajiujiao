package com.zjyeshi.dajiujiao.buyer.chat.consumer;

/**
 * Created by wuhk on 2017/1/10.
 */

public class UnReadNumPush {
    private String message;
    private String type;
    private String serverTime;
    private String version;
    private Content content;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public static class Content{
        private int CONTENTKEY_TOTAL_UNREAD_COUNT;

        public int getCONTENTKEY_TOTAL_UNREAD_COUNT() {
            return CONTENTKEY_TOTAL_UNREAD_COUNT;
        }

        public void setCONTENTKEY_TOTAL_UNREAD_COUNT(int CONTENTKEY_TOTAL_UNREAD_COUNT) {
            this.CONTENTKEY_TOTAL_UNREAD_COUNT = CONTENTKEY_TOTAL_UNREAD_COUNT;
        }
    }


}
