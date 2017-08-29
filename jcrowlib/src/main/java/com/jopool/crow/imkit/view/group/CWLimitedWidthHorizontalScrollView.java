package com.jopool.crow.imkit.view.group;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.HorizontalScrollView;

import com.jopool.crow.imlib.utils.CWLogUtil;

/**
 * Created by wuhk on 2016/11/8.
 */
public class CWLimitedWidthHorizontalScrollView extends HorizontalScrollView {
    public CWLimitedWidthHorizontalScrollView(Context context) {
        super(context);
    }

    public CWLimitedWidthHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        widthSize = ((screenWidth - 36) * 4) / 5;

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
