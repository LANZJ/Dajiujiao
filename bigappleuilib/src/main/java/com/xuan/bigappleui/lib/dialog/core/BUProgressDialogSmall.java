package com.xuan.bigappleui.lib.dialog.core;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.xuan.bigappleui.lib.R;

/**
 * 自定义加载框，这个加载框没有文字比较简单，感觉上不会太打断用户
 * 
 * @author xuan
 */
public class BUProgressDialogSmall extends ProgressDialog {
	private final Activity activity;
	protected View contentView;

	public BUProgressDialogSmall(Context context, int style) {
		super(context, style);
		activity = (Activity) context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bu_dialog_view_small_progressdialog);
	}

	@Override
	public void show() {
		super.show();
		// 设置整个屏幕显示
		WindowManager windowManager = activity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.width = (display.getWidth());
		lp.height = (display.getHeight());
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(true);
	}

	/**
	 * 创建实例
	 * 
	 * @param context
	 */
	public static ProgressDialog createInstance(Context context) {
		return new BUProgressDialogSmall(context, R.style.BuDialogForProgressStyle);
	}

}
