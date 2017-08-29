package com.xuan.bigappleui.lib.view.listview.pulltorefresh;

/**
 * 下拉刷新各种状态枚举
 * 
 * @author xuan
 */
public enum PullDownStateEnum {
	// 0:下拉刷新、1:松开刷新、2正在刷新、3刷新完成
	PULL_TO_REFRESH(0), RELEASE_TO_REFRESH(1), REFRESHING(2), DONE(3);

	private int value;

	PullDownStateEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public boolean equals(PullDownStateEnum pullDownStateEnum) {
		if (null == pullDownStateEnum) {
			return false;
		}

		return value == pullDownStateEnum.value;
	}

}
