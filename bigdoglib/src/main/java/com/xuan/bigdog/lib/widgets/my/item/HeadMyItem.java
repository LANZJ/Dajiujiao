package com.xuan.bigdog.lib.widgets.my.item;

/**
 * 头部布局
 * 
 * @author xuan
 */
public class HeadMyItem extends BaseMyItem {
	public String head;
	public String name;

	public HeadMyItem() {
	}

	public HeadMyItem(String head, String name) {
		this.head = head;
		this.name = name;
	}

	public HeadMyItem(String name) {
		this.name = name;
	}

}
