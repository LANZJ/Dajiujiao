package com.xuan.bigdog.lib.widgets.toast;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xuan.bigdog.lib.R;

/**
 * 吐司工具
 * 
 * @author xuan
 */
public abstract class DGToastUtils {

	/**
	 * 带图标的一个自定义的Toast
	 * 
	 * @param context
	 * @param text
	 */
	public static void showSuccess(Context context, String text) {
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout toastView = (LinearLayout) toast.getView();
		ImageView imageIv = new ImageView(context);
		imageIv.setImageResource(R.drawable.dg_widgets_toast_success);
		toastView.addView(imageIv, 0);
		toast.show();
	}

}
