package com.zjyeshi.dajiujiao.buyer.views.other;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.zjyeshi.dajiujiao.R;

/**
 * 基于ImageView的checkbox
 * 
 * @author xuan
 */
public class IVCheckBox extends ImageView {
	private boolean isChecked;
	private OnClickListener externalOnClickListener;

	public IVCheckBox(Context context) {
		super(context);
		init();
	}

	public IVCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public IVCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		super.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isChecked) {
					setImageResource(R.drawable.no_selected);
				} else {
					setImageResource(R.drawable.selected);
				}
				isChecked = !isChecked;

				if (null != externalOnClickListener) {
					externalOnClickListener.onClick(view);
				}
			}
		});
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean checked) {
		if (checked) {
			setImageResource(R.drawable.selected);
		} else {
			setImageResource(R.drawable.no_selected);
		}

		this.isChecked = checked;
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		this.externalOnClickListener = l;
	}

	public void toggle() {
		setChecked(!isChecked);
	}

}
