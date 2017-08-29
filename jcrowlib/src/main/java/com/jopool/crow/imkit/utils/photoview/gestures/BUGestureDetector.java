package com.jopool.crow.imkit.utils.photoview.gestures;

import android.view.MotionEvent;

/**
 * 手势检测器
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-4 上午11:14:24 $
 */
public interface BUGestureDetector {

	/**
	 * 手势触摸监听
	 * 
	 * @param ev
	 * @return
	 */
	public boolean onTouchEvent(MotionEvent ev);

	/**
	 * 是否缩放
	 * 
	 * @return
	 */
	public boolean isScaling();

	/**
	 * 设置手势动作监听器
	 * 
	 * @param listener
	 */
	public void setOnGestureListener(BUOnGestureListener listener);

}
