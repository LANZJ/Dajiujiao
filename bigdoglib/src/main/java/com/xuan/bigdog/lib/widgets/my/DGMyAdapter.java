package com.xuan.bigdog.lib.widgets.my;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuan.bigdog.lib.R;
import com.xuan.bigdog.lib.widgets.adapter.DGBaseAdapter;
import com.xuan.bigdog.lib.widgets.my.item.BaseMyItem;
import com.xuan.bigdog.lib.widgets.my.item.ContentMyItem;
import com.xuan.bigdog.lib.widgets.my.item.HeadMyItem;
import com.xuan.bigdog.lib.widgets.my.item.IntervalMyItem;

import java.util.List;

/**
 * 我的适配器
 * 
 * @author xuan
 */
public class DGMyAdapter extends DGBaseAdapter {
	private final List<BaseMyItem> dataList;
	private final Context context;

	public DGMyAdapter(Context context, List<BaseMyItem> dataList) {
		this.context = context;
		this.dataList = dataList;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		final BaseMyItem baseMyItem = dataList.get(position);
		if (baseMyItem instanceof HeadMyItem) {
			view = LayoutInflater.from(context).inflate(
					R.layout.dg_widgets_my_listitem_head, null);

			ImageView headIv = (ImageView) view.findViewById(R.id.headIv);
			TextView nameTv = (TextView) view.findViewById(R.id.nameTv);

			HeadMyItem headMyItem = (HeadMyItem) baseMyItem;
			initImageView(headIv, headMyItem.head);
			initTextView(nameTv, headMyItem.name);
			initBaseData(view, headMyItem);
		} else if (baseMyItem instanceof ContentMyItem) {
			// 正常item处理
			view = LayoutInflater.from(context).inflate(
					R.layout.dg_widgets_my_listitem_content, null);

			View seperatorTop = view.findViewById(R.id.seperatorTop);
			View seperatorBottom = view.findViewById(R.id.seperatorBottom);
			ImageView leftIv = (ImageView) view.findViewById(R.id.leftIv);
			TextView leftTv = (TextView) view.findViewById(R.id.leftTv);
			ImageView rightIv = (ImageView) view.findViewById(R.id.rightIv);
			TextView rightTv = (TextView) view.findViewById(R.id.rightTv);

			ContentMyItem contentMyItem = (ContentMyItem) baseMyItem;
			initImageView(leftIv, contentMyItem.leftImage);
			initTextView(leftTv, contentMyItem.leftText);
			initTextView(rightTv, contentMyItem.rightText);

			if (contentMyItem.showRightIv) {
				rightIv.setVisibility(View.VISIBLE);
			} else {
				rightIv.setVisibility(View.GONE);
			}

			if (ContentMyItem.SEPERATOR_TOP == contentMyItem.seperator) {
				seperatorTop.setVisibility(View.VISIBLE);
				seperatorBottom.setVisibility(View.GONE);
			} else if (ContentMyItem.SEPERATOR_MIDDLE == contentMyItem.seperator) {
				seperatorTop.setVisibility(View.VISIBLE);
				seperatorBottom.setVisibility(View.VISIBLE);
			} else if (ContentMyItem.SEPERATOR_BOTTOM == contentMyItem.seperator) {
				seperatorTop.setVisibility(View.GONE);
				seperatorBottom.setVisibility(View.VISIBLE);
			}

			initBaseData(view, contentMyItem);
		} else if (baseMyItem instanceof IntervalMyItem) {
			// 间隔item
			view = LayoutInflater.from(context).inflate(
					R.layout.dg_widgets_my_listitem_interval, null);
		}

		return view;
	}

}
