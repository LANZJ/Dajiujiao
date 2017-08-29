package com.xuan.bigdog.lib.widgets.popupwindow;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xuan.bigdog.lib.R;

/**
 * 顶部popup框
 * 
 * @author xuan
 */
public class TopListPopupWindow extends PopupWindow {
	private final Activity mActivity;
	private final LinearLayout containerLayout;

	private int popupWindowWidth;// 弹窗的宽，已子View的最大宽为准
	private int popupWindowHeight;// 弹窗的高，所有子View的高的和

	public TopListPopupWindow(Activity mActivity) {
		this.mActivity = mActivity;
		containerLayout = (LinearLayout) LayoutInflater.from(mActivity)
				.inflate(R.layout.dg_widgets_popupwindow_toplist, null);
		setContentView(containerLayout);
	}

	/**
	 * 添加一个View
	 * 
	 * @param view
	 * @param viewWidth
	 * @param viewHeight
	 */
	public void addViewItem(View view, int viewWidth, int viewHeight) {
		if (viewWidth > popupWindowWidth) {
			popupWindowWidth = viewWidth;
		}
		popupWindowHeight += viewHeight;

		containerLayout.addView(view);
		setWidth(popupWindowWidth);
		setHeight(popupWindowHeight);
	}

	/**
	 * 添加一个item
	 * 
	 * @param resid
	 * @param text
	 * @param l
	 */
	public void addTopListItem(int resid, String text, OnClickListener l) {
		View item = LayoutInflater.from(mActivity).inflate(
				R.layout.dg_widgets_popupwindow_toplist_item, null);
		ImageView imageIv = (ImageView) item.findViewById(R.id.imageIv);
		TextView textTv = (TextView) item.findViewById(R.id.textTv);
		imageIv.setImageResource(resid);
		textTv.setText(text);
		item.setOnClickListener(l);
	}

	/**
	 * 显示或者隐藏
	 *
	 * @param view
	 * @param x
	 * @param y
	 */
	public void toggle(View view, int x, int y) {
		if (!this.isShowing()) {
			this.showAsDropDown(view, x, y);
		} else {
			this.dismiss();
		}
	}

	/**
	 * 显示或者隐藏
	 * 
	 * @param activity
	 * @param view
	 */
	public void toggle(Activity activity, View view) {
		if (!this.isShowing()) {
			int screenWidth = activity.getWindowManager().getDefaultDisplay()
					.getWidth();
			this.showAsDropDown(
					view,
					screenWidth
							- (int) (activity.getResources()
									.getDimension(R.dimen.dg_widgets_popupwindow_toplist_item_width)),
					0);
		} else {
			this.dismiss();
		}
	}

}
