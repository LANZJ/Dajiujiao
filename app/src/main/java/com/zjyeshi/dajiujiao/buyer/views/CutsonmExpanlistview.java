package com.zjyeshi.dajiujiao.buyer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by lan on 2017/8/24.
 */
public class CutsonmExpanlistview extends ExpandableListView {
    public CutsonmExpanlistview(Context context) {
        super(context);
    }
    public CutsonmExpanlistview(Context context,  AttributeSet attrs){
       super(context,attrs);
        // TODO Auto-generated constructor stub  
       }

public CutsonmExpanlistview(Context context, AttributeSet attrs,
int defStyle) {
    super(context, attrs,defStyle);
        // TODO Auto-generated constructor stub  
        }
     @Override
          protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      int expandSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,
        MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
        }
}
