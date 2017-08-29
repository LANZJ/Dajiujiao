package com.xuan.bigdog.lib.widgets.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuan.bigapple.lib.Bigapple;
import com.xuan.bigapple.lib.bitmap.BPBitmapLoader;
import com.xuan.bigapple.lib.bitmap.BitmapDisplayConfig;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.R;

/**
 * 所有适配器的基类
 *
 * @author xuan
 */
public class DGBaseAdapter extends BaseAdapter {

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        return null;
    }

    /**
     * 初始化基础数据
     *
     * @param view
     * @param baseItem
     */
    public void initBaseData(final View view, final DGBaseItem baseItem) {
        // 点击事件
        if (null != baseItem.itemOnclickListener) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    baseItem.itemOnclickListener.itemOnclick(view, baseItem);
                }
            });
        }

        // 长按事件
        if (null != baseItem.itemOnLongclickListener) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    baseItem.itemOnLongclickListener.itemLongOnclick(view,
                            baseItem);
                    return true;
                }
            });
        }

        // 自定义绑定数据
        if (null != baseItem.bindDataListener) {
            baseItem.bindDataListener.bindData(view, baseItem);
        }
    }

    /**
     * 设置图片，图片是空的话就隐藏图片控件
     *
     * @param imageView
     * @param url
     */
    public void initImageView(ImageView imageView, String url) {
        if (!Validators.isEmpty(url)) {
            imageView.setVisibility(View.VISIBLE);
            if (Validators.isNumber(url)) {
                // 资源id
                imageView.setImageResource(Integer.valueOf(url));
            } else if (url.startsWith("http")) {
                // 加载网络
                BPBitmapLoader.getInstance().display(imageView, url);
            } else {
                // 加载本地
                BPBitmapLoader.getInstance().display(imageView, url);
            }
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    /**
     * 图片设置
     *
     * @param imageView
     * @param url
     * @param defaultBitmap
     */
    public void initImageView(ImageView imageView, String url, Bitmap defaultBitmap) {
        if (!Validators.isEmpty(url)) {
            imageView.setVisibility(View.VISIBLE);
            BitmapDisplayConfig config = new BitmapDisplayConfig();
            config.setLoadFailedBitmap(defaultBitmap);
            config.setLoadingBitmap(defaultBitmap);
            config.setBitmapMaxHeight(200);
            config.setBitmapMaxWidth(200);

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

    /**
     * 可以自己设置默认图片
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
            config.setBitmapMaxHeight(200);
            config.setBitmapMaxWidth(200);

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

    /**
     * 设置图片，图片是空的话就设置默认图片
     *
     * @param imageView
     * @param url
     */
    public void initImageViewDefault(ImageView imageView, String url) {
        if (Validators.isEmpty(url)) {
            url = String.valueOf(R.drawable.dg_default_image);
        }
        initImageView(imageView, url);
    }

    /**
     * 设置文本
     *
     * @param textView
     * @param text
     */
    public void initTextView(TextView textView, String text) {
        if (!Validators.isEmpty(text)) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    /**
     * item对象基类
     *
     * @author xuan
     */
    public static class DGBaseItem {
        public Object extData;
        public ItemOnclickListener itemOnclickListener;
        public ItemOnLongclickListener itemOnLongclickListener;
        public BindDataListener bindDataListener;

        public static interface BindDataListener {
            void bindData(View view, DGBaseItem baseItem);
        }

        public static interface ItemOnclickListener {
            void itemOnclick(View view, DGBaseItem baseItem);
        }

        public static interface ItemOnLongclickListener {
            void itemLongOnclick(View view, DGBaseItem baseItem);
        }
    }

}
