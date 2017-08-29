package com.zjyeshi.dajiujiao.buyer.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuan.bigapple.lib.bitmap.BPBitmapLoader;
import com.xuan.bigapple.lib.bitmap.BitmapDisplayConfig;
import com.zjyeshi.dajiujiao.R;

/**
 * Created by wuhk on 2016/6/27.
 */
public class GoodDetailDialog extends Dialog {
    private String pic;
    private String des;
    private TextView desTv;
    private View.OnClickListener listener;

    public TextView getDesTv() {
        return desTv;
    }

    public GoodDetailDialog(Context context , String pic , String des , View.OnClickListener listener) {
        super(context);
        this.pic = pic;
        this.des = des;
        this.listener = listener;
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
        View view = inflater.inflate(R.layout.layout_good_des_dialog , null);
        setContentView(view);

        ImageView imageView = (ImageView)view.findViewById(R.id.photoIv);
        desTv = (TextView)view.findViewById(R.id.desTv);
        TextView lookCommentTv = (TextView)view.findViewById(R.id.lookCommentTv);

        lookCommentTv.setOnClickListener(listener);

        BitmapDisplayConfig config = new BitmapDisplayConfig();
        config.setLoadFailedBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.default_img));
        config.setLoadingBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.default_img));
        BPBitmapLoader.getInstance().display(imageView , pic , config);
        desTv.setText(des);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
    }
}
