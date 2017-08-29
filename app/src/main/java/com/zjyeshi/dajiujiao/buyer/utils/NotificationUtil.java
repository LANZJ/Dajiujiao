package com.zjyeshi.dajiujiao.buyer.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NotificationCompat;

import com.xuan.bigapple.lib.utils.uuid.UUIDUtils;
import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.R;

import java.util.UUID;

/**
 * 通知类
 * 
 * @author wuhk
 * 
 */
public abstract class NotificationUtil {

	public static int NotificationFlag = 1;
	/**
	 * 显示顶部通知
	 *
	 */
	public static void showNotification(String title , String content , Intent intent) {
		Context context = App.instance;

		final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

		// params
		int smallIconId = R.drawable.logo;
		Bitmap largeIcon = ((BitmapDrawable)context. getResources().getDrawable(R.drawable.logo)).getBitmap();

		builder.setLargeIcon(largeIcon)
				.setSmallIcon(smallIconId)
				.setContentTitle(title)
				.setContentText(content)
				.setTicker(content)
				.setDefaults(Notification.DEFAULT_ALL);
		if (null != intent){
			PendingIntent pi = PendingIntent.getActivity(
					App.instance, UUID.randomUUID().hashCode(), intent, 0);
			builder.setContentIntent(pi);
		}

		final Notification n = builder.getNotification();
		n.flags = Notification.FLAG_AUTO_CANCEL;

		nm.notify(NotificationFlag ++ , n);
	}
}
