package com.xuan.bigappleui.lib.utils.asynctask;

import android.content.Context;

import com.xuan.bigappleui.lib.utils.BUContextUtil;
import com.xuan.bigappleui.lib.utils.BUToastUtil;
import com.xuan.bigappleui.lib.utils.asynctask.callback.AsyncTaskResultNullCallback;
import com.xuan.bigappleui.lib.utils.asynctask.helper.Result;

/**
 * 在请求http前，会先判断网络是否存在
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-5-20 下午3:12:25 $
 */
public abstract class NetAbstractTask<T> extends AbstractTask<T> {
	public NetAbstractTask(final Context context) {
		super(context);
		setAsyncTaskResultNullCallback(new AsyncTaskResultNullCallback() {
			@Override
			public void resultNullCallback() {
				BUToastUtil.displayTextShort("无网络连接");
			}
		});
	}

	@Override
	protected Result<T> doHttpRequest(Object... params) {
		if (!BUContextUtil.hasNetwork()) {
			return null;
		}

		// 正真的网络操作
		return onHttpRequest(params);
	}

	/**
	 * 子类实现，网络请求操作
	 * 
	 * @param params
	 * @return
	 */
	protected abstract Result<T> onHttpRequest(Object... params);

}
