package com.jopool.crow.imkit.utils.photoview.scrollerproxy;

import android.content.Context;

//@TargetApi(14)
public class BUIcsScroller extends BUGingerScroller {

	public BUIcsScroller(Context context) {
		super(context);
	}

	@Override
	public boolean computeScrollOffset() {
		return mScroller.computeScrollOffset();
	}

}
