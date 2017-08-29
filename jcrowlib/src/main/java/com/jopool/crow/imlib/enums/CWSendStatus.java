package com.jopool.crow.imlib.enums;

/**
 * 发送状态
 * 
 * @author xuan
 */
public enum CWSendStatus {
	// 1：发送中、2：发送失败、3：发送成功
	ING(1), FAIL(2), SUCCESS(3);
	private int value;

	CWSendStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static CWSendStatus valueOf(int value) {
		CWSendStatus sendStatus = null;
		switch (value) {
		case 1:
			sendStatus = ING;
			break;
		case 2:
			sendStatus = FAIL;
			break;
		case 3:
			sendStatus = SUCCESS;
			break;
		default:
			sendStatus = FAIL;
			break;
		}

		return sendStatus;
	}

	public static CWSendStatus valueStrOf(String valueStr) {
		CWSendStatus sendStatus = null;
		if("ING".equals(valueStr)){
			sendStatus = ING;
		}
		else if("FAIL".equals(valueStr)){
			sendStatus = FAIL;
		}
		else if("SUCCESS".equals(valueStr)){
			sendStatus = SUCCESS;
		}else{
			sendStatus = FAIL;
		}

		return sendStatus;
	}

	public String getValueForTras() {
		String desc = null;
		switch (this) {
			case ING:
				desc = "ING";
				break;
			case FAIL:
				desc = "FAIL";
				break;
			case SUCCESS:
				desc = "SUCCESS";
				break;
			default:
				desc = "FAIL";
				break;
		}

		return desc;
	}

	public String getDescription() {
		String desc = null;
		switch (this) {
		case ING:
			desc = "发送中";
			break;
		case FAIL:
			desc = "发送失败";
			break;
		case SUCCESS:
			desc = "发送成功";
			break;
		default:
			desc = "发送失败";
			break;
		}

		return desc;
	}

	public boolean equals(CWSendStatus sendStatus) {
		if (null == sendStatus) {
			return false;
		}

		return value == sendStatus.value;
	}

}
