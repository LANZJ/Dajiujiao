package com.xuan.bigdog.lib.widgets.carouse.core;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xuan.bigapple.lib.bitmap.BPBitmapLoader;
import com.xuan.bigapple.lib.bitmap.BitmapDisplayConfig;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.viewpage.BUCyclePage;
import com.xuan.bigappleui.lib.viewpage.listeners.OnScrollCompleteListener;
import com.xuan.bigdog.lib.R;
import com.xuan.bigdog.lib.widgets.DGBaseLayout;
import com.xuan.bigdog.lib.widgets.adapter.DGBaseAdapter;
import com.xuan.bigdog.lib.widgets.carouse.DGCarouseConfig;

import java.util.List;

/**
 * 轮播
 * 
 * @author xuan
 */
public class DGCarouselLayout extends DGBaseLayout {
	private ImageView mOcclusionIv;// 当图片在加载中时，用来设置默认图片的
	private BUCyclePage mCyclePage;//轮播控件
	private DGIndicatorLayout mIndicatorLayout;//指示器

	private BitmapDisplayConfig config;//加载配置

	public DGCarouselLayout(Context context) {
		super(context);
	}

	public DGCarouselLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void dgInit() {
		inflate(getContext(), R.layout.dg_widgets_carousel, this);

		mCyclePage = (BUCyclePage) findViewById(R.id.cyclePage);
		mOcclusionIv = (ImageView) findViewById(R.id.occlusionIv);
		mIndicatorLayout = (DGIndicatorLayout)findViewById(R.id.indicatorLayout);

		//初始化默认图片
		DGCarouseConfig.defualtImageBitmap = BitmapFactory.decodeResource(getResources(),DGCarouseConfig.defualtImageResId);
		config = new BitmapDisplayConfig();
		config.setLoadFailedBitmap(DGCarouseConfig.defualtImageBitmap);
		config.setLoadingBitmap(DGCarouseConfig.defualtImageBitmap);

		//初始化默认图片
		mOcclusionIv.setImageBitmap(DGCarouseConfig.defualtImageBitmap);
	}

	// 现实网络加载的图片
	public void loadImageAndShow(final List<DGImageItem> imageItemList) {
		if(Validators.isEmpty(imageItemList)){
			return;
		}

		mCyclePage.removeAllViews();
		mCyclePage.setAdapter(new DGBaseAdapter() {
			@Override
			public View getView(int position, View view, ViewGroup arg2) {
				ImageView imageIv = (ImageView) LayoutInflater.from(
						getContext()).inflate(R.layout.dg_widgets_carouse_image, null);

				DGImageItem imageItem = imageItemList.get(position);
//				BPBitmapLoader.getInstance().display(imageIv,
//						imageItem.getUrl(),config);
				displayImage(imageIv, imageItem.getUrl(), config);

				//点击事件
				if(null != imageItem.getOnClickListener()){
					imageIv.setOnClickListener(imageItem.getOnClickListener());
				}

				return imageIv;
			}

			@Override
			public int getCount() {
				return imageItemList.size();
			}
		});
		mOcclusionIv.setVisibility(View.GONE);

		//指示器
		mIndicatorLayout.initIndicatorLayout(R.drawable.dg_widgets_carouse_shape_indicator_activated,R.drawable.dg_widgets_carouse_indicator_shape_unactivated, imageItemList.size());
		mIndicatorLayout.activatePosition(0);
		mCyclePage.setOnScrollCompleteListener(new OnScrollCompleteListener() {
			@Override
			public void onScrollComplete(int i) {
				mIndicatorLayout.activatePosition(i - 1);
			}
		});
		mCyclePage.start();
	}

	protected void displayImage(ImageView imageView, String url, BitmapDisplayConfig config) {
		if (!Validators.isEmpty(url)) {
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
		}
	}

}
