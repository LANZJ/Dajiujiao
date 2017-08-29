package com.jopool.crow.imkit.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.jopool.crow.R;
import com.jopool.crow.imkit.utils.photoview.BUPhotoView;
import com.jopool.crow.imkit.utils.photoview.BUPhotoViewAttacher;
import com.jopool.crow.imlib.utils.bitmap.BPBitmapLoader;
import com.jopool.crow.imlib.utils.bitmap.BitmapDisplayConfig;
import com.jopool.crow.imlib.utils.bitmap.listeners.DisplayImageListener;

/**
 * 聊天查看大图
 * 
 * @author xuan
 */
public class CWShowImageActivity extends CWBaseActivity {
	public static final String PARAM_SMALL_URL = "param.small.url";
	public static final String PARAM_BIG_URL = "param.big.url";

	private BUPhotoView photoView;
	private ProgressBar progressBar;

	private String smallUrl;
	private String bigUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cw_chat_layout_showimage);
		smallUrl = getIntent().getStringExtra(PARAM_SMALL_URL);
		bigUrl = getIntent().getStringExtra(PARAM_BIG_URL);
		loadView();
		initWidgets();
	}

	private void loadView() {
		photoView = (BUPhotoView) F(R.id.photoView);
		progressBar = (ProgressBar) F(R.id.progressBar);
	}

	private void initWidgets() {

		Bitmap bitmap = BPBitmapLoader.getInstance().getBitmapFromCache(
				smallUrl, null);

		progressBar.setVisibility(View.VISIBLE);
		BitmapDisplayConfig displayConfig = new BitmapDisplayConfig();
		if (null != bitmap) {
			displayConfig.setLoadingBitmap(bitmap);
			displayConfig.setLoadFailedBitmap(bitmap);
		}

		displayConfig.setDisplayImageListener(new DisplayImageListener() {
			@Override
			public void loadFailed(ImageView arg0, BitmapDisplayConfig arg1) {
				progressBar.setVisibility(View.GONE);
			}

			@Override
			public void loadCompleted(ImageView imageView, Bitmap bitmap,
					BitmapDisplayConfig arg2) {
				imageView.setImageBitmap(bitmap);
				progressBar.setVisibility(View.GONE);
			}
		});
		BPBitmapLoader.getInstance().display(photoView, bigUrl, displayConfig);

		// 点击关闭
		photoView.setOnPhotoTapListener(new BUPhotoViewAttacher.OnPhotoTapListener() {
			@Override
			public void onPhotoTap(View view, float arg1, float arg2) {
				finish();
			}
		});
	}

}
