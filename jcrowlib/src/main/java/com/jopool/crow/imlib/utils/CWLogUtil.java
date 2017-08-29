package com.jopool.crow.imlib.utils;

import android.util.Log;

/**
 * 一个简单的日志工具封装，可简单自己定位TAG，TAG的格式为：类名[方法名， 调用行数]
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-9-13 下午12:22:49 $
 */
public abstract class CWLogUtil {
	public static String TAG = "CWLogUtil";

	public static boolean allowD = true;
	public static boolean allowE = true;

	/**
	 * DEBUG
	 *
	 * @param content
	 */
	public static void d(String content) {
		if (!allowD || CWValidator.isEmpty(content)) {
			return;
		}
		String tag = generateTag();
		Log.d(tag, content);
	}

	/**
	 * DEBUG
	 *
	 * @param content
	 * @param tr
	 */
	public static void d(String content, Throwable tr) {
		if (!allowD || CWValidator.isEmpty(content)) {
			return;
		}
		String tag = generateTag();
		Log.d(tag, content, tr);
	}

	/**
	 * ERROR
	 *
	 * @param content
	 */
	public static void e(String content) {
		if (!allowE || CWValidator.isEmpty(content)) {
			return;
		}
		String tag = generateTag();
		Log.e(tag, content);
	}

	/**
	 * ERROR
	 *
	 * @param content
	 * @param tr
	 */
	public static void e(String content, Throwable tr) {
		if (!allowE || CWValidator.isEmpty(content)) {
			return;
		}
		String tag = generateTag();
		Log.e(tag, content, tr);
	}

	/**
	 * ERROR
	 *
	 * @param tr
	 */
	public static void e(Throwable tr) {
		if (!allowE) {
			return;
		}
		String tag = generateTag();
		Log.e(tag, tr.getMessage(), tr);
	}

	private static String generateTag() {
		return TAG;
	}

}
