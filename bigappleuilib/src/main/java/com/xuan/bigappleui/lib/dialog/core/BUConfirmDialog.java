package com.xuan.bigappleui.lib.dialog.core;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

/**
 * 一个提示框，有确定和取消两个按钮供选择
 * 
 * @author xuan
 */
public class BUConfirmDialog extends BUBaseDialog {

	public BUConfirmDialog(Context context) {
		super(context);
	}

	public static class Builder {
		private final Activity activity;
		private String message;
		private String title;
		private String leftBtnText;
		private String rightBtnText;
		private View.OnClickListener leftBtnListener;
		private View.OnClickListener rightBtnListener;

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

		public Builder setLeftBtnText(String leftBtnText) {
			this.leftBtnText = leftBtnText;
			return this;
		}

		public Builder setRightBtnText(String rightBtnText) {
			this.rightBtnText = rightBtnText;
			return this;
		}

		public Builder setOnLeftBtnListener(View.OnClickListener leftBtnListener) {
			this.leftBtnListener = leftBtnListener;
			return this;
		}

		public Builder setOnRightBtnListener(
				View.OnClickListener rightBtnListener) {
			this.rightBtnListener = rightBtnListener;
			return this;
		}

		public BUConfirmDialog create() {
			final BUConfirmDialog dialog = new BUConfirmDialog(activity);
			dialog.inputEt.setVisibility(View.GONE);

			if (!TextUtils.isEmpty(title)) {
				dialog.titleTv.setText(title);
			}

			if (!TextUtils.isEmpty(message)) {
				dialog.messageTv.setText(message);
			}

			if (!TextUtils.isEmpty(leftBtnText)) {
				dialog.leftBtn.setText(leftBtnText);
			}

			dialog.leftBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog.dismiss();
					if (null != leftBtnListener) {
						leftBtnListener.onClick(view);
					}
				}
			});

			if (!TextUtils.isEmpty(rightBtnText)) {
				dialog.rightBtn.setText(rightBtnText);
			}

			dialog.rightBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog.dismiss();
					if (null != rightBtnListener) {
						rightBtnListener.onClick(view);
					}
				}
			});

			return dialog;
		}
	}

}
