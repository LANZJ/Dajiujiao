package com.jopool.crow.imlib.utils.bitmap.listeners;

/**
 * 产生缓存key监听
 * 
 * @author xuan
 */
public interface MakeCacheKeyListener {
	/**
	 * 产生cacheKey
	 * 
	 * @param url
	 * @return
	 */
	public String makeCacheKey(String url);
}
