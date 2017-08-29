package com.xuan.bigappleui.lib.dialog.core;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xuan.bigappleui.lib.R;

import java.util.List;

/**
 * 多个item单选对话框
 * 
 * @author xuan
 */
public class BUSingleSelectDialog extends Dialog {
	private final Activity mActivity;
	private View mContentView;
	private ListView mListView;

	public BUSingleSelectDialog(Context context) {
		super(context);
		mActivity = (Activity) context;
		loadView();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 背景透明
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);// 不需要标题
		setContentView(mContentView);
	}

	// 获取View
	private void loadView() {
		mContentView = LayoutInflater.from(mActivity).inflate(
				R.layout.bu_dialog_singleselect, null);
		mListView = (ListView) mContentView.findViewById(R.id.listView);
	}

	public static class Builder {
		private final Activity activity;
		private List<BUSingleSelectDialog.Item> itemList;
		private ItemOnClickListener itemOnClickListener;

		public Builder(Context context) {
			activity = (Activity) context;
		}

		public Builder setItems(List<BUSingleSelectDialog.Item> itemList){
			this.itemList = itemList;
			return this;
		}

		public Builder setItemOnClickListener(ItemOnClickListener l){
			this.itemOnClickListener = l;
			return this;
		}

		public BUSingleSelectDialog create() {
			final BUSingleSelectDialog dialog = new BUSingleSelectDialog(
					activity);
			if (null != itemList) {
				dialog.mListView.setAdapter(new BaseAdapter() {
					@Override
					public View getView(int postion, View view, ViewGroup arg2) {
						if (null == view) {
							view = LayoutInflater.from(activity).inflate(
									R.layout.bu_dialog_singleselect_listitem,
									null);
						}
						//get ui
						View seperator = view.findViewById(R.id.seperator);
						final TextView itemTv = (TextView) view.findViewById(R.id.itemTv);
						if (0 == postion) {
							// 第一item不要分割线
							seperator.setVisibility(View.GONE);
						}
						//set data
						final Item item = itemList.get(postion);
						itemTv.setText(item.getText());
						//init event
						view.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								if(null != itemOnClickListener){
									itemOnClickListener.onClick(item, view);
								}
								dialog.dismiss();
							}
						});
						//
						return view;
					}

					@Override
					public int getCount() {
						return itemList.size();
					}

					@Override
					public Object getItem(int position) {
						return position;
					}

					@Override
					public long getItemId(int position) {
						return position;
					}
				});
			}

			return dialog;
		}
	}

	@Override
	public void show() {
		super.show();
		WindowManager windowManager = mActivity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.width = (display.getWidth() - dp2px(40)); // 设置宽度
		this.getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(true);
	}

	// dp转ps
	private int dp2px(int dp) {
		DisplayMetrics metrics = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return (int) (metrics.density * dp);
	}

	public static class Item{
		private String text;
		private Object data;

		public Item(){
		}

		public Item(String text, Object data){
			this.text = text;
			this.data =data;
		}

		public Object getData() {
			return data;
		}

		public void setData(Object data) {
			this.data = data;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
	}

	public interface ItemOnClickListener{
		void onClick(Item item, View view);
	}

}
