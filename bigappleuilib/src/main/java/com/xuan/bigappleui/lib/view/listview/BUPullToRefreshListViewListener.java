package com.xuan.bigappleui.lib.view.listview;

/**
 * 刷新监听事件
 * 
 * @author xuan
 * 
 */
public interface BUPullToRefreshListViewListener {
	/**
	 * 下拉刷新
	 */
	void onPullDownRefresh();

	/**
	 * 上拉加载更多
	 */
	void onScrollUpRefresh();
}
