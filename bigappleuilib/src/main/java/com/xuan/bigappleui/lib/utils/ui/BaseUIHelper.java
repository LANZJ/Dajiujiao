package com.xuan.bigappleui.lib.utils.ui;

import android.R;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigappleui.lib.album.BUAlbum;
import com.xuan.bigappleui.lib.utils.BUCompat;
import com.xuan.bigappleui.lib.utils.BUDisplayUtil;
import com.xuan.bigappleui.lib.utils.ui.entity.TitleView;

/**
 * 所有动态布局帮助类的基类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-9 下午3:42:38 $
 */
public class BaseUIHelper {
	protected static int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
	protected static int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

	/**
	 * dp转化成sp
	 * 
	 * @param activity
	 * @param dp
	 *            dp为单位的数值
	 * @return
	 */
	protected static int getPx(Activity activity, int dp) {
		return (int) BUDisplayUtil.getPxByDp(activity, dp);
	}

	/**
	 * 获取通用标题View对象
	 * 
	 * @param activity
	 * @param parent
	 * @return
	 */
	protected static TitleView getTitleView(Activity activity, ViewGroup parent) {
		// 标题布局
		RelativeLayout headLayout = new RelativeLayout(activity);
		LinearLayout.LayoutParams headLayoutLp = new LinearLayout.LayoutParams(
				MATCH_PARENT, (int) BUDisplayUtil.getPxByDp(activity, 48));
		headLayout.setLayoutParams(headLayoutLp);
		headLayout.setBackgroundColor(BUAlbum.getConfig().titleBgColor);
		parent.addView(headLayout);

		// 标题-左边文字
		final TextView leftTextView = new TextView(activity);
		RelativeLayout.LayoutParams leftTextViewLp = new RelativeLayout.LayoutParams(
				WRAP_CONTENT, MATCH_PARENT);
		leftTextViewLp.addRule(RelativeLayout.CENTER_VERTICAL,
				RelativeLayout.TRUE);
		leftTextViewLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.TRUE);
		leftTextView.setLayoutParams(leftTextViewLp);
		leftTextView.setTextColor(BUAlbum.getConfig().titleTextColor);
		leftTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		leftTextView.setPadding(getPx(activity, 10),
				leftTextView.getPaddingTop(), getPx(activity, 10),
				leftTextView.getPaddingBottom());
		leftTextView.setGravity(Gravity.CENTER);
		headLayout.addView(leftTextView);

		// 标题-右边文字
		final TextView rightTextView = new TextView(activity);
		RelativeLayout.LayoutParams rightTextViewLp = new RelativeLayout.LayoutParams(
				WRAP_CONTENT, MATCH_PARENT);
		rightTextViewLp.addRule(RelativeLayout.CENTER_VERTICAL,
				RelativeLayout.TRUE);
		rightTextViewLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
				RelativeLayout.TRUE);
		rightTextView.setLayoutParams(rightTextViewLp);
		rightTextView.setTextColor(BUAlbum.getConfig().titleTextColor);
		rightTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		rightTextView.setPadding(getPx(activity, 10),
				leftTextView.getPaddingTop(), getPx(activity, 10),
				leftTextView.getPaddingBottom());
		rightTextView.setGravity(Gravity.CENTER);
		headLayout.addView(rightTextView);

		// 标题-中间标题
		TextView titleTextView = new TextView(activity);
		RelativeLayout.LayoutParams titleTextViewLp = new RelativeLayout.LayoutParams(
				WRAP_CONTENT, WRAP_CONTENT);
		titleTextViewLp.addRule(RelativeLayout.CENTER_IN_PARENT,
				RelativeLayout.TRUE);
		titleTextView.setLayoutParams(titleTextViewLp);
		titleTextView.setTextColor(BUAlbum.getConfig().titleTextColor);
		titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		headLayout.addView(titleTextView);

		// 设置按下效果
		BUCompat.setViewBackgroundDrawable(leftTextView,
				getPressedDrawable(ColorUtils.TRANSLUCENT));
		BUCompat.setViewBackgroundDrawable(rightTextView,
				getPressedDrawable(ColorUtils.TRANSLUCENT));

		TitleView titleView = new TitleView();
		titleView.headLayout = headLayout;
		titleView.leftTextView = leftTextView;
		titleView.rightTextView = rightTextView;
		titleView.titleTextView = titleTextView;
		return titleView;
	}

	/**
	 * 获取按下效果资源
	 * 
	 * @return
	 */
	protected static StateListDrawable getSelectorDrawable() {
		StateListDrawable drawable = new StateListDrawable();
		drawable.addState(new int[] { R.attr.state_pressed },
				new ColorDrawable(ColorUtils.COLOR_EBEBEB));
		return drawable;
	}

	/**
	 * 指定按下颜色效果
	 * 
	 * @param color
	 * @return
	 */
	protected static StateListDrawable getPressedDrawable(int color) {
		StateListDrawable drawable = new StateListDrawable();
		drawable.addState(new int[] { R.attr.state_pressed },
				new ColorDrawable(color));
		return drawable;
	}

}
