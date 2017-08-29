package com.jopool.crow.imlib.utils.bitmap.listeners.impl;


import com.jopool.crow.imlib.utils.bitmap.listeners.MakeCacheKeyListener;

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
