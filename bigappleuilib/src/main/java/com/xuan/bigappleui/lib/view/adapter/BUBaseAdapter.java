package com.xuan.bigappleui.lib.view.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuan.bigappleui.lib.utils.BUBitmapUtil;
import com.xuan.bigappleui.lib.utils.BUValidator;
import com.xuan.bigappleui.lib.utils.bitmap.BPBitmapLoader;
import com.xuan.bigappleui.lib.utils.bitmap.BitmapDisplayConfig;
import com.xuan.bigappleui.lib.utils.bitmap.listeners.DisplayImageListener;


/**
 * 适配器基类
 * 
 * @author xuan
 */
public abstract class BUBaseAdapter extends BaseAdapter {

	@Override
	public Object getItem(int item) {
		return item;
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	/**
	 * 设置图片，图片是空的话就隐藏图片控件
	 * 
	 * @param imageView
	 * @param url
	 */
	public void initImageView(ImageView imageView, String url) {
		if (!BUValidator.isEmpty(url)) {
			imageView.setVisibility(View.VISIBLE);
			BPBitmapLoader.getInstance().display(imageView, url);
		} else {
			imageView.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 设置圆形图片，图片是空的话就隐藏图片控件
	 * 
	 * @param imageView
	 * @param url
	 */
	public void initImageViewCircle(ImageView imageView, String url) {
		if (!BUValidator.isEmpty(url)) {
			imageView.setVisibility(View.VISIBLE);
			if (BUValidator.isNumber(url)) {
				// 资源id
				imageView.setImageResource(Integer.valueOf(url));
			} else {
				// 加载网络
				BitmapDisplayConfig config = new BitmapDisplayConfig();
				config.setDisplayImageListener(new DisplayImageListener() {
					@Override
					public void loadCompleted(ImageView imageView,
							Bitmap bitmap,
							BitmapDisplayConfig bitmapDisplayConfig) {
						if (null != bitmap) {
							imageView.setImageBitmap(BUBitmapUtil
									.getRoundedCornerBitmap(bitmap));
						} else {
							imageView.setImageBitmap(bitmapDisplayConfig
									.getLoadFailedBitmap());
						}
					}

					@Override
					public void loadFailed(ImageView imageView,
							BitmapDisplayConfig bitmapDisplayConfig) {
						imageView.setImageBitmap(bitmapDisplayConfig
								.getLoadFailedBitmap());
					}
				});

				BPBitmapLoader.getInstance().display(imageView, url, config);
			}
		} else {
			imageView.setVisibility(View.INVISIBLE);
		}
	}

	public void initImageViewByDefaultUrl(ImageView imageView, String url) {
		initImageView(imageView, url);
	}

	public void initImageViewCircleByDefaultUrl(ImageView imageView, String url) {
		initImageViewCircle(imageView, url);
	}

	/**
	 * 设置文本
	 * 
	 * @param textView
	 * @param text
	 */
	public void initTextView(TextView textView, String text) {
		textView.setVisibility(View.VISIBLE);
		if (!BUValidator.isEmpty(text)) {
			textView.setText(text);
		} else {
			textView.setText("");
		}
	}

}
