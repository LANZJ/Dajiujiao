package com.xuan.bigdog.lib.update.core;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * Handler 达到异步处理
 * 
 * @author xuan
 */
public class UpdateHandler extends Handler {
	private final Context context;
	private final UpdateConfig updateConfig;

	public static final int UPDATE_NOTIFICATION_PROGRESS = 0x1;
	public static final int COMPLETE_DOWNLOAD_APK = 0x2;

	public UpdateHandler(Context context, UpdateConfig updateConfig) {
		this.context = context;
		this.updateConfig = updateConfig;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch (msg.what) {
		case UPDATE_NOTIFICATION_PROGRESS:
			UpdateUIHelper.showDownloadNotificationUI(context, msg.arg1,
					updateConfig.getNoticeMessage());
			break;
		case COMPLETE_DOWNLOAD_APK:
			UpdateUIHelper.showDownloadFinishNotificationUI(context,
					updateConfig.getSaveFileName());
			break;
		}
	}
}
