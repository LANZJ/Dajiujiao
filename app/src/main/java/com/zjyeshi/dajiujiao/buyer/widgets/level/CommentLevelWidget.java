package com.zjyeshi.dajiujiao.buyer.widgets.level;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.zjyeshi.dajiujiao.buyer.views.other.MyGridView;
import com.zjyeshi.dajiujiao.buyer.widgets.level.callback.LevelCallback;
import com.zjyeshi.dajiujiao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 评价等级控件
 * Created by wuhk on 2016/1/21.
 */
public class CommentLevelWidget extends LinearLayout {
    private MyGridView myGridView;
    private LevelAdapter levelAdapter;
    private List<Integer> dataList = new ArrayList<Integer>();
    public CommentLevelWidget(Context context) {
        super(context);
        init();
    }

    public CommentLevelWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        inflate(getContext() , R.layout.widget_comment_level , this);
        myGridView = (MyGridView)findViewById(R.id.levelGridView);
        for(int i = 0 ; i < 5 ; i++){
            dataList.add(R.drawable.no_comment);
        }

    }

    public void setLevelCallback(LevelCallback levelCallback){
        levelAdapter = new LevelAdapter(getContext() , dataList , levelCallback);
        myGridView.setAdapter(levelAdapter);
    }
}
