package com.jopool.crow.imkit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by wuhk on 2016/6/21.
 */
public class CWSquareImageView extends ImageView {
    public CWSquareImageView(Context context) {
        super(context);
    }

    public CWSquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
