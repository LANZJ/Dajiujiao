package com.zjyeshi.dajiujiao.buyer.utils;

import com.xuan.bigapple.lib.utils.ContextUtils;

/**
 * Context相关工具类
 * 
 * @author xuan
 */
public class ContextUtil {
	/**
	 * 判断在前台还是后台
	 *
	 * @return
	 */
	public static boolean isBackground() {
		return ContextUtils.isBackground();
	}

}
