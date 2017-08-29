package com.xuan.bigdog.lib.widgets.edittext;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.R;
import com.xuan.bigdog.lib.widgets.DGBaseLayout;

/**
 * 输入框组件
 * 
 * @author xuan
 */
public class DGCanClearEditText extends DGBaseLayout {
	/** 输入框 */
	private EditText inputEt;
	/** 点击清理 */
	private Button clearBtn;

	private TextWatcher externalTextWatcher;

	public DGCanClearEditText(Context context) {
		super(context);
	}

	public DGCanClearEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void dgInit() {
		inflate(getContext(), R.layout.dg_view_edittext_canclearedittext, this);
		inputEt = (EditText) findViewById(R.id.inputEt);
		clearBtn = (Button) findViewById(R.id.clearBtn);
		clearBtn.setVisibility(View.GONE);

		inputEt.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus){
					if (inputEt.getText().length()>0){
						clearBtn.setVisibility(View.VISIBLE);
					}else {
						clearBtn.setVisibility(View.GONE);
					}

					// 文本数据监控
					inputEt.addTextChangedListener(new TextWatcher() {
						@Override
						public void onTextChanged(CharSequence arg0, int arg1, int arg2,
												  int arg3) {
							if (null != externalTextWatcher) {
								externalTextWatcher.onTextChanged(arg0, arg1, arg2, arg3);
							}
						}

						@Override
						public void beforeTextChanged(CharSequence arg0, int arg1,
													  int arg2, int arg3) {

							if (null != externalTextWatcher) {
								externalTextWatcher.beforeTextChanged(arg0, arg1, arg2,
										arg3);
							}
						}

						@Override
						public void afterTextChanged(Editable s) {
							if (Validators.isEmpty(s.toString())) {
								clearBtn.setVisibility(View.GONE);
							} else {
								clearBtn.setVisibility(View.VISIBLE);
							}

							if (null != externalTextWatcher) {
								externalTextWatcher.afterTextChanged(s);
							}
						}
					});
				}else {
					clearBtn.setVisibility(View.GONE);
				}
			}
		});

		// 清空内容
		clearBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				inputEt.setText(null);
			}
		});
	}

	public EditText getInputEt() {
		return inputEt;
	}

	public Button getClearBtn() {
		return clearBtn;
	}

	public void setExternalTextWatcher(TextWatcher externalTextWatcher) {
		this.externalTextWatcher = externalTextWatcher;
	}

}
