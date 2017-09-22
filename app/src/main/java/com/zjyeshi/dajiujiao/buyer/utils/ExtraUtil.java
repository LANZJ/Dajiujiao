package com.zjyeshi.dajiujiao.buyer.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.jopool.crow.CWChat;
import com.jopool.crow.imlib.entity.CWUser;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.xuan.bigdog.lib.utils.BDActivityManager;
import com.zjyeshi.dajiujiao.buyer.activity.frame.fragment.MianFramActivity;
import com.zjyeshi.dajiujiao.buyer.activity.store.BalanceAccountsActivity;
import com.zjyeshi.dajiujiao.buyer.chat.MyMessageReceiveListener;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.common.PreferenceConstants;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.my.BigArea;
import com.zjyeshi.dajiujiao.buyer.entity.my.DetailArea;
import com.zjyeshi.dajiujiao.buyer.activity.login.LoginActivity;
import com.zjyeshi.dajiujiao.buyer.chat.MyConnectListener;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.receiver.ToLoginReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.my.UnBindBPushTask;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * 新加的工具类
 * Created by wuhk on 2016/6/27.
 */
public abstract class ExtraUtil {
    public static boolean AER=true;

    /**获取小图连接
     *
     * @param url
     * @return
     */
    public static String getResizePic(String url , int width , int height){
        String result = "";
        if (!Validators.isEmpty(url)){
            result = url + "?x-oss-process=image/resize,m_fill,h_" + height + ",w_" + width;
        }
        return result;
    }


    /**转换成两位小数
     *
     * @param num
     * @return
     */
    public static String format(float num){
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        return decimalFormat.format(num);
    }

    /**获取显示的价格，服务器价格除100
     *
     * @param price
     * @return
     */
    public static float getShowPrice(String price){

        return  Float.parseFloat(price)/100;
    }

    /**获取上传的价格，显示价格乘以100
     *
     * @param price
     * @return
     */
    public static float getUpLoadPrice(String price){

        return  Float.parseFloat(price) * 100;
    }

    /**上传数量取整,只用来去掉 .00
     *
     * @param count
     * @return
     */
    public static String getUpLoadCount(String count){
        if (Validators.isEmpty(count)){
            return "0";
        }else if(count.contains(".")){
            String[] counts = count.split("\\.");
            return counts[0];
        }else{
            return count;
        }
    }

    /**退出登录并且解绑百度推送
     *
     * @param context
     */
    public static void logoutAndUnBindPush(final Context context){
//        ToLoginReceiver toLoginReceiver = new ToLoginReceiver((Activity) context);
//        toLoginReceiver.unRegister();
        //解绑百度推送
        UnBindBPushTask unBindBPushTask = new UnBindBPushTask(context);
        unBindBPushTask.setShowProgressDialog(false);
        unBindBPushTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        unBindBPushTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
            }
        });
        unBindBPushTask.execute();
        //把请求拉取消息的标志位去掉
        BPPreferences.instance().putBoolean(PreferenceConstants.REQUEST_MSG , false);
        //退出登录相关操作
        LoginedUser.getLoginedUser().setLogined(false);
        // 同步到本地文件
        LoginedUser.saveToFile();
        //退出微信
        CWChat.getInstance().getImClient().disConnect();
        Constants.isAlive = false;
        BDActivityManager.removeAndFinishAll();
        if (AER){
           // AER=false;
        context.startActivity(new Intent(context, LoginActivity.class));}
        //退出融云
        RongIM.getInstance().disconnect();

    }

    /**连接聊天服务器广播
     *
     * @param context
     */
    public static void connectChat(final Context context){
        if (!Constants.connecting){
            Constants.connecting = true;
            LogUtil.e("正在连接");
            if (!CWChat.getInstance().getImClient().isConnected()){
                LoginedUser loginedUser = LoginedUser.getLoginedUser();
                CWUser user = new CWUser();
                user.setUserId(loginedUser.getId());
                user.setName(loginedUser.getName());
                user.setUrl(loginedUser.getPic());
                CWChat.getInstance().startWork(context, user,
                        new MyMessageReceiveListener(), new MyConnectListener());
            }else{
                Constants.connecting = false;
                LogUtil.e("已连接变false");
            }
        }
    }

    /**根据区号代码获得地区
     *
     * @param code
     * @return
     */
    public static String getAreaByCode(String code) {
        String temp[] = code.split(",");
        String districtCode = "";
        final String provinceCode = temp[0];
        final String cityCode = temp[1];
        if (temp.length == 3){
            districtCode = temp[2];
        }

        final BigArea bigArea = DaoFactory.getAreaDao().findByCode("0");
        String result= "";
        List<DetailArea> detailAreaList = bigArea.getList();
        for (DetailArea detailArea : detailAreaList){
            if (detailArea.getCode().equals(provinceCode)){
                result = detailArea.getName();
            }
        }
        BigArea bigArea1 = DaoFactory.getAreaDao().findByCode("0"+"," +  provinceCode);
        List<DetailArea> detailAreaList1 = bigArea1.getList();
        for (DetailArea detailArea : detailAreaList1){
            if (detailArea.getCode().equals(cityCode)){
                result = result  + detailArea.getName();
            }
        }

        if (Validators.isEmpty(districtCode)){
            return result;
        }else{
            BigArea bigArea2 = DaoFactory.getAreaDao().findByCode("0" + "," + provinceCode + "," +cityCode);
            List<DetailArea> detailAreaList2 = bigArea2.getList();
            for (DetailArea detailArea : detailAreaList2){
                if (detailArea.getCode().equals(districtCode)){
                    result = result  + detailArea.getName();
                }
            }
            return  result;
        }
    }

    public static String list2String(List<String> strList){
        StringBuilder result = new StringBuilder();
        if (!Validators.isEmpty(strList)){
            for (String str : strList){
                result.append(str);
                result.append(",");
            }
            result.deleteCharAt(result.length() - 1);
            return result.toString();
        }else{
            return "";
        }
    }

    public static List<String> array2List(String strArray){
        if (Validators.isEmpty(strArray)){
            return new ArrayList<String>();
        }else{
            List<String> resultList = new ArrayList<String>();
            String[] strs = strArray.split(",");
            resultList = new ArrayList(Arrays.asList(strs));
            return resultList;
        }
    }

    public static String voicefileKey(String voiceUrl){
        return FileUtil.getUrlVoiceFileName(SecurityUtil.encodeByMD5(voiceUrl) + "." + Constants.VOICE_EXT);
    }
}
