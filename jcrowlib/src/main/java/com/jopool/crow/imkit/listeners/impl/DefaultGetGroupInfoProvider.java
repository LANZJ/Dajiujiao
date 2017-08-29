package com.jopool.crow.imkit.listeners.impl;

import com.jopool.crow.imkit.listeners.GetGroupInfoProvider;
import com.jopool.crow.imlib.entity.CWGroup;
import com.jopool.crow.imlib.model.CWGroupModel;

/**
 * 群组信息提供者默认实现
 * 
 * @author xuan
 */
public class DefaultGetGroupInfoProvider implements GetGroupInfoProvider {
	@Override
	public CWGroup getGroupById(String groupId) {
		return CWGroupModel.getInstance().getByGroupId(groupId);
	}

}
