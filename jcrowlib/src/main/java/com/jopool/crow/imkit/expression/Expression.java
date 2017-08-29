package com.jopool.crow.imkit.expression;

/**
 * 表情对象
 * 
 * @author xuan
 */
public class Expression {
	private String replaceText;
	private int showImage;

	public Expression(String replaceText, int showImage) {
		this.replaceText = replaceText;
		this.showImage = showImage;
	}

	public String getReplaceText() {
		return replaceText;
	}

	public void setReplaceText(String replaceText) {
		this.replaceText = replaceText;
	}

	public int getShowImage() {
		return showImage;
	}

	public void setShowImage(int showImage) {
		this.showImage = showImage;
	}

}
