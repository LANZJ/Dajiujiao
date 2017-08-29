package com.jopool.crow.imlib.enums;


/**
 * 会话类型
 * 
 * @author xuan
 */
public enum CWConversationType {
	// 1：单聊、2：群聊
	USER(1), GROUP(2);

	private int value;

	CWConversationType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static CWConversationType valueStrOf(String valueStr) {
		CWConversationType conversationType = null;
		if("USER".equals(valueStr)){
			conversationType = USER;
		}else if("GROUP".equals(valueStr)){
			conversationType = GROUP;
		}else {
			conversationType = USER;// 默认就算单聊
		}
		return conversationType;
	}

	public static CWConversationType valueOf(int value) {
		CWConversationType conversationType = null;
		switch (value) {
		case 1:
			conversationType = USER;
			break;
		case 2:
			conversationType = GROUP;
			break;
		default:
			conversationType = USER;// 默认就算单聊
			break;
		}
		return conversationType;
	}

	public String getValueForTras() {
		String desc = null;
		switch (this) {
			case USER:
				desc = "USER";
				break;
			case GROUP:
				desc = "GROUP";
				break;
			default:
				desc = "USER";// 默认就算单聊
				break;
		}
		return desc;
	}

	public String getDescription() {
		String desc = null;
		switch (this) {
		case USER:
			desc = "单聊";
			break;
		case GROUP:
			desc = "群聊";
			break;
		default:
			desc = "单聊";// 默认就算单聊
			break;
		}
		return desc;
	}

	@Override
	public String toString() {
		return getDescription();
	}

	public boolean equals(CWConversationType conversationType) {
		if (null == conversationType) {
			return false;
		}
		return value == conversationType.value;
	}

}
