package com.jopool.crow.imlib.entity;

import com.alibaba.fastjson.JSON;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.jopool.crow.imlib.enums.CWMessageContentType;
import com.jopool.crow.imlib.enums.CWMessageType;
import com.jopool.crow.imlib.enums.CWReadStatus;
import com.jopool.crow.imlib.enums.CWSendStatus;
import com.jopool.crow.imlib.task.CWGetMsgTask;
import com.jopool.crow.imlib.utils.uuid.CWUUIDUtils;

import java.util.Date;

/**
 * 会话消息
 *
 * @author xuan
 */
public class CWConversationMessage {
    public static final String TABLE = "dg_conversation_message";

    public static final String ID = "id";
    public static final String OWNER_USER_ID = "owner_user_id";
    public static final String SENDER_USER_ID = "sender_user_id";
    public static final String CONVERSATION_TYPE = "conversation_type";
    public static final String CONVERSATION_TO_ID = "conversation_to_id";
    public static final String MESSAGE_TYPE = "message_type";
    public static final String MESSAGE_CONTENT_TYPE = "message_content_type";
    public static final String CONTENT = "content";
    public static final String DOWNLOAD_URL = "download_url";
    public static final String VOICE_LENGTH = "voice_length";
    public static final String READ_STATUS = "read_status";
    public static final String SEND_STATUS = "send_status";
    public static final String EXT = "ext";
    public static final String MODIFY_TIME = "modify_time";
    public static final String CREATION_TIME = "creation_time";

    private String id;
    private String ownerUserId;
    private String senderUserId;
    private CWConversationType conversationType;
    private String conversationToId;
    private CWMessageType messageType;
    private CWMessageContentType messageContentType;
    private String content;
    private String downloadUrl;
    private int voiceLength;
    private CWReadStatus readStatus;
    private CWSendStatus sendStatus;
    private String ext;
    private Date modifyTime;
    private Date creationTime;

    public static String[] getAllColumns() {
        return new String[]{ID, OWNER_USER_ID, SENDER_USER_ID,
                CONVERSATION_TYPE, CONVERSATION_TO_ID, MESSAGE_TYPE,
                MESSAGE_CONTENT_TYPE, CONTENT, DOWNLOAD_URL, VOICE_LENGTH,
                READ_STATUS, SEND_STATUS, EXT, MODIFY_TIME, CREATION_TIME};
    }

    /**
     * 生成文本消息
     *
     * @param senderUserId
     * @param conversationType
     * @param conversationToId
     * @param messageType
     * @param content
     * @param readStatus
     * @param sendStatus
     * @return
     */
    public static CWConversationMessage obtain(String senderUserId,
                                               CWConversationType conversationType, String conversationToId,
                                               CWMessageType messageType, CWMessageContentType messageContentType,
                                               String content, String downloadUrl, int voiceLength,
                                               CWReadStatus readStatus, CWSendStatus sendStatus) {
        CWConversationMessage m = new CWConversationMessage();
        m.setId(CWUUIDUtils.createId());//
        m.setOwnerUserId(CWUser.getUser().getUserId());
        m.setSenderUserId(senderUserId);
        m.setConversationType(conversationType);
        m.setConversationToId(conversationToId);
        m.setMessageType(messageType);
        m.setMessageContentType(messageContentType);
        m.setContent(content);
        m.setDownloadUrl(downloadUrl);
        m.setVoiceLength(voiceLength);
        m.setReadStatus(readStatus);
        m.setSendStatus(sendStatus);
        m.setExt("");
        m.setModifyTime(new Date());//
        m.setCreationTime(new Date());//
        return m;
    }

    public boolean isText() {
        return CWMessageContentType.TEXT.equals(messageContentType);
    }

    public boolean isImage() {
        return CWMessageContentType.IMAGE.equals(messageContentType);
    }

    public boolean isVoice() {
        return CWMessageContentType.VOICE.equals(messageContentType);
    }

    public boolean isSystem() {
        return CWMessageContentType.SYSTEM.equals(messageContentType);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public CWConversationType getConversationType() {
        return conversationType;
    }

    public void setConversationType(CWConversationType conversationType) {
        this.conversationType = conversationType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getVoiceLength() {
        return voiceLength;
    }

    public void setVoiceLength(int voiceLength) {
        this.voiceLength = voiceLength;
    }

    public CWReadStatus getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(CWReadStatus readStatus) {
        this.readStatus = readStatus;
    }

    public CWSendStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(CWSendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getConversationToId() {
        return conversationToId;
    }

    public void setConversationToId(String conversationToId) {
        this.conversationToId = conversationToId;
    }

    public CWMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(CWMessageType messageType) {
        this.messageType = messageType;
    }

    public CWMessageContentType getMessageContentType() {
        return messageContentType;
    }

    public void setMessageContentType(CWMessageContentType messageContentType) {
        this.messageContentType = messageContentType;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    /**
     * 是否是群组消息
     *
     * @return
     */
    public boolean isGroup() {
        return CWConversationType.GROUP.equals(conversationType);
    }

    /**
     * 消息转化成JSON协议串
     *
     * @return
     */
    public String encode() {
        return JSON.toJSONString(this);
    }

    /**
     * JSON协议串转化消息
     *
     * @param messageJsonStr
     * @return
     */
    public static CWConversationMessage decode(String messageJsonStr) {
        return JSON.parseObject(messageJsonStr, CWConversationMessage.class);
    }

    /**
     * 把网络上收到到的消息转换成本地支持的消息
     *
     * @param netMessage
     * @return
     */
    public static CWConversationMessage adapterLocalFromNet(CWConversationMessage netMessage , String realStatus) {
        //拥有者id
        netMessage.setOwnerUserId(CWUser.getConnectUserId());
        //左右两边消息类型设置
        if (CWUser.getConnectUserId().equals(netMessage.getSenderUserId())) {
            //自己的发的
            netMessage.setMessageType(CWMessageType.RIGHT);
        } else {
            netMessage.setMessageType(CWMessageType.LEFT);
        }
        //消息阅读状态设置
        if (CWUser.getConnectUserId().equals(netMessage.getSenderUserId())) {
            //自己的发的
            if (CWMessageContentType.VOICE.equals(netMessage.getMessageContentType())) {
                netMessage.setReadStatus(CWReadStatus.LISTENED);
            } else {
                netMessage.setReadStatus(CWReadStatus.READED);
            }
        } else {
            if (!realStatus.equals(CWGetMsgTask.GetMsgResResultItem.REAL_STATUS_READED)){
                netMessage.setReadStatus(CWReadStatus.UNREAD);
            }else{
                netMessage.setReadStatus(CWReadStatus.READED);
            }
        }
        //消息发送状态设置
        netMessage.setSendStatus(CWSendStatus.SUCCESS);
        //消息对方id设置
        if (CWConversationType.USER.equals(netMessage.getConversationType())) {
            //单聊的toId就是对方的toId,所以如果不是我发送的toId要设置senderUserId,如果是我发送的toId本身就是对方id就不用管了
            if (!CWUser.getConnectUserId().equals(netMessage.getSenderUserId())) {
                netMessage.setConversationToId(netMessage.getSenderUserId());
            }
        }
        //语音的content存在的对方本地地址,所以清空
        if (CWMessageContentType.VOICE.equals(netMessage.getMessageContentType())) {
            netMessage.setContent("");
        }
        //系统消息设置成中间布局
        if (CWMessageContentType.SYSTEM.equals(netMessage.getMessageContentType())) {
            netMessage.setMessageType(CWMessageType.CENTER);
        }
        return netMessage;
    }

}
