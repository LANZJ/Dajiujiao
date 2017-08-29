package com.xuan.bigappleui.lib.dialog.core;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

/**
 * 可输入文字的自定义Dialog
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2015-3-24 下午4:47:13 $
 */
public class BUPromptDialog extends BUBaseDialog {
	public BUPromptDialog(Context context) {
		super(context);
	}

	public static class Builder {
		private final Activity activity;
		private String message;
		private String title;
		private String leftBtnText;
		private String rightBtnText;
		private PromptDialogListener leftBtnListener;
		private PromptDialogListener rightBtnListener;

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

		public Builder setOnLeftBtnListener(PromptDialogListener leftBtnListener) {
			this.leftBtnListener = leftBtnListener;
			return this;
		}

		public Builder setOnRightBtnListener(
				PromptDialogListener rightBtnListener) {
			this.rightBtnListener = rightBtnListener;
			return this;
		}

		public BUPromptDialog create() {
			final BUPromptDialog dialog = new BUPromptDialog(activity);
			dialog.inputEt.setVisibility(View.VISIBLE);

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
						leftBtnListener.onClick(view, dialog.inputEt.getText()
								.toString());
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
						rightBtnListener.onClick(view, dialog.inputEt.getText()
								.toString());
					}
				}
			});

			return dialog;
		}
	}

	/**
	 * 点击监听
	 * 
	 * @author xuan
	 */
	public interface PromptDialogListener {
		void onClick(View view, String inputText);
	}

}
