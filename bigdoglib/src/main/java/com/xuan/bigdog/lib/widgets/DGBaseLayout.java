package com.xuan.bigdog.lib.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 布局文件的基类，把一个布局抽出来方便复用
 * 
 * @author xuan
 */
public abstract class DGBaseLayout extends RelativeLayout {
	public DGBaseLayout(Context context) {
		super(context);
		dgInit();
	}

	public DGBaseLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		dgInit();
	}

	protected void dgInit() {
	}

}
