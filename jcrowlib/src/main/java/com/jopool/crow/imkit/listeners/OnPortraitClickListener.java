package com.jopool.crow.imkit.listeners;

import android.content.Context;
import android.view.View;

/**
 * 点击头像事件
 * 
 * @author xuan
 */
public interface OnPortraitClickListener {
	/**
	 * 点击头像
	 * 
	 * @param context
	 * @param view
	 * @param userId
	 */
	void onPortaitClick(Context context, View view, String userId);
}
