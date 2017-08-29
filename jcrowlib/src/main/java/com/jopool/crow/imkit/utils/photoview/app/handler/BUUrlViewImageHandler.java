package com.jopool.crow.imkit.utils.photoview.app.handler;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.jopool.crow.imkit.utils.photoview.BUPhotoViewAttacher;
import com.jopool.crow.imkit.utils.photoview.app.viewholder.WraperFragmentView;
import com.jopool.crow.imlib.utils.bitmap.BitmapDisplayConfig;
import com.jopool.crow.imlib.utils.bitmap.listeners.DisplayImageListener;


/**
 * 加载网络地址或者本地SD卡的地址
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-6 下午3:45:12 $
 */
public class BUUrlViewImageHandler extends BUViewImageBaseHandler {

	@Override
	public void onHandler(String url,
			final WraperFragmentView wraperFragmentView,
			final Activity activity, Object[] datas) {
		try {
			wraperFragmentView.photoView
					.setOnPhotoTapListener(new BUPhotoViewAttacher.OnPhotoTapListener() {
						@Override
						public void onPhotoTap(View view, float x, float y) {
							activity.finish();
						}
					});

			// 显示大图
			wraperFragmentView.progressBar.setVisibility(View.VISIBLE);
			BitmapDisplayConfig config = new BitmapDisplayConfig();
			config.setDisplayImageListener(new DisplayImageListener() {
				@Override
				public void loadFailed(ImageView arg0, BitmapDisplayConfig arg1) {
				}

				@Override
				public void loadCompleted(ImageView imageView, Bitmap bitmap,
						BitmapDisplayConfig arg2) {
					imageView.setImageBitmap(bitmap);
					wraperFragmentView.progressBar.setVisibility(View.GONE);
				}
			});

			com.jopool.crow.imkit.utils.photoview.app.core.ImageLoader.display(wraperFragmentView.photoView, url, config);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}

	@Override
	public String getLoadType() {
		return BUViewImageBaseHandler.LOADTYPE_BY_URL;
	}

}
