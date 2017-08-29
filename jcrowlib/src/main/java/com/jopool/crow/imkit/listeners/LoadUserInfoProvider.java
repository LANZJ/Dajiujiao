package com.jopool.crow.imkit.listeners;

import com.jopool.crow.imlib.entity.CWCacheUser;

/**
 * 在本地没有缓存时,使用这个加在器加在用户信息,注意这个方法线程同步请
 * 
 * @author xuan
 */
public interface LoadUserInfoProvider {

	/**
	 * 根据用户id，返回用户
	 * 
	 * @param userId
	 * @return
	 */
	CWCacheUser loadUserById(String userId);
}
