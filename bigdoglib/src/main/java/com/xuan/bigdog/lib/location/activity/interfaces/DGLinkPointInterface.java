package com.xuan.bigdog.lib.location.activity.interfaces;

import java.util.List;

import com.xuan.bigdog.lib.location.DGPoint;

/**
 * 地图连接点操作
 * 
 * @author xuan
 */
public interface DGLinkPointInterface {
	public static final int DEFAULT_STROKE_WIDTH = 4;
	public static final int DEFAULT_STROKE_COLOR = 0x8800FF00;
	public static final int DEFAULT_FILL_COLOR = 0x44FFFF00;

	/**
	 * 连接点
	 * 
	 * @param pointList
	 */
	void doLinkPoint(List<DGPoint> pointList);

	/**
	 * 连接点
	 * 
	 * @param pointList
	 * @param strokeWidth
	 * @param strokeColor
	 * @param fillColor
	 */
	void doLinkPoint(List<DGPoint> pointList, int strokeWidth, int strokeColor,
					 int fillColor);

}
