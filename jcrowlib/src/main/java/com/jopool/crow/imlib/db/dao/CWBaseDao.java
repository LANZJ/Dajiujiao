package com.jopool.crow.imlib.db.dao;

import com.jopool.crow.imlib.entity.CWUser;

/**
 * 所有Dao的基类
 * 
 * @author xuan
 */
public class CWBaseDao extends com.jopool.crow.imlib.db.core.CWBaseDao {
	/**
	 * 获取本人userId
	 * 
	 * @return
	 */
	protected String getOwnerUserId() {
		return CWUser.getConnectUserId();
	}

}
