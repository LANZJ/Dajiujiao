package com.xuan.bigappleui.lib.dialog.core;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xuan.bigappleui.lib.R;

/**
 * 通用对话框
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2015-3-25 下午5:37:29 $
 */
public abstract class BUBaseDialog extends Dialog {
	private final Activity activity;

	/** 标题栏 */
	protected TextView titleTv;
	/** 提示语 */
	protected TextView messageTv;
	/** 输入框 */
	protected EditText inputEt;

	/** 左边的按钮 */
	protected Button leftBtn;
	/** 中间分割线 */
	protected View btnSeperator;
	/** 右边的按钮 */
	protected Button rightBtn;

	/** 单个按钮 */
	protected Button oneBtn;

	/** 布局View */
	protected View contentView;

	public BUBaseDialog(Context context) {
		super(context);
		activity = (Activity) context;
		loadView();
	}

	// 获取View
	private void loadView() {
		contentView = LayoutInflater.from(activity).inflate(R.layout.bu_dialog,
				null);
		titleTv = (TextView) contentView.findViewById(R.id.titleTv);
		messageTv = (TextView) contentView.findViewById(R.id.messageTv);
		inputEt = (EditText) contentView.findViewById(R.id.inputEt);
		leftBtn = (Button) contentView.findViewById(R.id.leftBtn);
		btnSeperator = contentView.findViewById(R.id.btnSeperator);
		rightBtn = (Button) contentView.findViewById(R.id.rightBtn);
		oneBtn = (Button) contentView.findViewById(R.id.oneBtn);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 背景透明
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);// 不需要标题
		setContentView(contentView);
	}

	@Override
	public void show() {
		super.show();
		WindowManager windowManager = activity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.width = (display.getWidth() - dp2px(20)); // 设置宽度
		this.getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(true);
	}

	// dp转ps
	private int dp2px(int dp) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return (int) (metrics.density * dp);
	}

}
