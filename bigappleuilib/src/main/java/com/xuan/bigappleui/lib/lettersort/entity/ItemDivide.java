package com.xuan.bigappleui.lib.lettersort.entity;

/**
 * 分割线
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-16 下午3:36:52 $
 */
public class ItemDivide extends BaseItem {
	private String letter;// 分割线显示的字母

	public ItemDivide(String letter) {
		this.letter = letter;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

}
