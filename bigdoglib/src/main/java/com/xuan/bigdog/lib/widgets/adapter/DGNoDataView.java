package com.xuan.bigdog.lib.widgets.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.R;
import com.xuan.bigdog.lib.widgets.DGBaseLayout;

import java.util.List;

/**
 * 没数据布局
 * 
 * @author xuan
 */
public class DGNoDataView extends DGBaseLayout {
	private ImageView imageIv;
	private TextView messageTv;

	public DGNoDataView(Context context) {
		super(context);
	}

	public DGNoDataView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void dgInit() {
		inflate(getContext(), R.layout.dg_widgets_adapter_layout_nodata, this);
		imageIv = (ImageView) findViewById(R.id.imageIv);
		messageTv = (TextView) findViewById(R.id.messageTv);
	}

	/**
	 * 提示图片
	 * 
	 * @param resid
	 * @return
	 */
	public DGNoDataView configImage(int resid) {
		imageIv.setImageResource(resid);
		return this;
	}

	/**
	 * 提示消息
	 * 
	 * @param message
	 * @return
	 */
	public DGNoDataView configMessage(String message) {
		messageTv.setText(message);
		return this;
	}

	/**
	 * 显示
	 */
	public void show() {
		setVisibility(View.VISIBLE);
	}

	/**
	 * 隐藏
	 */
	public void hide() {
		setVisibility(View.GONE);
	}

	/**
	 * list空就显示，非空就隐藏
	 *
	 * @param list
	 */
	public void showIfEmpty(List<?> list) {
		if (Validators.isEmpty(list)) {
			show();
		} else {
			hide();
		}
	}

}
