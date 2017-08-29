package com.jopool.crow.imlib.soket;

/**
 * 错误对象
 * 
 * @author xuan
 */
public class CWErrorCode {
	private Code code;
	private String message;
	private Throwable e;

	public CWErrorCode(Code code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * 获取一个错误对象
	 * 
	 * @param code
	 * @param message
	 * @return
	 */
	public static CWErrorCode obtain(Code code, String message) {
		CWErrorCode errorCode = new CWErrorCode(code, message);
		return errorCode;
	}

	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Throwable getE() {
		return e;
	}

	public void setE(Throwable e) {
		this.e = e;
	}

	public static enum Code {
		// 408：请求超时、500：服务器错误、-1：未知错误
		// 801:消息不支持错误、802：异常错误、803：无网络
		TIMEOUT(408), SERVER_ERROR(500), UNSUPPORT(801), EXCEPTION(802), NONETWORK(
				803), UNKOWN(-1);
		private final int value;

		Code(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static Code valueOf(int value) {
			Code code = null;
			switch (value) {
			case 408:
				code = TIMEOUT;
				break;
			case 500:
				code = TIMEOUT;
				break;
			case 801:
				code = UNSUPPORT;
				break;
			case 802:
				code = EXCEPTION;
				break;
			case 803:
				code = NONETWORK;
				break;
			default:
				code = UNKOWN;
				break;
			}

			return code;
		}

		public String getDescription() {
			String desc = null;
			switch (this) {
			case TIMEOUT:
				desc = "请求超时";
				break;
			case SERVER_ERROR:
				desc = "服务器错误";
				break;
			case UNSUPPORT:
				desc = "消息不支持";
				break;
			case EXCEPTION:
				desc = "异常错误";
				break;
			case NONETWORK:
				desc = "无网络";
				break;
			default:
				desc = "未知错误";
				break;
			}

			return desc;
		}

		public boolean equals(Code code) {
			if (null == code) {
				return false;
			}

			return value == code.value;
		}
	}

}
