package com.zjyeshi.dajiujiao.buyer.widgets.addLessFormat;

import android.content.Context;
import android.util.AttributeSet;

import com.zjyeshi.dajiujiao.R;


/**
 * Created by wuhk on 2016/9/18.
 */
public class VFormatWidget extends FormatWidget {
    public VFormatWidget(Context context) {
        super(context);
    }

    public VFormatWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void loadView() {
        inflate(getContext() , R.layout.widget_format_vertical , this);
    }
}
