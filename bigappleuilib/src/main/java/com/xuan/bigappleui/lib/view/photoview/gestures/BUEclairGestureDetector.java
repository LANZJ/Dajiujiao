package com.xuan.bigappleui.lib.view.photoview.gestures;

import android.content.Context;
import android.view.MotionEvent;

import com.xuan.bigappleui.lib.view.photoview.BUCompat;

public class BUEclairGestureDetector extends BUCupcakeGestureDetector {

	private static final int INVALID_POINTER_ID = -1;
	private int mActivePointerId = INVALID_POINTER_ID;
	private int mActivePointerIndex = 0;

	public BUEclairGestureDetector(Context context) {
		super(context);
	}

	@Override
	float getActiveX(MotionEvent ev) {
		try {
			return ev.getX(mActivePointerIndex);
		} catch (Exception e) {
			return ev.getX();
		}
	}

	@Override
	float getActiveY(MotionEvent ev) {
		try {
			return ev.getY(mActivePointerIndex);
		} catch (Exception e) {
			return ev.getY();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mActivePointerId = ev.getPointerId(0);
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mActivePointerId = INVALID_POINTER_ID;
			break;
		case MotionEvent.ACTION_POINTER_UP:
			// Ignore deprecation, ACTION_POINTER_ID_MASK and
			// ACTION_POINTER_ID_SHIFT has same value and are deprecated
			// You can have either deprecation or lint target api warning
			final int pointerIndex = BUCompat.getPointerIndex(ev.getAction());
			final int pointerId = ev.getPointerId(pointerIndex);
			if (pointerId == mActivePointerId) {
				// This was our active pointer going up. Choose a new
				// active pointer and adjust accordingly.
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				mActivePointerId = ev.getPointerId(newPointerIndex);
				mLastTouchX = ev.getX(newPointerIndex);
				mLastTouchY = ev.getY(newPointerIndex);
			}
			break;
		}

		mActivePointerIndex = ev
				.findPointerIndex(mActivePointerId != INVALID_POINTER_ID ? mActivePointerId
						: 0);
		return super.onTouchEvent(ev);
	}
}