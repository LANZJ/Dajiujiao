package com.jopool.crow.imlib.enums;

/**
 * 消息内容类型
 *
 * @author xuan
 */
public enum CWMessageContentType {
    // 1：文本、2：图片、3：语音、4：网页、9：网页、99：其他
    TEXT(1), IMAGE(2), VOICE(3), URL(4), SYSTEM(5), CUSTOM(9), OTHER(99);
    private int value;

    CWMessageContentType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getValueStr() {
        return String.valueOf(value);
    }

    /**
     * 本地存储类型用的是数字
     *
     * @param value
     * @return
     */
    public static CWMessageContentType valueOf(int value) {
        CWMessageContentType messageContentType = null;
        switch (value) {
            case 1:
                messageContentType = TEXT;
                break;
            case 2:
                messageContentType = IMAGE;
                break;
            case 3:
                messageContentType = VOICE;
                break;
            case 4:
                messageContentType = URL;
                break;
            case 5:
                messageContentType = SYSTEM;
                break;
            case 9:
                messageContentType = CUSTOM;
                break;
            default:
                messageContentType = OTHER;
                break;
        }
        return messageContentType;
    }

    /**
     * 传输用的是字符串
     *
     * @param valueStr
     * @return
     */
    public static CWMessageContentType valueStrOf(int valueStr) {
        CWMessageContentType messageContentType = null;
        if ("TEXT".equals(valueStr)) {
            messageContentType = TEXT;
        } else if ("IMAGE".equals(valueStr)) {
            messageContentType = IMAGE;
        } else if ("VOICE".equals(valueStr)) {
            messageContentType = VOICE;
        } else if ("URL".equals(valueStr)) {
            messageContentType = URL;
        } else if ("CUSTOM".equals(valueStr)) {
            messageContentType = CUSTOM;
        } else if ("SYSTEM".equals(valueStr)) {
            messageContentType = SYSTEM;
        } else {
            messageContentType = OTHER;
        }

        return messageContentType;
    }

    public String getValueForTras() {
        String desc = null;
        switch (this) {
            case TEXT:
                desc = "TEXT";
                break;
            case IMAGE:
                desc = "IMAGE";
                break;
            case VOICE:
                desc = "VOICE";
                break;
            case URL:
                desc = "URL";
                break;
            case SYSTEM:
                desc = "SYSTEM";
                break;
            case CUSTOM:
                desc = "CUSTOM";
                break;
            case OTHER:
            default:
                desc = "OTHER";
                break;
        }
        return desc;
    }

    public String getDescription() {
        String desc = null;
        switch (this) {
            case TEXT:
                desc = "文本";
                break;
            case IMAGE:
                desc = "图片";
                break;
            case VOICE:
                desc = "语音";
                break;
            case URL:
                desc = "网页链接";
                break;
            case SYSTEM:
                desc = "系统消息";
                break;
            case CUSTOM:
                desc = "自定义消息";
                break;
            default:
                desc = "未知";
                break;
        }
        return desc;
    }

    public boolean equals(CWMessageContentType messageContentType) {
        if (null == messageContentType) {
            return false;
        }
        return value == messageContentType.value;
    }

}
