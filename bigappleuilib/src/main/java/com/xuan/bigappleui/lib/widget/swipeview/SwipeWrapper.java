package com.xuan.bigappleui.lib.widget.swipeview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 
 * @author xuan
 */
public class SwipeWrapper extends FrameLayout {

	public SwipeWrapper(Context context) {
		super(context);
	}

	public SwipeWrapper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SwipeWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	int getSize(int measureSpec, boolean width) {

		int mode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		int size = specSize;

		if (mode == MeasureSpec.EXACTLY) {
			return specSize;
		} else {
			if (width) {
				specSize += getPaddingLeft() + getPaddingRight();
			} else {
				specSize += getPaddingTop() + getPaddingBottom();
			}

			if (mode == MeasureSpec.AT_MOST) {
				return Math.min(size, specSize);
			}
			return size;
		}
	}

}
