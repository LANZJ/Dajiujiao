package com.zjyeshi.dajiujiao.buyer.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.R;

/**
 * Created by wuhk on 2016/6/20.
 */
public class CustomerDialog extends Dialog{
    private boolean isNormal;
    private View.OnClickListener sureListener;//确认
    private View.OnClickListener reportListener;//报告原因
    private String des;

    public CustomerDialog(Context context , boolean isNormal , String des , View.OnClickListener sureListener , View.OnClickListener reportListener) {
        super(context);
        this.isNormal = isNormal;
        this.sureListener = sureListener;
        this.reportListener = reportListener;
        this.des = des;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 背景透明
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);// 不需要标题
        init();
    }

    private void init(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.layout_card_dialog , null);
        setContentView(view);

        ImageView imgIv = (ImageView)view.findViewById(R.id.imgIv);
        final TextView desTv = (TextView)view.findViewById(R.id.desTv);
        TextView timeTv = (TextView)view.findViewById(R.id.timeTv);
        TextView leftTv = (TextView)view.findViewById(R.id.leftTv);
        TextView rightTv = (TextView)view.findViewById(R.id.rightTv);

        long sysTime = System.currentTimeMillis();
        CharSequence sysTimeStr = DateFormat.format("HH:mm", sysTime);
        timeTv.setText(sysTimeStr);
        leftTv.setText("确定");
        desTv.setText(des);

        if (isNormal){
            imgIv.setImageResource(R.drawable.ok);

            rightTv.setText("添加备注");
        }else{
            imgIv.setImageResource(R.drawable.late);
            rightTv.setText("报告原因");
        }

        leftTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (null != sureListener){
                    sureListener.onClick(v);
                }
            }
        });

        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (null != reportListener){
                    reportListener.onClick(v);
                }
            }
        });


        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.7); // 宽度设置为屏幕的0.6
        lp.height = (int) (d.heightPixels * 0.6); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }
}
