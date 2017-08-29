package com.xuan.bigdog.lib.update;

import android.content.Context;

import com.xuan.bigapple.lib.utils.log.LogUtils;
import com.xuan.bigdog.lib.update.core.AsyncCheck;
import com.xuan.bigdog.lib.update.core.UpdateConfig;
import com.xuan.bigdog.lib.update.core.UpdateHandler;

/**
 * 更新组件
 * 
 * 需要权限:
 * android.permission.INTERNET
 * android.permission.ACCESS_NETWORK_STATE
 * android.permission.WRITE_EXTERNAL_STORAGE
 * 
 * @author xuan
 */
public class UpdateHelper {
	private final Context mContext;
	private final UpdateConfig mUpdateconfig;

	public UpdateHelper(Context context, UpdateConfig updateconfig) {
		this.mContext = context;
		if (null == updateconfig) {
			updateconfig = new UpdateConfig();
		}
		this.mUpdateconfig = updateconfig;

		if (null == mUpdateconfig.getHandler()) {
			mUpdateconfig.setHandler(new UpdateHandler(context, mUpdateconfig));
		}
	}

	/**
	 * 检查更新
	 */
	public void check() {
		if (null == mContext) {
			LogUtils.e("The context must not be null.");
			return;
		}
		AsyncCheck asyncCheck = new AsyncCheck(mContext, mUpdateconfig);
		asyncCheck.execute();
	}

}