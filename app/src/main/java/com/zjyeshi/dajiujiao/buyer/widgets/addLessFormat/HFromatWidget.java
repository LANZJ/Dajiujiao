package com.zjyeshi.dajiujiao.buyer.widgets.addLessFormat;

import android.content.Context;
import android.util.AttributeSet;

import com.zjyeshi.dajiujiao.R;


/**
 * Created by wuhk on 2016/9/18.
 */
public class HFromatWidget extends FormatWidget {
    public HFromatWidget(Context context) {
        super(context);
    }

    public HFromatWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void loadView() {
        inflate(getContext() , R.layout.widget_format , this);
    }
}
