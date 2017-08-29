package com.jopool.crow.imlib.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 聊天界面时间显示格式
 * 
 * @author xuan
 */
public class CWFriendlyTimeUtil {
	private static final int DAY_INTER = 24 * 60 * 60 * 1000;
	private static final int HOUR_INTER = 60 * 60 * 1000;
	private static final int MINUTE_INTER = 60 * 1000;
	private static final int SECOND_INTER = 1000;

	private final static ThreadLocal<SimpleDateFormat> dateFormater1 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("HH:mm");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		}
	};

	/**
	 * 规则如下,显示在聊天界面消息的时间<br>
	 * 
	 * 1、今天的：显示：今天 9:10<br>
	 * 2、昨天的：显示：昨天 9:10<br>
	 * 3、昨天之前显示:2015年06月07日 9：10 <br>
	 * 4、时间不合法的情况下显示：2015年06月07日 9：10
	 * 
	 * @param time
	 * @return
	 */
	public static String friendlyTime(Date time) {
		if (null == time) {
			return "unknown";
		}

		String ftime = "";
		Calendar cal = Calendar.getInstance();
		long intervals = cal.getTimeInMillis() - time.getTime();
		if (intervals < 0) {
			return dateFormater2.get().format(time);
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) { // 今天
			ftime = "今天 " + dateFormater1.get().format(time);
		} else if (days == 1) {// 昨天
			ftime = "昨天 " + dateFormater1.get().format(time);
		} else {
			ftime = dateFormater2.get().format(time);
		}

		return ftime;
	}

	/**
	 * 规则如下，显示在消息列表：<br>
	 *
	 * 0、如果小于1秒钟内，显示刚刚<br>
	 * 1、如果在1分钟内，显示XXX秒前<br>
	 * 2、如果在1小时内，显示XXX分钟前<br>
	 * 3、如果在1小时外的今天内，显示今天15:32<br>
	 * 4、如果是昨天的，显示昨天15:32<br>
	 * 5、其余显示，13-10-15<br>
	 * 6、时间不合法的情况下显示：年-月-日，如25-10-21
	 *
	 * @param time
	 * @return
	 */
	public static String friendlyTime2(Date time) {
		if (null == time) {
			return "unknown";
		}

		String ftime = "";
		Calendar cal = Calendar.getInstance();
		long intervals = cal.getTimeInMillis() - time.getTime();
		if (intervals < 0) {
			return dateFormater2.get().format(time);
		}

		long ltd = time.getTime() / DAY_INTER;
		long ctd = cal.getTimeInMillis() / DAY_INTER;
		int days = (int) (ctd - ltd);
		if (days == 0) { // 今天
			long lth = time.getTime() / HOUR_INTER;
			long cth = cal.getTimeInMillis() / HOUR_INTER;
			int hours = (int) (cth - lth);
			if (hours == 0) {// 1小时内
				long ltm = time.getTime() / MINUTE_INTER;
				long ctm = cal.getTimeInMillis() / MINUTE_INTER;
				int minutes = (int) (ctm - ltm);
				if (minutes == 0) {
					long lts = time.getTime() / SECOND_INTER;
					long cts = cal.getTimeInMillis() / SECOND_INTER;
					int seconds = (int) (cts - lts);
					if (seconds <= 0) {
						ftime = "刚刚";
					} else {
						ftime = seconds + "秒前";
					}
				} else {
					ftime = minutes + "分钟前";
				}
			} else {
				ftime = "今天  " + dateFormater1.get().format(time);
			}
		} else if (days == 1) {// 昨天
			ftime = "昨天  " + dateFormater1.get().format(time);
		} else {// 其余时间
			ftime = dateFormater2.get().format(time);
		}

		return ftime;
	}

}
