package com.zjyeshi.dajiujiao.buyer.circle.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


import com.zjyeshi.dajiujiao.buyer.circle.task.data.Evaluate;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.Praise;

import java.util.List;

/**
 * 评论列表
 *
 * Created by xuan on 15/11/6.
 */
public class CommentListView extends ListView {
    public CommentListView(Context context) {
        super(context);
    }

    public CommentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void loadImage(final List<Praise> praiseList, List<Evaluate> evaluateList) {
    }

}
