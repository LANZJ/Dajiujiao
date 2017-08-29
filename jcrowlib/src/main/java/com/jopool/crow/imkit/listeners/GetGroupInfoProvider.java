package com.jopool.crow.imkit.listeners;

import com.jopool.crow.imlib.entity.CWGroup;

/**
 * 群组信息提供者
 * 
 * @author xuan
 */
public interface GetGroupInfoProvider {
	/**
	 * 群组信息
	 * 
	 * @param groupId
	 * @return
	 */
	CWGroup getGroupById(String groupId);

}
