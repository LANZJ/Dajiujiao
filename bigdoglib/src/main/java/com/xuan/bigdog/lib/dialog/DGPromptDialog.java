package com.xuan.bigdog.lib.dialog;

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
public class DGPromptDialog extends DGAbstractDialog {
	public DGPromptDialog(Context context) {
		super(context);
	}

	public static class Builder {
		private final Activity activity;
		private String message;
		private String title;
		private String leftBtnText;
		private String rightBtnText;
		private String defaultContent;
		private PromptDialogListener leftBtnListener;
		private PromptDialogListener rightBtnListener;

		public Builder(Context context) {
			activity = (Activity) context;
		}

		public Builder setDefaultContent(String content) {
			this.defaultContent = content;
			return this;
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

		public DGPromptDialog create() {
			final DGPromptDialog dialog = new DGPromptDialog(activity);
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

			if (!TextUtils.isEmpty(defaultContent)){
				dialog.inputEt.setText(defaultContent);
				dialog.inputEt.setSelection(defaultContent.length());
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
