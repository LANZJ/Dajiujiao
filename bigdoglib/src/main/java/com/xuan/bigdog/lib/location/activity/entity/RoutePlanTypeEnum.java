package com.xuan.bigdog.lib.location.activity.entity;

/**
 * 路线规划类型枚举
 * 
 * @author xuan
 */
public enum RoutePlanTypeEnum {

	// 1：开车、2：做公交、0：步行
	DRIVING(1), TRANSIT(2), WALKING(3);
	private int value;

	RoutePlanTypeEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static RoutePlanTypeEnum valueOf(int value) {
		RoutePlanTypeEnum routePlanTypeEnum = null;
		switch (value) {
		case 1:
			routePlanTypeEnum = DRIVING;
			break;
		case 2:
			routePlanTypeEnum = TRANSIT;
			break;
		case 3:
		default:
			routePlanTypeEnum = WALKING;
			break;
		}

		return routePlanTypeEnum;
	}

	public String getDescription() {
		String desc = null;
		switch (this) {
		case DRIVING:
			desc = "开车";
			break;
		case TRANSIT:
			desc = "做公交";
			break;
		case WALKING:
		default:
			desc = "步行";
			break;
		}

		return desc;
	}

	@Override
	public String toString() {
		return getDescription();
	}

	public boolean equals(RoutePlanTypeEnum routePlanTypeEnum) {
		if (null == routePlanTypeEnum) {
			return false;
		}

		return value == routePlanTypeEnum.value;
	}

}
