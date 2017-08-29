package com.jopool.crow.imkit.utils.photoview.scrollerproxy;

import android.content.Context;
import android.widget.Scroller;

public class BUPreGingerScroller extends BUScrollerProxy {

	private final Scroller mScroller;

	public BUPreGingerScroller(Context context) {
		mScroller = new Scroller(context);
	}

	@Override
	public boolean computeScrollOffset() {
		return mScroller.computeScrollOffset();
	}

	@Override
	public void fling(int startX, int startY, int velocityX, int velocityY,
			int minX, int maxX, int minY, int maxY, int overX, int overY) {
		mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY,
				maxY);
	}

	@Override
	public void forceFinished(boolean finished) {
		mScroller.forceFinished(finished);
	}

	@Override
	public boolean isFinished() {
		return mScroller.isFinished();
	}

	@Override
	public int getCurrX() {
		return mScroller.getCurrX();
	}

	@Override
	public int getCurrY() {
		return mScroller.getCurrY();
	}
}