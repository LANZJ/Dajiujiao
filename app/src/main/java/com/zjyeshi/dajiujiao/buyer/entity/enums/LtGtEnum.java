package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * 刷新时间状态
 *
 * Created by wuhk on 2015/11/4.
 */
public enum LtGtEnum {
	// 1、小于指定时间（一般用在第一次和上拉加载更多） 2、大于指定时间（一般用在下拉刷新）
	LT("lt"), GT("gt");

	private String value;

	private LtGtEnum(String typeStr) {
		this.value = typeStr;
	}

	public String getValueStr() {
		return value;
	}

	public static LtGtEnum valueOfStr(String v) {
		if ("gt".equals(v)) {
			return GT;
		} else {
			return LT;
		}
	}

	@Override
	public String toString() {
		switch (this) {
		case LT:
			return "小于指定时间";
		case GT:
			return "大于指定时间";
		default:
			return "小于指定时间";
		}
	};

	public boolean equals(LtGtEnum ltGtEnum) {
		if (null == ltGtEnum) {
			return false;
		}

		return value.equals(ltGtEnum.getValueStr());
	}

}
