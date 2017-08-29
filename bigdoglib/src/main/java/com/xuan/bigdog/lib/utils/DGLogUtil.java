package com.xuan.bigdog.lib.utils;

import android.util.Log;

/**
 * 日志工具
 *
 * Created by xuan on 16/3/7.
 */
public class DGLogUtil {
    private final static String TAG = "Bigdog Say:";
    public final static boolean DEBUG = true;

    public static void d(String message){
        if(!DEBUG){
            return;
        }
        Log.d(TAG, message);
    }

    public static void e(Throwable e){
        if(!DEBUG){
            return;
        }
        Log.e(TAG, e.getMessage(), e);
    }

}
