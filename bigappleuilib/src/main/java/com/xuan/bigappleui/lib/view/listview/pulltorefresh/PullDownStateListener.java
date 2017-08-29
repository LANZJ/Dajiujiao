package com.xuan.bigappleui.lib.view.listview.pulltorefresh;

/**
 * 下拉刷新各种动作监听
 * 
 * @author xuan
 * 
 */
public interface PullDownStateListener {
	/**
	 * 释放刷新状态
	 * 
	 * @param headerView
	 */
	void releaseToRefresh(BUHeaderView headerView);

	/**
	 * 下拉刷新状态
	 * 
	 * @param headerView
	 */
	void pullToRefresh(BUHeaderView headerView);

	/**
	 * 刷新中状态
	 * 
	 * @param headerView
	 */
	void refreshing(BUHeaderView headerView);

	/**
	 * 刷新完成状态
	 * 
	 * @param headerView
	 */
	void done(BUHeaderView headerView);
}
