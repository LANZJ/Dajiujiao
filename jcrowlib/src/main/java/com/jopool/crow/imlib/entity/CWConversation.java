package com.jopool.crow.imlib.entity;

import com.jopool.crow.imlib.enums.CWConversationType;

import java.util.Date;

/**
 * 会话对象
 * 
 * @author xuan
 */
public class CWConversation {
	public static final String TABLE = "dg_conversation";

	public static final String TO_ID = "to_id";
	public static final String CONVERSATION_TYPE = "conversation_type";
	public static final String OWNER_USER_ID = "owner_user_id";
	public static final String PRIORITY = "priority";
	public static final String EXT = "ext";
	public static final String MODIFY_TIME = "modify_time";
	public static final String CREATION_TIME = "creation_time";

	private String toId;
	private CWConversationType conversationType;
	private String ownerUserId;
	private int priority;
	private String ext;
	private Date modifyTime;
	private Date creationTime;

	// dto
	private int unreadNum;// 单个会话未读消息
	private CWConversationMessage lastMessage;// 会话的最新一条消息
	private CWUser toUser;// 对聊的那个人
	private CWGroup toGroup;// 聊天所在群组

	public CWConversation() {
	}

	public static CWConversation obtain(String toId,
			CWConversationType conversationType) {
		CWConversation conversation = new CWConversation();
		conversation.setToId(toId);
		conversation.setConversationType(conversationType);
		conversation.setOwnerUserId(CWUser.getConnectUserId());
		conversation.setPriority(0);
		conversation.setExt("");
		conversation.setModifyTime(new Date());
		conversation.setCreationTime(new Date());
		return conversation;
	}

	/**
	 * 是否置顶聊天
	 *
	 * @return
	 */
	public boolean isPriorityUp(){
		return 1 == priority;
	}

	/**
	 * 是否单聊
	 * @return
	 */
	public boolean isUser(){
		return CWConversationType.USER.equals(conversationType);
	}

	/**
	 * 是否是群聊
	 *
	 * @return
	 */
	public boolean isGroup(){
		return CWConversationType.GROUP.equals(conversationType);
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public CWConversationType getConversationType() {
		return conversationType;
	}

	public void setConversationType(CWConversationType conversationType) {
		this.conversationType = conversationType;
	}

	public String getOwnerUserId() {
		return ownerUserId;
	}

	public void setOwnerUserId(String ownerUserId) {
		this.ownerUserId = ownerUserId;
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

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getUnreadNum() {
		return unreadNum;
	}

	public void setUnreadNum(int unreadNum) {
		this.unreadNum = unreadNum;
	}

	public CWConversationMessage getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(CWConversationMessage lastMessage) {
		this.lastMessage = lastMessage;
	}

	public CWUser getToUser() {
		return toUser;
	}

	public void setToUser(CWUser toUser) {
		this.toUser = toUser;
	}

	public CWGroup getToGroup() {
		return toGroup;
	}

	public void setToGroup(CWGroup toGroup) {
		this.toGroup = toGroup;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

}
