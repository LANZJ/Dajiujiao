package com.xuan.bigappleui.lib.album.core;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.xuan.bigappleui.lib.album.BUAlbum;
import com.xuan.bigappleui.lib.utils.bitmap.BitmapDisplayConfig;
import com.xuan.bigappleui.lib.utils.bitmap.core.impl.local.LocalBitmapLoader;
import com.xuan.bigappleui.lib.utils.bitmap.listeners.DisplayImageListener;
import com.xuan.bigappleui.lib.utils.ui.ColorUtils;
import com.xuan.bigappleui.lib.view.imageview.BURotationImageView;

import java.io.File;

/**
 * 图片加载器
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-7 上午11:46:00 $
 */
public abstract class ImageLoader {
	private static Bitmap defaultBitmap = Bitmap.createBitmap(
			new int[] { ColorUtils.COLOR_EBEBEB }, 1, 1, Config.ARGB_8888);

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public synchronized static void init(Context context) {
		if (context instanceof Activity) {
			context = ((Activity) context).getApplication();
		}

		LocalBitmapLoader.init(context);
	}

	/**
	 * 显示图片
	 * 
	 * @param context
	 * @param imageView
	 * @param thumbPath
	 * @param sourcePath
	 */
	public static void display(final Context context, ImageView imageView,
			String thumbPath, String sourcePath) {
		if (null == context || null == imageView) {
			return;
		}

		String showUrl = null;
		if (TextUtils.isEmpty(thumbPath)) {
			showUrl = sourcePath;
		} else {
			File file = new File(thumbPath);
			if (file.exists()) {
				showUrl = thumbPath;
			} else {
				showUrl = sourcePath;
			}
		}

		if (TextUtils.isEmpty(showUrl)) {
			return;
		}

		LocalBitmapLoader.getInstance().display(imageView, showUrl,
				getConfig(context, showUrl, sourcePath));
	}

	private static BitmapDisplayConfig getConfig(final Context context,
			final String filePath, final String sourcePath) {
		BitmapDisplayConfig config = new BitmapDisplayConfig();
		config.setShowOriginal(false);
		config.setLoadingBitmap(defaultBitmap);
		config.setLoadFailedBitmap(defaultBitmap);
		config.setBitmapMaxHeight(BUAlbum.getConfig().thumbnailQualityHeight);
		config.setBitmapMaxWidth(BUAlbum.getConfig().thumbnailQualityWidth);
		config.setDisplayImageListener(new DisplayImageListener() {
			@Override
			public void loadFailed(ImageView imageView,
					BitmapDisplayConfig config) {
				imageView.setImageBitmap(config.getLoadFailedBitmap());
			}

			@Override
			public void loadCompleted(ImageView imageView, Bitmap bitmap,
					BitmapDisplayConfig arg2) {
				if (imageView instanceof BURotationImageView) {
					BURotationImageView rotationImageView = (BURotationImageView) imageView;
					rotationImageView.setRotationDegree(ImageUtils
							.getBitmapDegree(context, Uri.parse(sourcePath)));
					rotationImageView.setImageBitmap(bitmap);
				} else {
					imageView.setImageBitmap(bitmap);
				}
			}
		});

		return config;
	}

}
