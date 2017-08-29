package com.jopool.crow.imkit.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jopool.crow.imlib.utils.CWValidator;

/**
 * 所有适配器的基类
 * 
 * @author xuan
 */
public class CWBaseAdapter extends BaseAdapter {

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		return null;
	}

	/**
	 * 设置文本
	 *
	 * @param textView
	 * @param text
	 */
	protected void initTextView(TextView textView, String text) {
		if (!CWValidator.isEmpty(text)) {
			textView.setVisibility(View.VISIBLE);
			textView.setText(text);
		} else {
			textView.setVisibility(View.INVISIBLE);
		}
	}

}
