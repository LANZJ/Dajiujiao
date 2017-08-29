package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * 用户角色枚举
 * 
 * @author wuhk
 * 
 */
public enum UserEnum {
	// 1、消费者 2、终端 3、经销商 4、总代理 5、业务员 99、系统管理员 100、未知
	CUSTOMER(1), TERMINAL(2), DEALER(3) , AGENT(4) , SALESMAN(5) , ADMINISTRATOR(99) ,UNKNOW(100);

	private int value;

	private UserEnum(int type) {
		this.value = type;
	}

	public int getValue() {
		return value;
	}

	public static UserEnum valueOf(int userType) {
		switch (userType){
			case 1:
				return CUSTOMER;
			case 2:
				return TERMINAL;
			case 3:
				return DEALER;
			case 4:
				return AGENT;
			case 5:
				return SALESMAN;
			case 99:
				return ADMINISTRATOR;
			default:
				return UNKNOW;
		}
	}

	@Override
	public String toString() {
		switch (this) {
			case CUSTOMER:
				return "消费者";
			case TERMINAL:
				return "终端";
			case DEALER:
				return "经销商";
			case AGENT:
				return "总代理";
			case SALESMAN:
				return "业务员";
			case ADMINISTRATOR:
				return "系统管理员";
			default:
				return "未知";
		}
	};

	public boolean equals(UserEnum userEnum) {
		if (null == userEnum) {
			return false;
		}

		return value == userEnum.value;
	}

}
