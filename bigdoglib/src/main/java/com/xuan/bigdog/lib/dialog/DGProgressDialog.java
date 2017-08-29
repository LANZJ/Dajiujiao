package com.xuan.bigdog.lib.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.xuan.bigdog.lib.R;

/**
 * 自定义加载框
 * 
 * @author xuan
 */
public class DGProgressDialog extends ProgressDialog {
	private String message;
	private TextView define_progress_msg;

	public DGProgressDialog(Context context) {
		super(context);
		message = "正在载入...";
	}

	public DGProgressDialog(Context context, String message) {
		super(context);
		this.message = message;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dg_dialog_view_progressdialog);
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
	public static DGProgressDialog createInstance(Context context) {
		return new DGProgressDialog(context);
	}

}
