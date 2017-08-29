package com.jopool.crow.imkit.utils.photoview.app.core;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 修改过几个异常的兼容性
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-5 下午5:17:32 $
 */
public class BUHackyViewPager extends ViewPager {
	private static final String TAG = "HackyViewPager";

	public BUHackyViewPager(Context context) {
		super(context);
	}

	public BUHackyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					"hacky viewpager IllegalArgumentException："
							+ e.getMessage());
			return false;
		} catch (ArrayIndexOutOfBoundsException e) {
			Log.e(TAG,
					"hacky viewpager ArrayIndexOutOfBoundsException："
							+ e.getMessage());
			return false;
		}
	}

}
