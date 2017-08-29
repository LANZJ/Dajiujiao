package com.jopool.crow.imkit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 自定义GridView
 *
 * Created by wuhk on 2015/9/30.
 */
public class CWGroupUserGridView extends GridView {

    public CWGroupUserGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CWGroupUserGridView(Context context) {
        super(context);
    }

    public CWGroupUserGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
