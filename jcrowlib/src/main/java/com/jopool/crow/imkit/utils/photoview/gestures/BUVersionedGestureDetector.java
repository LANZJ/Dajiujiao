package com.jopool.crow.imkit.utils.photoview.gestures;

import android.content.Context;
import android.os.Build;

/**
 * 各个系统版本的手势
 * 
 * @author xuan
 */
public final class BUVersionedGestureDetector {

	/**
	 * 根据系统版本，获取相应的手势操作
	 * 
	 * @param context
	 * @param listener
	 * @return
	 */
	public static BUGestureDetector newInstance(Context context,
			BUOnGestureListener listener) {
		final int sdkVersion = Build.VERSION.SDK_INT;

		BUGestureDetector detector = null;
		if (sdkVersion < Build.VERSION_CODES.ECLAIR) {
			detector = new BUCupcakeGestureDetector(context);
		} else if (sdkVersion < Build.VERSION_CODES.FROYO) {
			detector = new BUEclairGestureDetector(context);
		} else {
			detector = new BUFroyoGestureDetector(context);
		}

		detector.setOnGestureListener(listener);
		return detector;
	}

}