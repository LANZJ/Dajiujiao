package com.xuan.bigdog.lib.update.core;

import java.io.InputStream;

import com.xuan.bigdog.lib.update.entity.UpdateInfo;

/**
 * 处理返回信息接口
 * 
 * @author xuan
 */
public interface PasreUpdateInfoHandler {
	/**
	 * 返回流处理成对象
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public UpdateInfo toUpdateInfo(InputStream is) throws Exception;
}
