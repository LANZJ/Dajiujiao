package com.jopool.crow.imkit.utils.photoview.app.core;

import android.widget.ImageView;

import com.jopool.crow.imlib.utils.CWValidator;
import com.jopool.crow.imlib.utils.bitmap.BPBitmapLoader;
import com.jopool.crow.imlib.utils.bitmap.BitmapDisplayConfig;

/**
 * 图片加载器
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-7 上午11:46:00 $
 */
public abstract class ImageLoader {

	/**
	 * 显示图片
	 * 
	 * @param imageView
	 * @param url
	 */
	public static void display(ImageView imageView, String url,
			BitmapDisplayConfig config) {
		if (null == imageView || CWValidator.isEmpty(url)) {
			return;
		}

		BPBitmapLoader.getInstance().display(imageView, url);
	}

}
