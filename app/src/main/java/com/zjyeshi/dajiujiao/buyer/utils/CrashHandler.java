package com.zjyeshi.dajiujiao.buyer.utils;

import android.content.Context;
import android.os.Build;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.http.BPResponse;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigapple.lib.utils.VersionUtils;
import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.task.data.ErrorInfo;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * 异常捕获
 * Created by wuhk on 2016/8/25.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler sInstance = new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return sInstance;
    }

    public void init(Context context) {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context;
    }

    /**
     * 捕获异常
     */
    @Override
    public void uncaughtException(final  Thread thread, final Throwable ex) {
        if (Constants.isDebug){
            mDefaultCrashHandler.uncaughtException(thread , ex);
        }else{
            if (!handleException(ex) && mDefaultCrashHandler != null){
                mDefaultCrashHandler.uncaughtException(thread , ex);
            }else{
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

                // 退出程序
                android.os.Process.killProcess(android.os.Process.myPid());
                //参数非零表示异常终止
                System.exit(1);
            }
        }
    }


    /**
     * 用户试图自己处理异常
     *
     * @return 处理true，不处理false
     */
    private boolean handleException(final Throwable e) {
        if (e == null) {
            return false;
        }

        final CountDownLatch latch = new CountDownLatch(1);
        new Thread() {
            @Override
            public void run() {
                uploadExceptionToServer(getErrorInfo(e));
                //执行完成之后减去计数器
                latch.countDown();
            }
        }.start();

        try {
            // 等待，直到记录错误信息的线程完成再执行
            latch.await();
//            Thread.sleep(2000);
        }
        catch (InterruptedException e1) {
            // Ignore
        }

        return false;
    }

    /**上传到服务器
     *
     * @param errorInfo
     */
    private void uploadExceptionToServer(ErrorInfo errorInfo) {

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("pk" , errorInfo.getPk());
        paramMap.put("identifier" , errorInfo.getIdentifier());
        paramMap.put("os" , errorInfo.getOs());
        paramMap.put("ver" , errorInfo.getVer());
        paramMap.put("logLevel" , errorInfo.getLogLevel());
        paramMap.put("logContent" , errorInfo.getLogContent());
        paramMap.put("phoneName" , errorInfo.getPhoneName());
        paramMap.put("osName" , errorInfo.getOsName());
        paramMap.put("osVersion" , errorInfo.getOsVersion());
        paramMap.put("ext" , errorInfo.getExt());

        BPResponse response = HttpUtil.post(mContext, UrlConstants.ADDLOG, paramMap);
        if (-1 == response.getStatusCode()) {
            ToastUtil.toast(response.getReasonPhrase());
        } else if (HttpURLConnection.HTTP_OK == response.getStatusCode()) {
            // 正常200
            NoResultData retData = JSON.parseObject(response.getResultStr(), NoResultData.class);
            if (retData.codeOk()) {
                LogUtil.e("成功了");
            } else {
                ToastUtil.toast("出错");
            }
        } else {
            // 非200的状态码
            ToastUtil.toast("返回状态码错误" + response.toString());
        }
    }

    /**获取手机信息
     *
     * @return
     */
    private void setPhoneInfo(ErrorInfo errorInfo) {
        //操作系统名称
        errorInfo.setOsName(Build.MODEL);
        //手机名称
        errorInfo.setPhoneName(Build.MANUFACTURER);

        //操作系统版本
        errorInfo.setOsVersion(Build.VERSION.RELEASE);

    }

    /**获取错误信息
     *
     * @param ex
     * @return
     */
    private ErrorInfo getErrorInfo(Throwable ex){

        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        pw.close();
        String errorStr= writer.toString();

        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setPk("ifcyGJoyp7yd4g89tU84VP7w");
        errorInfo.setIdentifier(App.instance.getPackageName());
        errorInfo.setOs("ANDROID");
        errorInfo.setLogLevel("ERROR");
        errorInfo.setLogContent(errorStr);
        errorInfo.setVer(VersionUtils.getVersionName());
        setPhoneInfo(errorInfo);
        String ext = "";
        if (!Validators.isEmpty(LoginedUser.getLoginedUser().getLoginedUser().getId())){
            ext = "用户Id:" + LoginedUser.getLoginedUser().getId();
        }
        errorInfo.setExt(ext);
        return errorInfo;
    }
}
