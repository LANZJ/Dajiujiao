package com.xuan.bigdog.lib.tabframe.mcall;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuan.bigapple.lib.ioc.ViewUtils;

/**
 * tab的一个界面，当界面第一次启动的时候，生命周期如下<br>
 * 
 * ----------------------Fragment1:onAttach <br>
 * ----------------------Fragment1:onCreate <br>
 * ----------------------Fragment1:onCreateView <br>
 * ----------------------Fragment1:onActivityCreated <br>
 * ----------------------Fragment1:onStart <br>
 * ----------------------Fragment1:onResume<br>
 * 
 * 当他的宿主Activity被销毁时发生的生命周期如下<br>
 * 
 * ----------------------Fragment1:onPause<br>
 * ----------------------Fragment1:onStop<br>
 * ----------------------Fragment1:onDestroyView<br>
 * ----------------------Fragment1:onDestroy<br>
 * ----------------------Fragment1:onDetach<br>
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-7-10 下午1:33:39 $
 */
public class DGFrameFragment extends CallFragment {
	@Override
	public void callByActivity(int command, Object... data) {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = getFragmentView();
		ViewUtils.inject(this, contentView);// 可以这样使用bigapple里面的注解注入
		initFragmentWidgets(inflater, container, savedInstanceState);
		return contentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	/**
	 * 子类实现，设置Fragment的View
	 */
	protected View getFragmentView() {
		TextView tv = new TextView(getActivity());
		tv.setText("This is default fragment.");
		return tv;
	}

	/**
	 * 子类实现，在这里可以初始化数据
	 *
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 */
	protected void initFragmentWidgets(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
	}

}
