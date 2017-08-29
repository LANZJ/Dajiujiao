package com.zjyeshi.dajiujiao.buyer.circle.itemview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuan.bigapple.lib.Bigapple;
import com.xuan.bigapple.lib.bitmap.BitmapDisplayConfig;
import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.utils.BitmapDisplayConfigFactory;
import com.xuan.bigapple.lib.bitmap.BPBitmapLoader;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.widgets.DGBaseLayout;

/**
 * 所有ItemView的基类
 *
 * Created by xuan on 15/10/18.
 */
public class BaseView extends DGBaseLayout{
    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void initTextView(TextView textView, String text){
        if(Validators.isEmpty(text)){
            textView.setText("");
        }else{
            textView.setText(text);
        }
    }

    protected void initImageView(ImageView imageView, String url){
        if (!Validators.isEmpty(url)) {
            if (Validators.isNumber(url)) {
                // 资源id
                //imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), Integer.valueOf(url)));\
                imageView.setImageResource(Integer.valueOf(url));
            } else if (url.startsWith("http")) {
                // 加载网络
                BPBitmapLoader.getInstance().display(imageView, url);
            } else {
                // 加载本地
                BPBitmapLoader.getInstance().display(imageView, url);
            }
        }
    }

    /**
     * 可以自己设置默认图片 , 做列表
     *
     * @param imageView
     * @param url
     * @param defaultResId
     */
    public void initImageView(ImageView imageView, String url, int defaultResId) {
        Bitmap defaultBitmap = BitmapFactory.decodeResource(Bigapple.getApplicationContext().getResources(), defaultResId);

        if (!Validators.isEmpty(url)) {
            imageView.setVisibility(View.VISIBLE);
            BitmapDisplayConfig config = new BitmapDisplayConfig();
            config.setLoadFailedBitmap(defaultBitmap);
            config.setLoadingBitmap(defaultBitmap);
            config.setBitmapMaxWidth(200);
            config.setBitmapMaxHeight(200);

            if (Validators.isNumber(url)) {
                // 资源id
                imageView.setImageResource(Integer.valueOf(url));
            } else if (url.startsWith("http")) {
                // 加载网络
                BPBitmapLoader.getInstance().display(imageView, url, config);
            } else {
                // 加载本地
                BPBitmapLoader.getInstance().display(imageView, url, config);
            }
        } else {
            imageView.setImageBitmap(defaultBitmap);
        }
    }

    //图片配置
    public void setSingleImage(ImageView imageView, String url) {
//        if (!Validators.isEmpty(url)) {
//            BitmapDisplayConfig config = BitmapDisplayConfigFactory.getInstance().getCircleImageConfig();
//            config.setBitmapMaxHeight((int) App.instance.getResources().getDimension(R.dimen.circle_single_image_max_size));
//            config.setBitmapMaxWidth((int)App.instance.getResources().getDimension(R.dimen.circle_single_image_max_size));
//            BPBitmapLoader.getInstance().display(imageView, url, BitmapDisplayConfigFactory.getInstance().getCircleImageConfig());
//        } else {
//            imageView.setImageBitmap(App.defaultBitmap);
//        }

        GlideImageUtil.glidImage(imageView , url , R.drawable.default_img);
    }

    public void setMultiImage(ImageView imageView, String url) {
        if (!Validators.isEmpty(url)) {
            BitmapDisplayConfig config = BitmapDisplayConfigFactory.getInstance().getCircleImageConfig();
            BPBitmapLoader.getInstance().display(imageView, url, BitmapDisplayConfigFactory.getInstance().getCircleImageConfig());
        } else {
            imageView.setImageBitmap(App.defaultBitmap);
        }
    }
}
