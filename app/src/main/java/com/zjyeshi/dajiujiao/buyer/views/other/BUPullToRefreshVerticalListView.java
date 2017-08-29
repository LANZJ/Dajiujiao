package com.zjyeshi.dajiujiao.buyer.views.other;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListView;

/**
 * 解决banner冲突问题
 *
 * Created by xuan on 15/8/10.
 */
public class BUPullToRefreshVerticalListView extends BUPullToRefreshListView {
    private float mXdistance;
    private float mYdistance;

    private float mLastX;
    private float mLastY;

    public BUPullToRefreshVerticalListView(Context context) {
        super(context);
    }

    public BUPullToRefreshVerticalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mXdistance = mYdistance = 0f;
                mLastX = ev.getX();
                mLastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                mXdistance += Math.abs(curX - mLastX);
                mYdistance += Math.abs(curY - mLastY);
                mLastX = curX;
                mLastY = curY;
                if (mXdistance > mYdistance) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }

}
