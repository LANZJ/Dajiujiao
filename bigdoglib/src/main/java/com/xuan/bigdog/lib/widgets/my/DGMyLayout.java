package com.xuan.bigdog.lib.widgets.my;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.xuan.bigdog.lib.R;
import com.xuan.bigdog.lib.widgets.DGBaseLayout;
import com.xuan.bigdog.lib.widgets.my.item.BaseMyItem;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的里面的fragment
 * 
 * @author xuan
 */
public class DGMyLayout extends DGBaseLayout {
	/** 标题 */
	private DGTitleLayout dgTitleLayout;

	/** 列表 */
	protected ListView listView;

	/** 退出按钮 */
	protected Button logoutBtn;

	protected List<BaseMyItem> dataList;
	protected DGMyAdapter myAdapter;

	public DGMyLayout(Context context) {
		super(context);
	}

	public DGMyLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void dgInit() {
		inflate(getContext(), R.layout.dg_widgets_my_mylayout, this);
		injectView();
		initWidigets();
	}

	/** 注入View */
	private void injectView() {
		listView = (ListView) findViewById(R.id.listView);
		logoutBtn = (Button) findViewById(R.id.logoutBtn);
		dgTitleLayout = (DGTitleLayout) findViewById(R.id.dgTitleLayout);
	}

	/** 初始化列表 */
	private void initWidigets() {
		dataList = new ArrayList<BaseMyItem>();
		myAdapter = new DGMyAdapter(getContext(), dataList);
		listView.setAdapter(myAdapter);
	}

	/**
	 * 是否显示退出登录按钮
	 * 
	 * @param showLogoutBtn
	 * @return
	 */
	public DGMyLayout configShowLogoutBtn(boolean showLogoutBtn) {
		if (showLogoutBtn) {
			logoutBtn.setVisibility(View.VISIBLE);
		} else {
			logoutBtn.setVisibility(GONE);
		}
		return this;
	}

	/**
	 * 配置退出登录按钮
	 * 
	 * @param btnText
	 * @param l
	 * @return
	 */
	public DGMyLayout configLogoutBtn(String btnText, View.OnClickListener l) {
		if (null != btnText) {
			logoutBtn.setText(btnText);
		}

		if (null != l) {
			logoutBtn.setOnClickListener(l);
		}
		return this;
	}

	/**
	 * 添加Item
	 * 
	 * @return
	 */
	public DGMyLayout addMyItem(BaseMyItem baseMyItem) {
		dataList.add(baseMyItem);
		return this;
	}

	/**
	 * 添加Item
	 * 
	 * @return
	 */
	public DGMyLayout addMyItems(List<BaseMyItem> baseMyItemList) {
		dataList.addAll(baseMyItemList);
		return this;
	}

	/**
	 * 刷新数据
	 * 
	 * @return
	 */
	public DGMyLayout updateMyItem() {
		myAdapter.notifyDataSetChanged();
		return this;
	}

	public ListView getListView() {
		return listView;
	}

	public Button getLogoutBtn() {
		return logoutBtn;
	}

	public void setLogoutBtn(Button logoutBtn) {
		this.logoutBtn = logoutBtn;
	}

	public DGMyAdapter getMyAdapter() {
		return myAdapter;
	}

	public List<BaseMyItem> getDataList() {
		return dataList;
	}

	public DGTitleLayout getDgTitleLayout() {
		return dgTitleLayout;
	}

}
