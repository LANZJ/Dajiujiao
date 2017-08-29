package com.zjyeshi.dajiujiao.buyer.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.display.DisplayUtils;
import com.zjyeshi.dajiujiao.R;

/**
 * 消息未读数View
 * Created by wuhk on 2016/7/25.
 */
public class UnReadNumView extends RelativeLayout {
    private RelativeLayout numBgLayout;
    private TextView numTv;

    public UnReadNumView(Context context) {
        super(context);
        init();
    }

    public UnReadNumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_unread_number, this);
        numBgLayout = (RelativeLayout) findViewById(R.id.numBgLayout);
        numTv = (TextView) findViewById(R.id.numTv);
        setVisibility(GONE);
    }

    public void setNum(int num) {
        numTv.setText(String.valueOf(num));
        if (num <= 0) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            if (num > 0 && num < 10) {
                LayoutParams lp = new LayoutParams((int) DisplayUtils.getPxByDp(((Activity) getContext()), 20), (int) DisplayUtils.getPxByDp(((Activity) getContext()), 20));
                numBgLayout.setLayoutParams(lp);
            } else if (num >= 10 && num <= 99) {
                LayoutParams lp = new LayoutParams((int) DisplayUtils.getPxByDp(((Activity) getContext()), 25), (int) DisplayUtils.getPxByDp(((Activity) getContext()), 20));
                numBgLayout.setLayoutParams(lp);
            } else {
                LayoutParams lp = new LayoutParams((int) DisplayUtils.getPxByDp(((Activity) getContext()), 30), (int) DisplayUtils.getPxByDp(((Activity) getContext()), 20));
                numBgLayout.setLayoutParams(lp);
                numTv.setText("99+");
            }
        }
    }
}
