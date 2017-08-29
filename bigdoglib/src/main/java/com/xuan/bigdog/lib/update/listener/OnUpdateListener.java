package com.xuan.bigdog.lib.update.listener;

import com.xuan.bigdog.lib.update.entity.UpdateInfo;

/**
 * 更新回调
 * 
 * @author xuan
 */
public interface OnUpdateListener {
	/**
	 * 检查是否需要更新之前调用
	 */
	public void onStartCheck();

	/**
	 * 检查结束之后调用
	 *
	 * @param info
	 */
	public void onFinishCheck(UpdateInfo info);

	/**
	 * 开始下载前调用
	 */
	public void onStartDownload();

	/**
	 * 下载过程中调用
	 *
	 * @param progress
	 */
	public void onDownloading(int progress);

	/**
	 * 下载结束调用
	 */
	public void onFinshDownload();

}
