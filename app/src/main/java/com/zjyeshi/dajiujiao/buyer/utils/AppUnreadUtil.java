package com.zjyeshi.dajiujiao.buyer.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.activity.WelcomeActivity;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.NewRemind;
import com.zjyeshi.dajiujiao.buyer.entity.enums.UserEnum;
import com.zjyeshi.dajiujiao.buyer.task.work.data.NewRemindData;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;


/**
 * App角标工具类
 * Created by wuhk on 2016/8/11.
 */
public class AppUnreadUtil  {
    private final static Uri CONTENT_URI = Uri.parse("content://" + "com.android.badge" + "/" + "badge");
    private static int messageCount;
    //必须使用，Activity启动页
    private final static String getLancherActivityClassName() {
        return WelcomeActivity.class.getName();
    }

    /**
     * 发送未读数量,自己计算
     *
     * @param context
     */
    public static void sendBadgeNumber(Context context) {

        int number = Math.max(0, Math.min(getUnreadNum(), 99));
        LogUtil.e("应用未读消息数量" + number);


        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")){
            //小米
            setXiaoMiBadge(context,number);

        }else if(Build.MANUFACTURER.equalsIgnoreCase("samsung")||
                Build.MANUFACTURER.toLowerCase().contains("lg")){
            //三星
            sendToSamsumg(context, number);

        }else if (Build.MANUFACTURER.equalsIgnoreCase("sony")) {
            sendToSony(context, number);
        }else if (Build.MANUFACTURER.equalsIgnoreCase("lenovo")) {
            sendToSony(context, number);
        }else if (Build.MANUFACTURER.toLowerCase().contains("nova")) {
            setBadgeOfNova(context, number);
        }
        else if (Build.MANUFACTURER.toLowerCase().contains("htc")) {
            setBadgeOfHTC(context, number);
        }
        else {//其他原生系统手机
         //   installRawShortCut(context, MainActivity.class, isShowNum, num, isStroke);
        }
    }



    /**
     * 收到百度推送发送的改变数量的请求，使用百度推送中的数量
     *
     * @param context
     * @param count
     */
    public static void sendBageNumberUseMySet(Context context, int count) {
        int number = Math.max(0, Math.min(count, 99));
        LogUtil.e("应用未读消息数量" + number);

        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")){
            //小米
            setXiaoMiBadge(context,number);

        }else if(Build.MANUFACTURER.equalsIgnoreCase("samsung")||
                Build.MANUFACTURER.toLowerCase().contains("lg")){
            sendToSamsumg(context, number);

        }else if (Build.MANUFACTURER.toLowerCase().contains("nova")) {
            setBadgeOfNova(context, number);
        }
        else if (Build.MANUFACTURER.equalsIgnoreCase("sony")) {
            sendToSony(context, number);
        }
        else if (Build.MANUFACTURER.equalsIgnoreCase("lenovo")) {
            testreddot(context, number);
        }
        else if (Build.MANUFACTURER.toLowerCase().contains("htc")) {
            setBadgeOfHTC(context, number);
        }else {//其他原生系统手机
        //    installRawShortCut(context, MainActivity.class, isShowNum, num, isStroke);
        }
    }

    /**
     * 三星发送
     *
     * @param context
     * @param number
     */
    private static void sendToSamsumg(Context context, int number) {
       // ToastUtil.toast(number+"");
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", number);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", getLancherActivityClassName());
        context.sendBroadcast(intent);
    }
  //小米角标
    private static void setXiaoMiBadge(Context context,int number){
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("title").setContentText("text").setSmallIcon(number);
        Notification notification = builder.build();
        try {
            Field field = notification.getClass().getDeclaredField("extraNotification");
            Object extraNotification = field.get(notification);
            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
            method.invoke(extraNotification, number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mNotificationManager.notify(0, notification);
    }




    /**
     * 向索尼手机发送未读消息数广播<br/>
     * 据说：需添加权限：<uses-permission android:name="com.sonyericsson.home.permission.BROADCAST_BADGE" /> [未验证]
     * @param count
     */
    private static void sendToSony(Context context, int count){
        String launcherClassName = getLancherActivityClassName();
        if (launcherClassName == null) {
            return;
        }
        boolean isShow = true;
        if (count == 0) {
            isShow = false;
        }
        Intent localIntent = new Intent();
        localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE",isShow);//是否显示
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME",launcherClassName );//启动页
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", String.valueOf(count));//数字
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.getPackageName());//包名
        context.sendBroadcast(localIntent);
    }
    //ZUK角标
    private static void testreddot(Context context, int counts){
        Bundle extra = new Bundle();
        ArrayList<String> ids = new ArrayList<String>();
// 以列表形式传递快捷方式id，可以添加多个快捷方式id
        ids.add("custom_id_1");
        ids.add("custom_id_2");
        extra.putStringArrayList("app_shortcut_custom_id", ids);
        extra.putInt("app_badge_count", counts);
        Bundle b = null;
        b = context.getContentResolver().call(CONTENT_URI,"setAppBadgeCount", null, extra);
        boolean result = false;
        if (b != null) {
            result = true;
        }else {
            result = false;
        }
        return;
    }
    /**
     * 设置Nova的Badgy
     */
    private static void setBadgeOfNova(Context context, int count) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("tag", context.getPackageName() + "/" +
                getLancherActivityClassName());
        contentValues.put("count", count);
        context.getContentResolver().insert(Uri.parse("content://com.teslacoilsw.notifier/unread_count"),
                contentValues);
    }

    /**
     * 设置HTC的Badge
     *
     * @param context context
     * @param count   count
     */
    private static void setBadgeOfHTC(Context context, int count) {
        Intent intentNotification = new Intent("com.htc.launcher.action.SET_NOTIFICATION");
        ComponentName localComponentName = new ComponentName(context.getPackageName(),
               getLancherActivityClassName());
        intentNotification.putExtra("com.htc.launcher.extra.COMPONENT", localComponentName.flattenToShortString());
        intentNotification.putExtra("com.htc.launcher.extra.COUNT", count);
        context.sendBroadcast(intentNotification);

        Intent intentShortcut = new Intent("com.htc.launcher.action.UPDATE_SHORTCUT");
        intentShortcut.putExtra("packagename", context.getPackageName());
        intentShortcut.putExtra("count", count);
        context.sendBroadcast(intentShortcut);
    }

    /**
     * 获取角标显示数量
     *
     * @return
     */
    private static int getUnreadNum() {
        NewRemind data = NewRemind.getNewRemind();
       // int messageCount = CWChat.getInstance().getTotalUnreadNum();
        //RongIM.getInstance().get
        int allNum = 0;
        allNum += data.getFeeApplyChangeCount() + data.getLeaveUnAuditCount() +
                data.getFeeUnAuditCount() + data.getLeaveChangeCount() + data.getFeeReimbursementCount()
               + data.getOrderCount() + messageCount + data.getRebackPipeliningCount();//friendApplyCount 淡化酒友好友
        //Boss日报未读和评论未读数之和
        if (!Validators.isEmpty(data.getUnReadDailyList())) {
            for (NewRemindData.UnReadDaily unReadDaily : data.getUnReadDailyList()) {
                allNum += unReadDaily.getUnreadCount();
            }
        }
        //非boss，计算日报评论未读数量
        if (!LoginedUser.getLoginedUser().isMaxLeavel()) {
            allNum += data.getUnreadDailyCommentCount();
        }

        //非业务员，增加待收货数量，并且再增加一次订单数量（工作台里的）
        if (!LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
            allNum += data.getOrderWaitingForReceived() + data.getOrderCount();
        }
//
        return allNum;
    }
//融云未读消息条数
  public static void om(int e){
        messageCount=e;
  // ToastUtil.toast(messageCount+""+"e"+e+"");

}



}
