package com.xuan.bigdog.lib.tabframe.mcall;

import android.support.v4.app.Fragment;

import com.xuan.bigapple.lib.ioc.app.BPFragmentActivity;
import com.xuan.bigapple.lib.utils.log.LogUtils;

/**
 * FragmentActivity基类，实现了与Fragment之间的通讯机制
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-7-10 下午3:07:21 $
 */
public class CallFragmentActivity extends BPFragmentActivity implements
		CallByFragmentListener {

	/**
	 * 子类可以调用该方法调用指定的Fragment
	 * 
	 * @param tag
	 * @param command
	 * @param data
	 */
	protected void callFragment(String tag, int command, Object... data) {
		Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
		if (null == fragment) {
			LogUtils.e("Fragment of tag[" + tag
					+ "] command[" + command + "] not exists.");
		} else {
			if(fragment instanceof CallByActivityListener){
				CallByActivityListener callByActivityListener = (CallByActivityListener) fragment;
				callByActivityListener.callByActivity(command, data);
			}else{
				LogUtils.e("Fragment of tag[" + tag
						+ "] not implements the CallByActivityListener.");
			}
		}
	}

	@Override
	public void callByFragment(int command, Object... data) {
		// 如果这个Activity需要被Fragment调用就复写这个类
	}

}
