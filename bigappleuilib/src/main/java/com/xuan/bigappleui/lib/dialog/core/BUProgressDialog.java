package com.xuan.bigappleui.lib.dialog.core;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.xuan.bigappleui.lib.R;

/**
 * 自定义加载框
 * 
 * @author xuan
 */
public class BUProgressDialog extends ProgressDialog {
	private String message;
	private TextView define_progress_msg;

	public BUProgressDialog(Context context) {
		super(context);
		message = "正在载入...";
	}

	public BUProgressDialog(Context context, String message) {
		super(context);
		this.message = message;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bu_dialog_view_progressdialog);
		define_progress_msg = (TextView) findViewById(R.id.define_progress_msg);
		define_progress_msg.setText(message);
	}

	public void setTitle(String message) {
		this.message = message;
	}

	/**
	 * 创建一个实例
	 * 
	 * @param context
	 * @return
	 */
	public static BUProgressDialog createInstance(Context context) {
		return new BUProgressDialog(context);
	}

	public static BUProgressDialog createInstance(Context context, String message) {
		return new BUProgressDialog(context, message);
	}

}
