package com.jopool.crow.imkit.utils.photoview.app.handler;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.jopool.crow.imkit.utils.photoview.BUPhotoViewAttacher;
import com.jopool.crow.imkit.utils.photoview.app.viewholder.WraperFragmentView;


/**
 * 根据resid显示系统图片
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-5 下午7:10:15 $
 */
public class BUResidViewImageHandler extends BUViewImageBaseHandler {

	@Override
	public void onHandler(String url, WraperFragmentView wraperFragmentView,
			final Activity activity, Object[] datas) {
		try {
			wraperFragmentView.photoView.setImageResource(Integer.valueOf(url));
			wraperFragmentView.photoView
					.setOnPhotoTapListener(new BUPhotoViewAttacher.OnPhotoTapListener() {
						@Override
						public void onPhotoTap(View view, float x, float y) {
							activity.finish();
						}
					});
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}

	@Override
	public String getLoadType() {
		return BUViewImageBaseHandler.LOADTYPE_BY_RESIID;
	}

}
