package com.zjyeshi.dajiujiao.buyer.utils;

import java.util.TimeZone;

/**
 * 时间判断类
 *
 * Created by wuhk on 2015/11/10.
 */
public class TimeUtil {
    public static final int SECONDS_IN_DAY = 60 * 60 * 24;

    public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

    public static boolean isSameDayOfMillis(final long ms1, final long ms2) {
        final long interval = ms1 - ms2;
        return interval < MILLIS_IN_DAY
                && interval > -1L * MILLIS_IN_DAY
                && toDay(ms1) == toDay(ms2);
    }
    private static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
    }

//    /**上下午方式计算天数
//     *
//     * @param startTime
//     * @param endTime
//     * @return
//     */
//    public static String calLeaveDays(String startTime , String endTime){
//        String days = "0.0";
//        if (!startTime.equals("请选择") && !endTime.equals("请选择")){
//            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//            try {
//                Date smallDate = sdf.parse(startTime.split(" ")[0]);
//                String sItem = startTime.split(" ")[1];
//                Date bigDate = sdf.parse(endTime.split(" ")[0]);
//                String bItem = endTime.split(" ")[1];
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(smallDate);
//                long time1 = cal.getTimeInMillis();
//                cal.setTime(bigDate);
//                long time2 = cal.getTimeInMillis();
//                long between_days=(time2-time1)/(1000*3600*24);
//                double leaveDays = Integer.parseInt(String.valueOf(between_days)) - calReleaseDays(time1 , time2);
//                if (sItem.equals(bItem)){
//                    leaveDays = leaveDays + 0.5;
//                }else if (sItem.equals("上午") && bItem.equals("下午")){
//                    leaveDays = leaveDays + 1;
//                }
//                days = String.format("%.1f" , leaveDays);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        return days;
//    }
//
//    /**计算间隔日期，精确到分
//     *
//     * @param startTime
//     * @param endTime
//     * @return
//     */
//    public static String calLeaveTimes(String startTime , String endTime){
//        String times = "";
//        if (!startTime.equals("请选择") && !endTime.equals("请选择")){
//            try {
//                SimpleDateFormat speSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                Date startDate = speSdf.parse(startTime);
//                Date endDate = speSdf.parse(endTime);
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(startDate);
//                long sTime = cal.getTimeInMillis();
//                cal.setTime(endDate);
//                long eTime = cal.getTimeInMillis();
//                long betweenTimes = eTime - sTime;
//                long between_days= betweenTimes/(1000*3600*24);//天
//                long between_hous = (betweenTimes%(1000*3600*24))/(1000*3600);//时
//                long between_mins = ((betweenTimes%(1000*3600*24))%(1000*3600))/(1000*60);//分
//
//                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//                Date smallDate = sdf.parse(startTime.split(" ")[0]);
//                Date bigDate = sdf.parse(endTime.split(" ")[0]);
//                cal.setTime(smallDate);
//                long time1 = cal.getTimeInMillis();
//                cal.setTime(bigDate);
//                long time2 = cal.getTimeInMillis();
//
//                int leaveDays = Integer.parseInt(String.valueOf(between_days)) - calReleaseDays(time1 , time2);//去除双休天数
//                int reqDay = Integer.parseInt(String.valueOf(leaveDays));
//                int reqHour = Integer.parseInt(String.valueOf(between_hous));
//                int reqMin = Integer.parseInt(String.valueOf(between_mins));
//                times = reqDay + "天" + reqHour + "时" + reqMin + "分"
//                        + "+" + calReleaseDays(time1 , time2) + "+" + reqDay + "," + reqHour + "," + reqMin;
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        return times;
//    }
//
//    //计算双休日
//    private static int calReleaseDays(long sTime , long bTime){
//        int days = 0;
//        List<Long> dateList = new ArrayList<Long>();
//        for (long i = sTime; i < bTime ; i = i + (1000*3600*24)){
//            dateList.add(i);
//        }
//        for (Long time : dateList){
//            if (DateUtils.getDayOfWeek(new Date(time)) == 1 || DateUtils.getDayOfWeek(new Date(time)) ==7){
//                days++;
//            }
//        }
//        return days;
//    }
}
