package com.zjyeshi.dajiujiao.buyer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 请假天数计算
 * Created by wuhk on 2016/7/25.
 */
public class LeaveDaysUtil {

    private static final int DAY_MILLIS_24_HOUR = 24 * 60 * 60 * 1000;//24小时制的一天
    private static final int DAY_MILLIS_8_HOUR = 8 * 60 * 60 * 1000;//8小时制的一天
    private static final int HOUR_MILLIS_8_HOUR = 60 * 60 * 1000;//时
    private static final int MINUTE_MILLIS_8_HOUR = 60 * 1000;//分

    /**
     * 计算间隔日期，精确到分
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static String cal8HourDay(String startTime, String endTime, String amStartTime, String amEndTime,
                                     String pmStartTime, String pmEndTime) {
        String times = "";
        try {
            //精确到分时间戳
            SimpleDateFormat speSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date startDate = speSdf.parse(startTime);
            Date endDate = speSdf.parse(endTime);
            long sTime = startDate.getTime();
            long eTime = endDate.getTime();
            //精确到天的时间戳
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date smallDate = sdf.parse(startTime.split(" ")[0]);
            Date bigDate = sdf.parse(endTime.split(" ")[0]);
            long time1 = smallDate.getTime();
            long time2 = bigDate.getTime();

            //计算两者取天的时间时的天数，之后将开始多算的减去，结尾少算的添上
            long allBetweenTime = time2 - time1;
            long allDays = allBetweenTime / DAY_MILLIS_24_HOUR;

            //开始那天的时间计算
            long topAmStartTimes = (speSdf.parse(startTime.split(" ")[0] + " " + amStartTime)).getTime();
            long topAmEndTimes = (speSdf.parse(startTime.split(" ")[0] + " " + amEndTime)).getTime();
            long topPmStartTimes = (speSdf.parse(startTime.split(" ")[0] + " " + pmStartTime)).getTime();
            long topPmEndTimes = (speSdf.parse(startTime.split(" ")[0] + " " + pmEndTime)).getTime();
            //获取开始那天需要计入请假的时间 ， 但是最后这个值是需要减去的时间
            long topTimes = getTopTime(sTime, topAmStartTimes, topAmEndTimes, topPmStartTimes, topPmEndTimes);
            //如果开始是双休日，不变，若不是，将这个时间作为要减去的时间，因为最后要化成8小时制毫秒计算，所以用8小时制时间减
            if (!isWeekEnd(sTime)) {
                topTimes = DAY_MILLIS_8_HOUR - topTimes;
            }

            //结束那天的时间计算
            long tailAmStartTimes = (speSdf.parse(endTime.split(" ")[0] + " " + amStartTime)).getTime();
            long tailAmEndTimes = (speSdf.parse(endTime.split(" ")[0] + " " + amEndTime)).getTime();
            long tailPmStartTimes = (speSdf.parse(endTime.split(" ")[0] + " " + pmStartTime)).getTime();
            long tailPmEndTimes = (speSdf.parse(endTime.split(" ")[0] + " " + pmEndTime)).getTime();
            //获取结束那天需要加入请假的时间
            long tailTims = getTailTime(eTime, tailAmStartTimes, tailAmEndTimes, tailPmStartTimes, tailPmEndTimes);
            //计算总的请假时间毫秒数 = 之前获取的总天数 - 开始那天多算的 +　结尾那天少算的
            long leaveTimes = allDays * DAY_MILLIS_8_HOUR - topTimes + tailTims;

            //获取到了总的时间毫秒数，根据8小时制的时间算出天，时，分
            long between_days = leaveTimes / DAY_MILLIS_8_HOUR;//天
            long between_hous = (leaveTimes % DAY_MILLIS_8_HOUR) / HOUR_MILLIS_8_HOUR;//时
            long between_mins = ((leaveTimes % DAY_MILLIS_8_HOUR) % HOUR_MILLIS_8_HOUR) / MINUTE_MILLIS_8_HOUR;//分
            //最后天数需要减去双休日的天数
            int leaveDays = Integer.parseInt(String.valueOf(between_days)) - calReleaseDays(time1, time2);//去除双休天数
            int reqDay = Integer.parseInt(String.valueOf(leaveDays));
            int reqHour = Integer.parseInt(String.valueOf(between_hous));
            int reqMin = Integer.parseInt(String.valueOf(between_mins));
            //返回的时间 , 为了获取各个时间方便，最后加上了天时分用逗号分隔，也可以不加。
            times = reqDay + "天" + reqHour + "小时" + reqMin + "分"
                    + "+" + reqDay + "," + reqHour + "," + reqMin;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 获取开始那天的请假时间
     *
     * @param time
     * @param amStartTime
     * @param amEndTime
     * @param pmStartTime
     * @param pmEndTime
     * @return
     */
    private static long getTopTime(long time, long amStartTime, long amEndTime,
                                   long pmStartTime, long pmEndTime) {
        long remainTimes = 0L;
        if (!isWeekEnd(time)) {
            if (time < amStartTime) {
                remainTimes = (amEndTime - amStartTime) + (pmEndTime - pmStartTime);
            } else if (time >= amStartTime && time <= amEndTime) {
                remainTimes = (amEndTime - time) + (pmEndTime - pmStartTime);
            } else if (time > amEndTime && time < pmStartTime) {
                remainTimes = (pmEndTime - pmStartTime);
            } else if (time >= pmStartTime && time <= pmEndTime) {
                remainTimes = pmEndTime - time;
            } else if (time > pmEndTime) {
                remainTimes = 0L;
            }
        } else {
            remainTimes = 0L;
        }
        return remainTimes;
    }

    /**
     * 获取结束那天的请假时间
     *
     * @param time
     * @param amStartTime
     * @param amEndTime
     * @param pmStartTime
     * @param pmEndTime
     * @return
     */
    private static long getTailTime(long time, long amStartTime, long amEndTime,
                                    long pmStartTime, long pmEndTime) {
        long remainTimes = 0L;
        if (!isWeekEnd(time)) {
            if (time < amStartTime) {
                remainTimes = 0L;
            } else if (time >= amStartTime && time <= amEndTime) {
                remainTimes = time - amStartTime;
            } else if (time > amEndTime && time < pmStartTime) {
                remainTimes = (amEndTime - amStartTime);
            } else if (time >= pmStartTime && time <= pmEndTime) {
                remainTimes = (amEndTime - amStartTime) + (time - pmStartTime);
            } else if (time > pmEndTime) {
                remainTimes = (amEndTime - amStartTime) + (pmEndTime - pmStartTime);
            } else {
                remainTimes = 0L;
            }
        }
        return remainTimes;
    }

    /**
     * 计算双休日的天数
     *
     * @param sTime
     * @param bTime
     * @return
     */
    private static int calReleaseDays(long sTime, long bTime) {
        int days = 0;
        List<Long> dateList = new ArrayList<Long>();
        for (long i = sTime; i < bTime; i = i + DAY_MILLIS_24_HOUR) {
            dateList.add(i);
        }
        for (Long time : dateList) {

            if (isWeekEnd(time)) {
                days++;
            }
        }
        return days;
    }

    /**
     * 判断是否是周末
     *
     * @param time
     * @return
     */
    private static boolean isWeekEnd(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        int week = calendar.get(Calendar.DAY_OF_WEEK);//返回值1 到 7，表示星期天 到 星期六
        if (week == 1 || week == 7) {
            return true;
        } else {
            return false;
        }
    }
}
