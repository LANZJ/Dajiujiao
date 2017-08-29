package com.xuan.bigappleui.lib.view.gridview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 定义一个超级高的GridView，使其在ScrollView等滚动的控件中能显示所有item
 * 
 * @author xuan
 */
public class BUHighHeightGridView extends GridView {
	public BUHighHeightGridView(Context context) {
		super(context);
	}

	public BUHighHeightGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BUHighHeightGridView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
