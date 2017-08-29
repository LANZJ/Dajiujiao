package com.zjyeshi.dajiujiao.buyer.circle.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.R;

/**
 * 点赞和评论
 * Created by wuhk on 2015/11/18.
 */
public class RemarkView extends PopupWindow {
    private View mainView;
    private RelativeLayout zanLayout, pinglunLayout;
    private TextView zanTv , pinglunTv;

    public RemarkView(Activity paramActivity, int paramInt1, int paramInt2){
        super(paramActivity);
        //窗口布局
        mainView = LayoutInflater.from(paramActivity).inflate(R.layout.layout_popwindow, null);
        //赞布局
        zanLayout = (RelativeLayout)mainView.findViewById(R.id.zanlayout);
        //复制布局
        pinglunLayout = (RelativeLayout)mainView.findViewById(R.id.pinglunLayout);
        //赞文字
        zanTv = (TextView)mainView.findViewById(R.id.zanTv);
        //评论文字
        pinglunTv = (TextView)mainView.findViewById(R.id.pinglunTv);

        setContentView(mainView);
        //设置宽度
        setWidth(paramInt1);
        //设置高度
        setHeight(paramInt2);
        //设置显示隐藏动画
        setAnimationStyle(R.style.AnimTools);
        //设置背景透明
        setBackgroundDrawable(new ColorDrawable(0));
    }

    //设置赞的文字
    public void setZanTv(String str){
        zanTv.setText(str);
    }

    //赞的点击
    public RemarkView setZanClick(View.OnClickListener l) {
        zanLayout.setOnClickListener(l);
        return this;
    }
    //评论的点击
    public RemarkView setComment(View.OnClickListener l) {
        pinglunLayout.setOnClickListener(l);
        return this;
    }

    //获取状态
    public boolean isPraised(){
        if (zanTv.getText().toString().equals("赞")){
            return  false;
        }else{
            return  true;
        }
    }

}
