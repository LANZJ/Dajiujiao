package com.jopool.crow.imkit.utils.photoview.scrollerproxy;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

public abstract class BUScrollerProxy {

	public static BUScrollerProxy getScroller(Context context) {
		if (VERSION.SDK_INT < VERSION_CODES.GINGERBREAD) {
			return new BUPreGingerScroller(context);
		} else if (VERSION.SDK_INT < 14) {// 14=VERSION_CODES.ICE_CREAM_SANDWICH
			return new BUGingerScroller(context);
		} else {
			return new BUIcsScroller(context);
		}
	}

	public abstract boolean computeScrollOffset();

	public abstract void fling(int startX, int startY, int velocityX,
			int velocityY, int minX, int maxX, int minY, int maxY, int overX,
			int overY);

	public abstract void forceFinished(boolean finished);

	public abstract boolean isFinished();

	public abstract int getCurrX();

	public abstract int getCurrY();

}
