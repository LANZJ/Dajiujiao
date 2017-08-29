package com.xuan.bigdog.lib.widgets.carouse.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xuan.bigdog.lib.R;

/**
 * 指示器布局
 *
 * Created by xuan on 15/8/5.
 */
public class DGIndicatorLayout extends LinearLayout{
    private int mActivatedResid;//选中资源
    private int mUnactivatedResid;//未选中资源
    private int mIndicatorNum;//指示器数量
    private View[] indicators;

    private int activatedPosition = -1;//当前激活的指示器

    public DGIndicatorLayout(Context context) {
        super(context);
        init();
    }

    public DGIndicatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
    }

    /**
     * 初始化
     *
     * @param activatedResid
     * @param unactivatedResid
     * @param indicatorNum
     */
    public void initIndicatorLayout(int activatedResid, int unactivatedResid, int indicatorNum){
        removeAllViews();

        this.mActivatedResid = activatedResid;
        this.mUnactivatedResid = unactivatedResid;
        this.mIndicatorNum = indicatorNum;

        //循环创建子View
        indicators = new View[indicatorNum];
        for(int i=0; i<indicatorNum; i++){

            indicators[i] = LayoutInflater.from(getContext()).inflate(R.layout.dg_widgets_carouse_indicator_circle_view,null);
            ((ImageView)indicators[i].findViewById(R.id.imageIv)).setImageResource(unactivatedResid);
            addView(indicators[i]);
        }
    }

    /**
     * 激活某个指示器
     *
     * @param position
     */
    public void activatePosition(int position){
        position = Math.min(mIndicatorNum - 1, Math.max(position,0));

        if(-1 != activatedPosition){
            ((ImageView)indicators[activatedPosition].findViewById(R.id.imageIv)).setImageResource(mUnactivatedResid);
        }

        ((ImageView)indicators[position].findViewById(R.id.imageIv)).setImageResource(mActivatedResid);
        this.activatedPosition = position;
    }

    public void setIndicatorNum(int indicatorNum) {
        this.mIndicatorNum = indicatorNum;
    }

}
