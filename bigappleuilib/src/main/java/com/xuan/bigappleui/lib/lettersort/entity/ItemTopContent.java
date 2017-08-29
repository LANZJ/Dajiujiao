package com.xuan.bigappleui.lib.lettersort.entity;

/**
 * 显示在通讯录之上的项目
 * 
 * @author xuan
 */
public class ItemTopContent extends ItemContent {

	private int level;// 根据这个level的大小来排序显示在哪个位置

	public ItemTopContent(int level, int tag) {
		this(null, null, level, tag);
	}

	public ItemTopContent(String name, Object value, int level, int tag) {
		super(name, value);
		this.level = level;
		setTag(tag);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
