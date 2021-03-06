package com.xuan.bigappleui.lib.pullrefresh.core;

import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigappleui.lib.pullrefresh.core.FooterLoadingLayout.FooterViewWraper;
import com.xuan.bigappleui.lib.pullrefresh.core.HeaderLoadingLayout.HeaderViewWraper;
import com.xuan.bigappleui.lib.utils.ui.BaseUIHelper;
import com.xuan.bigappleui.lib.view.img.BUArrowImageView;

/**
 * 创建View帮助工具
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-17 上午10:08:09 $
 */
public abstract class PullToRefreshUIHelper extends BaseUIHelper {

	/**
	 * 获取下拉刷新尾部布局
	 * 
	 * @param activity
	 * @return
	 */
	public static FooterViewWraper getFooterViewWraper(Activity activity) {
		LinearLayout root = new LinearLayout(activity);
		root.setOrientation(LinearLayout.HORIZONTAL);

		// 创建footerContent布局
		LinearLayout footerContent = new LinearLayout(activity);
		LinearLayout.LayoutParams footerContentLp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, getPx(activity, 60));
		footerContent.setLayoutParams(footerContentLp);
		footerContent.setGravity(Gravity.CENTER);
		root.addView(footerContent);

		// 创建进度条布局
		ProgressBar progressBar = new ProgressBar(activity);
		LinearLayout.LayoutParams progressBarLp = new LinearLayout.LayoutParams(
				getPx(activity, 28), getPx(activity, 28));
		progressBarLp.setMargins(0, 0, getPx(activity, 8), 0);
		progressBar.setLayoutParams(progressBarLp);
		footerContent.addView(progressBar);

		// 创建提示文本布局
		TextView textView = new TextView(activity);
		LinearLayout.LayoutParams textViewLp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		textView.setLayoutParams(textViewLp);
		textView.setTextColor(Color.parseColor("#999999"));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		footerContent.addView(textView);

		FooterViewWraper footerViewWraper = new FooterViewWraper();
		footerViewWraper.root = root;
		footerViewWraper.footerContent = footerContent;
		footerViewWraper.progressBar = progressBar;
		footerViewWraper.textView = textView;
		return footerViewWraper;
	}

	/**
	 * 获取头部布局
	 * 
	 * @return
	 */
	public static HeaderViewWraper getHeaderViewWraper(Activity activity) {
		int headerTextHintId = 1;
		int headerTextTimeHintId = 2;
		int headerTextLayoutId = 3;

		/** 根布局 */
		LinearLayout root = new LinearLayout(activity);
		root.setOrientation(LinearLayout.VERTICAL);
		/** 根布局->内容布局 */
		RelativeLayout headerContentLayout = new RelativeLayout(activity);
		LinearLayout.LayoutParams headerContentLayoutLp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, getPx(activity, 60));
		headerContentLayout.setLayoutParams(headerContentLayoutLp);
		headerContentLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		root.addView(headerContentLayout);
		/** 根布局->内容布局 ->文字提示布局 */
		RelativeLayout headerTextLayout = new RelativeLayout(activity);
		headerTextLayout.setId(headerTextLayoutId);
		RelativeLayout.LayoutParams headerTextLayoutLp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		headerTextLayoutLp.addRule(RelativeLayout.CENTER_IN_PARENT,
				RelativeLayout.TRUE);
		headerTextLayout.setLayoutParams(headerTextLayoutLp);
		headerContentLayout.addView(headerTextLayout);
		/** 根布局->内容布局 ->文字提示布局->上拉下拉可以刷新提示语 */
		TextView headerTextHint = new TextView(activity);
		headerTextHint.setId(headerTextHintId);
		RelativeLayout.LayoutParams headerTextHintLp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		headerTextHintLp.addRule(RelativeLayout.ALIGN_PARENT_TOP,
				RelativeLayout.TRUE);
		headerTextHint.setLayoutParams(headerTextHintLp);
		headerTextHint.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		headerTextHint.setTextColor(Color.parseColor("#999999"));
		headerTextHint.setText("下拉可以刷新");
		headerTextLayout.addView(headerTextHint);
		/** 根布局->内容布局 ->文字提示布局->最后更新时间 : */
		TextView headerTextTimeHint = new TextView(activity);
		headerTextTimeHint.setId(headerTextTimeHintId);
		RelativeLayout.LayoutParams headerTextTimeHintLp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		headerTextTimeHintLp.addRule(RelativeLayout.BELOW, headerTextHintId);
		headerTextTimeHint.setLayoutParams(headerTextTimeHintLp);
		headerTextTimeHint.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
		headerTextTimeHint.setTextColor(Color.parseColor("#999999"));
		headerTextTimeHint.setText("最后更新时间 :");
		headerTextTimeHint.setPadding(0, getPx(activity, 6), 0, 0);
		headerTextLayout.addView(headerTextTimeHint);
		/** 根布局->内容布局 ->文字提示布局->最后更新时间 :XXX提示 */
		TextView headerTextTimeText = new TextView(activity);
		RelativeLayout.LayoutParams headerTextTimeTextLp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		headerTextTimeTextLp.addRule(RelativeLayout.BELOW, headerTextHintId);
		headerTextTimeTextLp.addRule(RelativeLayout.RIGHT_OF,
				headerTextTimeHintId);
		headerTextTimeText.setLayoutParams(headerTextTimeTextLp);
		headerTextTimeText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
		headerTextTimeText.setTextColor(Color.parseColor("#999999"));
		headerTextTimeText.setPadding(getPx(activity, 2), getPx(activity, 6),
				0, 0);
		headerTextLayout.addView(headerTextTimeText);
		/** 根布局->内容布局->箭头图标 */
		ImageView arrow = new BUArrowImageView(activity);
		RelativeLayout.LayoutParams arrowLp = new RelativeLayout.LayoutParams(
				getPx(activity, 40), getPx(activity, 40));
		arrowLp.addRule(RelativeLayout.LEFT_OF, headerTextLayoutId);
		arrowLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		arrowLp.setMargins(0, 0, getPx(activity, 8), 0);
		arrow.setLayoutParams(arrowLp);
		headerContentLayout.addView(arrow);
		/** 根布局->内容布局->进度圈 */
		ProgressBar progressBar = new ProgressBar(activity);
		RelativeLayout.LayoutParams progressBarLp = new RelativeLayout.LayoutParams(
				getPx(activity, 28), getPx(activity, 28));
		progressBarLp.addRule(RelativeLayout.LEFT_OF, headerTextLayoutId);
		progressBarLp.addRule(RelativeLayout.CENTER_VERTICAL,
				RelativeLayout.TRUE);
		progressBarLp.setMargins(0, 0, getPx(activity, 8), 0);
		progressBar.setLayoutParams(progressBarLp);
		progressBar.setVisibility(View.INVISIBLE);
		headerContentLayout.addView(progressBar);

		HeaderViewWraper headerViewWraper = new HeaderViewWraper();
		headerViewWraper.root = root;
		headerViewWraper.headerContentLayout = headerContentLayout;
		headerViewWraper.headerTextLayout = headerTextLayout;
		headerViewWraper.stateHintTv = headerTextHint;
		headerViewWraper.lastTimeHintTv = headerTextTimeHint;
		headerViewWraper.lastTimeTv = headerTextTimeText;
		headerViewWraper.arrowIv = arrow;
		headerViewWraper.progressBar = progressBar;
		return headerViewWraper;
	}

}
