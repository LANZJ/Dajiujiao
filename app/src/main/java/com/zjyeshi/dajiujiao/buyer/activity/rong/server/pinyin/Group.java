package com.zjyeshi.dajiujiao.buyer.activity.rong.server.pinyin;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table GROUP.
 */
public class Group {

    /**
     * Not-null value.
     */
    private String groupId;
    private String name;
    private String portraitUri;
    private String displayName;
    private String role;
    private Long timestamp;
    private String letters;

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public Group() {
    }

    public Group(String groupId) {
        this.groupId = groupId;
    }

    public Group(String groupId, String name, String portraitUri, String displayName, String role, Long timestamp) {
        this.groupId = groupId;
        this.name = name;
        this.portraitUri = portraitUri;
        this.displayName = displayName;
        this.role = role;
        this.timestamp = timestamp;
    }

    public Group(String groupId, String name, String portraitUri) {
        this.groupId = groupId;
        this.name = name;
        this.portraitUri = portraitUri;
    }

    /**
     * Not-null value.
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}
