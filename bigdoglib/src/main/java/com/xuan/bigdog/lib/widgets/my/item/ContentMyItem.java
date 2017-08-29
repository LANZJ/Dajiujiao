package com.xuan.bigdog.lib.widgets.my.item;

/**
 * 内容item
 * 
 * @author xuan
 */
public class ContentMyItem extends BaseMyItem {
	public static final int SEPERATOR_TOP = 1;
	public static final int SEPERATOR_MIDDLE = 2;
	public static final int SEPERATOR_BOTTOM = 3;
	public int seperator = SEPERATOR_MIDDLE;

	public String leftImage;
	public String leftText;
	public String rightText;
	public boolean showRightIv;

	public ContentMyItem() {
	}

	public ContentMyItem(String leftImage, String leftText, String rightText,
			boolean showRightIv, int seperator) {
		this.leftImage = leftImage;
		this.leftText = leftText;
		this.rightText = rightText;
		this.showRightIv = showRightIv;
		this.seperator = seperator;
	}

	public ContentMyItem(String leftImage, String leftText,
			boolean showRightIv, int seperator) {
		this(leftImage, leftText, null, showRightIv, seperator);
	}

	public ContentMyItem(String leftText, boolean showRightIv, int seperator) {
		this(null, leftText, null, showRightIv, seperator);
	}

}
