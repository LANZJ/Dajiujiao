package com.jopool.crow.imkit.listeners;

import com.jopool.crow.imlib.entity.CWUser;

/**
 * 用户可以自己实现
 * 
 * @author xuan
 */
public interface GetUserInfoProvider {

	/**
	 * 根据用户id，返回用户
	 * 
	 * @param userId
	 * @return
	 */
	CWUser getUserById(String userId);
}
