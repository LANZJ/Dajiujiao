package com.xuan.bigappleui.lib.utils.bitmap.listeners.impl;


import com.xuan.bigappleui.lib.utils.bitmap.listeners.MakeCacheKeyListener;

/**
 * 默认产生cacheKey
 * 
 * @author xuan
 */
public class DefaultMakeCacheKeyListener implements MakeCacheKeyListener {
	@Override
	public String makeCacheKey(String url) {
		return url;
	}

}
