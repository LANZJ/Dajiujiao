package com.xuan.bigappleui.lib.view.adapter.swipe;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.xuan.bigappleui.lib.view.BUSwipeView;
import com.xuan.bigappleui.lib.view.adapter.BUBaseAdapter;

/**
 * 支持侧滑的Adapter
 * 
 * @author xuan
 */
public abstract class SwipeBaseAdapter extends BUBaseAdapter {
	private Context context;

	public SwipeBaseAdapter() {

	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		BUSwipeView swipeView = new BUSwipeView(context);

		View behindView = getBehindView(position, swipeView);
		View aboveView = getAboveView(position, swipeView);
		int offset = getOffset(position, swipeView);

		return null;
	}

	/**
	 * 需要子类提供，侧滑过后的界面
	 * 
	 * @return
	 */
	protected abstract View getBehindView(int position, View swipeView);

	/**
	 * 需要子类提供显示界面
	 * 
	 * @return
	 */
	protected abstract View getAboveView(int position, View swipeView);

	/**
	 * 需要子类实现，设置便宜量
	 * 
	 * @param position
	 * @param swipeView
	 * @return
	 */
	protected abstract int getOffset(int position, View swipeView);

}
