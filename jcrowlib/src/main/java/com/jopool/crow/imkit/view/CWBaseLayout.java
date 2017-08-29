package com.jopool.crow.imkit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;


/**
 * 布局文件的基类，把一个布局抽出来方便复用
 * 
 * @author xuan
 */
public abstract class CWBaseLayout extends RelativeLayout {
	public CWBaseLayout(Context context) {
		super(context);
		onViewInit();
	}

	public CWBaseLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		onViewInit();
	}

	protected void onViewInit() {
	}
}
