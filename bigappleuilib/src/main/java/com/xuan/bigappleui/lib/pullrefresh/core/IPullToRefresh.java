package com.xuan.bigappleui.lib.pullrefresh.core;

import android.view.View;

import com.xuan.bigappleui.lib.pullrefresh.core.PullToRefreshBase.OnRefreshListener;

/**
 * 定义了拉动刷新的接口，一些主要操作方法
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-13 下午4:07:20 $
 */
public interface IPullToRefresh<T extends View> {

	/**
	 * 设置当前下拉刷新是否可用
	 * 
	 * @param pullDownRefreshEnabled
	 *            true可用，false不可用
	 */
	void setPullDownRefreshEnabled(boolean pullDownRefreshEnabled);

	/**
	 * 设置当前上拉加载更多是否可用
	 * 
	 * @param pullUpLoadEnabled
	 *            true可用，false不可用
	 */
	void setPullUpRefreshEnabled(boolean pullUpLoadEnabled);

	/**
	 * 滑动到底部是否自动加载更多数据
	 * 
	 * @param scrollUpRefreshEnabled
	 *            如果这个值为true的话，那么上拉加载更多的功能将会禁用
	 */
	void setScrollUpRefreshEnabled(boolean scrollUpRefreshEnabled);

	/**
	 * 判断当前下拉刷新是否可用
	 * 
	 * @return true如果可用，false不可用
	 */
	boolean isPullDownRefreshEnabled();

	/**
	 * 判断上拉加载是否可用
	 * 
	 * @return true可用，false不可用
	 */
	boolean isPullUpRefreshEnabled();

	/**
	 * 滑动到底部加载是否可用
	 * 
	 * @return true可用，false不可用
	 */
	boolean isScrollUpRefreshEnabled();

	/**
	 * 结束下拉刷新
	 */
	void onPullDownRefreshComplete();

	/**
	 * 结束上拉加载更多
	 */
	void onPullUpRefreshComplete();

	/**
	 * 设置刷新的监听器
	 * 
	 * @param refreshListener
	 *            监听器对象
	 */
	void setOnRefreshListener(OnRefreshListener<T> refreshListener);

	/**
	 * 设置最后更新的时间文本
	 * 
	 * @param label
	 *            时间文本
	 */
	void setLastUpdatedLabel(CharSequence label);

	/**
	 * 得到可刷新的View对象
	 *
	 * @return
	 */
	T getRefreshableView();

	/**
	 * 得到Header布局对象
	 * 
	 * @return Header布局对象
	 */
	AbstractLoadingLayout getHeaderLoadingLayout();

	/**
	 * 得到Footer布局对象
	 * 
	 * @return Footer布局对象
	 */
	AbstractLoadingLayout getFooterLoadingLayout();

}
