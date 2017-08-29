package com.xuan.bigdog.lib.update.core;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.xuan.bigdog.lib.dialog.DGConfirmDialog;
import com.xuan.bigdog.lib.update.entity.UpdateInfo;

/**
 * 一些提示框
 * 
 * @author xuan
 */
public abstract class UpdateUIHelper {
	/**
	 * 弹出提示更新窗口
	 * 
	 * @param context
	 * @param updateInfo
	 * @param updateConfig
	 */
	public static void showUpdateUI(final Context context,
			final UpdateInfo updateInfo, final UpdateConfig updateConfig) {
		new DGConfirmDialog.Builder(context).setTitle("提示")
				.setMessage(updateInfo.getDescription()).setLeftBtnText("下次再说")
				.setRightBtnText("升级")
				.setOnRightBtnListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						NetWorkUtils netWorkUtils = new NetWorkUtils(context);
						int type = netWorkUtils.getNetType();
						if (type != 1) {
							// 非1表示不是WIFI下，需要提醒一下用户
							UpdateUIHelper.showNetDialog(context, updateInfo,
									updateConfig);
						} else {
							AsyncDownLoad asyncDownLoad = new AsyncDownLoad(
									updateConfig, updateInfo);
							asyncDownLoad.execute();
						}
					}
				}).create().show();
	}

	/**
	 * 提醒非WIFI下是否下载
	 * 
	 * @param context
	 * @param updateInfo
	 * @param updateConfig
	 */
	public static void showNetDialog(Context context,
			final UpdateInfo updateInfo, final UpdateConfig updateConfig) {
		new DGConfirmDialog.Builder(context).setTitle("下载提示")
				.setMessage("您在目前的网络环境下继续下载将可能会消耗手机流量，请确认是否继续下载？")
				.setLeftBtnText("取消下载").setRightBtnText("继续下载")
				.setOnRightBtnListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						AsyncDownLoad asyncDownLoad = new AsyncDownLoad(
								updateConfig, updateInfo);
						asyncDownLoad.execute();
					}
				}).create().show();
	}

	/**
	 * 下载进度提示
	 *
	 * @param context
	 * @param progress
	 * @param message
	 */
	public static void showDownloadNotificationUI(Context context,
			final int progress, String message) {
		if (null != context) {
			// 行为
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
					new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);

			// 通知对象
			String contentText = new StringBuffer().append(progress)
					.append("%").toString();
			NotificationCompat.Builder ntfBuilder = new NotificationCompat.Builder(
					context).setSmallIcon(context.getApplicationInfo().icon)
					.setTicker("开始下载...").setContentTitle(message)
					.setContentIntent(contentIntent)
					.setContentText(contentText)
					.setProgress(100, progress, false);

			// 显示
			((NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE)).notify(
					UpdateConfig.DOWNLOAD_NOTIFICATION_ID, ntfBuilder.build());
		}
	}

	/**
	 * 给通知栏显示下载完成
	 * 
	 * @param context
	 * @param apkFileName
	 */
	public static void showDownloadFinishNotificationUI(Context context,
			String apkFileName) {
		if (null != context) {
			// 行为
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse("file://" + apkFileName),
					"application/vnd.android.package-archive");
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					intent, 0);

			// 通知对象
			NotificationCompat.Builder ntfBuilder = new NotificationCompat.Builder(
					context).setSmallIcon(context.getApplicationInfo().icon)
					.setTicker("任务下载完成").setContentTitle("下载完成，点击安装")
					.setContentIntent(pendingIntent);

			// 显示
			((NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE)).notify(
					UpdateConfig.DOWNLOAD_NOTIFICATION_ID, ntfBuilder.build());
		}
	}

}
