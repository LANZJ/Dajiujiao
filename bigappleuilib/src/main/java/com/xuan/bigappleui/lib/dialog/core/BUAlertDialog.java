package com.xuan.bigappleui.lib.dialog.core;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

/**
 * 简单的提示，然后取消
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2015-3-25 下午6:28:02 $
 */
public class BUAlertDialog extends BUBaseDialog {

	public BUAlertDialog(Context context) {
		super(context);
	}

	public static class Builder {
		private final Activity activity;
		private String message;
		private String title;
		private String buttonText;
		private View.OnClickListener l;

		public Builder(Context context) {
			activity = (Activity) context;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setbuttonText(String buttonText) {
			this.buttonText = buttonText;
			return this;
		}

		public Builder setOnButtonListener(View.OnClickListener l) {
			this.l = l;
			return this;
		}

		public BUAlertDialog create() {
			final BUAlertDialog dialog = new BUAlertDialog(activity);
			dialog.leftBtn.setVisibility(View.GONE);
			dialog.rightBtn.setVisibility(View.GONE);
			dialog.btnSeperator.setVisibility(View.GONE);
			dialog.oneBtn.setVisibility(View.VISIBLE);
			dialog.inputEt.setVisibility(View.GONE);

			if (!TextUtils.isEmpty(title)) {
				dialog.titleTv.setText(title);
			}

			if (!TextUtils.isEmpty(message)) {
				dialog.messageTv.setText(message);
			}

			if (!TextUtils.isEmpty(buttonText)) {
				dialog.oneBtn.setText(buttonText);
			}

			dialog.oneBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog.dismiss();
					if (null != l) {
						l.onClick(view);
					}
				}
			});

			return dialog;
		}
	}

}
