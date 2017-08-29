package com.xuan.bigappleui.lib.view.webview.progress;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xuan.bigappleui.lib.utils.BUCompat;

/**
 * 供WebView使用的进度条
 * 
 * @author xuan
 * 
 */
public class BUWebViewProgressBar extends FrameLayout implements BUProgress{
	private static final String DEFAULT_PROGRESS_COLOR = "#53b53e";

	/** 控件的宽 */
	private int mWidth;
	/** 进度条 */
	private View progressView;
	/** 进度值 */
	private int percent;

	public BUWebViewProgressBar(Context context) {
		super(context);
		init();
	}

	public BUWebViewProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BUWebViewProgressBar(Context context, AttributeSet attrs,
								int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mWidth = w;
	}

	// Init
	private void init() {
		progressView = new View(getContext());
		LayoutParams progressLp = new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		progressView.setLayoutParams(progressLp);

		// 默认颜色
		BUCompat.setViewBackgroundDrawable(progressView, new ColorDrawable(
				Color.parseColor(DEFAULT_PROGRESS_COLOR)));

		progressView.setVisibility(View.INVISIBLE);
		addView(progressView);
	}

	/**
	 * 设置进度条背景颜色
	 *
	 * @param colorStr
	 */
	public void setProgressColor(String colorStr){
		BUCompat.setViewBackgroundDrawable(progressView, new ColorDrawable(
				Color.parseColor(colorStr)));
	}

	/**
	 * 设置当前进度
	 * 
	 * @param p
	 *            进度值在[1-100]之间
	 */
	@Override
	public void updateProgress(int p) {
		if (View.VISIBLE != progressView.getVisibility()) {
			progressView.setVisibility(View.VISIBLE);
		}
		percent = Math.min(100, Math.max(1, p));
		int padding = (mWidth / 100) * (100 - percent);
		setPadding(0, 0, padding, 0);
	}

	@Override
	public void show() {
		progressView.setVisibility(View.VISIBLE);
	}

	@Override
	public void dismiss() {
		progressView.setVisibility(View.GONE);
	}

	/**
	 * 进度条
	 * 
	 * @return
	 */
	public View getProgressView() {
		return progressView;
	}

	/**
	 * 当前比例。返回进度值在[1-100]之间
	 * 
	 * @return
	 */
	public int getPercent() {
		return percent;
	}

}
