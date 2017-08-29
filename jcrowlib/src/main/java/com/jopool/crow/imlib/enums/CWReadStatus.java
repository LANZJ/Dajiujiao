package com.jopool.crow.imlib.enums;

/**
 * 已读未读状态
 * 
 * @author xuan
 */
public enum CWReadStatus {
	// 1：已读、2：未读、3：已听（对语音而言）
	READED(1), UNREAD(2), LISTENED(3);
	private int value;

	CWReadStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public String getValueStr() {
		return String.valueOf(value);
	}

	public static CWReadStatus valueOf(int value) {
		CWReadStatus readStatus = null;
		switch (value) {
			case 1:
				readStatus = READED;
				break;
			case 2:
				readStatus = UNREAD;
				break;
			case 3:
				readStatus = LISTENED;
				break;
			default:
				readStatus = UNREAD;
				break;
		}

		return readStatus;
	}

	public static CWReadStatus valueStrOf(String valueStr) {
		CWReadStatus readStatus = null;
		if("READED".equals(valueStr)){
			readStatus = READED;
		}
		else if("UNREAD".equals(valueStr)){
			readStatus = UNREAD;
		}
		else if("LISTENED".equals(valueStr)){
			readStatus = LISTENED;
		}
		else {
			readStatus = UNREAD;
		}

		return readStatus;
	}

	public String getValueForTras() {
		String desc = null;
		switch (this) {
			case UNREAD:
				desc = "UNREAD";
				break;
			case LISTENED:
				desc = "LISTENED";
				break;
			case READED:
			default:
				desc = "READED";
				break;
		}

		return desc;
	}

	public String getDescription() {
		String desc = null;
		switch (this) {
		case READED:
			desc = "已读";
			break;
		case UNREAD:
			desc = "未读";
			break;
		case LISTENED:
			desc = "已听";
			break;
		default:
			desc = "未读";
			break;
		}

		return desc;
	}

	public boolean equals(CWReadStatus readStatus) {
		if (null == readStatus) {
			return false;
		}

		return value == readStatus.value;
	}

}
