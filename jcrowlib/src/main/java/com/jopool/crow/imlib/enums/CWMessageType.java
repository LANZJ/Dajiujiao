package com.jopool.crow.imlib.enums;

/**
 * 消息显示类型
 * 
 * @author xuan
 */
public enum CWMessageType {
	// 1：显示在左边、2：显示在右边、3：显示在中间提示、4：其他
	LEFT(1), RIGHT(2), CENTER(3), OTHER(99);
	private int value;

	CWMessageType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static CWMessageType valueOf(int value) {
		CWMessageType messageType = null;
		switch (value) {
		case 1:
			messageType = LEFT;
			break;
		case 2:
			messageType = RIGHT;
			break;
		case 3:
			messageType = CENTER;
			break;
		default:
			messageType = OTHER;
			break;
		}

		return messageType;
	}

	public static CWMessageType valueStrOf(String valueStr) {
		CWMessageType messageType = null;
		if("LEFT".equals(valueStr)){
			messageType = LEFT;
		}else if("RIGHT".equals(valueStr)){
			messageType = RIGHT;
		}else if("CENTER".equals(valueStr)){
			messageType = CENTER;
		}else if("OTHER".equals(valueStr)){
			messageType = OTHER;
		}

		return messageType;
	}

	public String getValueForTras() {
		String desc = null;
		switch (this) {
			case LEFT:
				desc = "LEFT";
				break;
			case RIGHT:
				desc = "RIGHT";
				break;
			case CENTER:
				desc = "CENTER";
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
		case LEFT:
			desc = "显示在左边";
			break;
		case RIGHT:
			desc = "显示在右边";
			break;
		case CENTER:
			desc = "显示在中间的通知";
			break;
		default:
			desc = "未知";
			break;
		}

		return desc;
	}

	public boolean equals(CWMessageType messageType) {
		if (null == messageType) {
			return false;
		}

		return value == messageType.value;
	}

}
