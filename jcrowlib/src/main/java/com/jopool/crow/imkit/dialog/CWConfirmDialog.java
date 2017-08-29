package com.jopool.crow.imkit.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jopool.crow.R;


/**
 * 提示Dialog
 * Created by wuhk on 2016/8/2.
 */
public class CWConfirmDialog extends Dialog {
    private Activity activity;
    private String title;
    private String message;
    private View.OnClickListener l;
    private View.OnClickListener n;

    public CWConfirmDialog(Context context , String title , String message , View.OnClickListener l , View.OnClickListener n) {
        super(context);
        this.activity = (Activity) context;
        this.title = title;
        this.message = message;
        this.l = l;
        this.n = n;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 背景透明
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);// 不需要标题

        setContentView(R.layout.cw_dialog_confirm);

        TextView titleTv = (TextView)findViewById(R.id.titleTv);
        TextView messageTv = (TextView)findViewById(R.id.messageTv);
        TextView cancelTv = (TextView)findViewById(R.id.cancelTv);
        TextView sureTv = (TextView)findViewById(R.id.sureTv);

        titleTv.setText(title);
        messageTv.setText(message);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (null != l){
                    l.onClick(v);
                }
            }
        });
        sureTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(null != n){
                    n.onClick(v);
                }
            }
        });
    }

    @Override
    public void show() {
        super.show();
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = (display.getWidth() - dp2px(60)); // 设置宽度
        this.getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
    }

    // dp转ps
    private int dp2px(int dp) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (int) (metrics.density * dp);
    }
}
