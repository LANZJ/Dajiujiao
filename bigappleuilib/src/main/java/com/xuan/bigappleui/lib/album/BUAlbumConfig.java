package com.xuan.bigappleui.lib.album;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.xuan.bigappleui.lib.utils.ui.drawable.HookDrawable;

/**
 * 配置参数
 * 
 * @author xuan
 */
public class BUAlbumConfig {
	/** 显示小图时限制的宽高 */
	public int thumbnailQualityWidth = 200;
	public int thumbnailQualityHeight = 200;

	/** 头部的一些样式颜色 */
	public int titleBgColor = Color.parseColor("#414141");
	public int titleTextColor = Color.WHITE;
	/** 选中的资源 */
	public Drawable selectedDrawable = new HookDrawable();

	/**
	 * 标题栏背景
	 * 
	 * @param titleBgColor
	 * @return
	 */
	public BUAlbumConfig configTitleBgColor(int titleBgColor) {
		this.titleBgColor = titleBgColor;
		return this;
	}

	/**
	 * 标题栏文字
	 * 
	 * @param titleTextColor
	 * @return
	 */
	public BUAlbumConfig configTitleTextColor(int titleTextColor) {
		this.titleTextColor = titleTextColor;
		return this;
	}

	/**
	 * 选中状态
	 * 
	 * @param selectedDrawable
	 * @return
	 */
	public BUAlbumConfig configSelectedDrawable(Drawable selectedDrawable) {
		this.selectedDrawable = selectedDrawable;
		return this;
	}

}
