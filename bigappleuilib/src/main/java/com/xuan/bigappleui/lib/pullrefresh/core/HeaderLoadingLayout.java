package com.xuan.bigappleui.lib.pullrefresh.core;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 这个类封装了下拉刷新的布局
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-17 上午11:31:50 $
 */
public class HeaderLoadingLayout extends AbstractLoadingLayout {
	/** 旋转动画时间 */
	private static final int ROTATE_ANIM_DURATION = 150;

	/** 向上的动画 */
	private Animation mRotateUpAnim;
	/** 向下的动画 */
	private Animation mRotateDownAnim;

	/** header部分的布局 */
	private HeaderViewWraper headerViewWraper;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 */
	public HeaderLoadingLayout(Context context) {
		super(context);
		init(context);
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 */
	public HeaderLoadingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 *            context
	 */
	private void init(Context context) {
		float pivotValue = 0.5f; // SUPPRESS CHECKSTYLE
		float toDegree = -180f; // SUPPRESS CHECKSTYLE
		// 初始化旋转动画
		mRotateUpAnim = new RotateAnimation(0.0f, toDegree,
				Animation.RELATIVE_TO_SELF, pivotValue,
				Animation.RELATIVE_TO_SELF, pivotValue);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(toDegree, 0.0f,
				Animation.RELATIVE_TO_SELF, pivotValue,
				Animation.RELATIVE_TO_SELF, pivotValue);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
	}

	@Override
	public void setLastUpdatedLabel(CharSequence label) {
		// 如果最后更新的时间的文本是空的话，隐藏前面的标题
		if (TextUtils.isEmpty(label)) {
			headerViewWraper.lastTimeTv.setVisibility(View.INVISIBLE);
		} else {
			headerViewWraper.lastTimeTv.setVisibility(View.VISIBLE);
		}
		headerViewWraper.lastTimeHintTv.setText(label);
	}

	@Override
	protected View createLoadingView(Context context, AttributeSet attrs) {
		headerViewWraper = PullToRefreshUIHelper
				.getHeaderViewWraper((Activity) context);
		return headerViewWraper.root;
	}

	@Override
	protected void onNoMoreData() {
	}

	@Override
	protected void onStateChanged(State curState, State oldState) {
		headerViewWraper.arrowIv.setVisibility(View.VISIBLE);
		headerViewWraper.progressBar.setVisibility(View.INVISIBLE);
		super.onStateChanged(curState, oldState);
	}

	@Override
	protected void onReset() {
		headerViewWraper.arrowIv.clearAnimation();
		headerViewWraper.stateHintTv.setText("下拉可以刷新");
	}

	@Override
	protected void onPullToRefresh() {
		if (State.RELEASE_TO_REFRESH == getPreState()) {
			headerViewWraper.arrowIv.clearAnimation();
			headerViewWraper.arrowIv.startAnimation(mRotateDownAnim);
		}

		headerViewWraper.stateHintTv.setText("下拉可以刷新");
	}

	@Override
	protected void onReleaseToRefresh() {
		headerViewWraper.arrowIv.clearAnimation();
		headerViewWraper.arrowIv.startAnimation(mRotateUpAnim);
		headerViewWraper.stateHintTv.setText("松开后刷新");
	}

	@Override
	protected void onRefreshing() {
		headerViewWraper.arrowIv.clearAnimation();
		headerViewWraper.arrowIv.setVisibility(View.INVISIBLE);
		headerViewWraper.progressBar.setVisibility(View.VISIBLE);
		headerViewWraper.stateHintTv.setText("正在加载...");
	}

	static class HeaderViewWraper {
		public LinearLayout root;

		public RelativeLayout headerContentLayout;

		/** 头部提示语部分 */
		public RelativeLayout headerTextLayout;
		/** 状态提示 */
		public TextView stateHintTv;
		public TextView lastTimeTv;
		public TextView lastTimeHintTv;

		/** 箭头图片 */
		public ImageView arrowIv;
		/** 进度条 */
		public ProgressBar progressBar;
	}

}
