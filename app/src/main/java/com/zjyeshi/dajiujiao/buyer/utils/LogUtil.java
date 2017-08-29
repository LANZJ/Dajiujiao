package com.zjyeshi.dajiujiao.buyer.utils;

import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigapple.lib.utils.log.LogUtils;

/**
 * 日志工具类
 *
 * Created by xuan on 15/9/16.
 */
public class LogUtil {
    static{
        //日志前缀
        LogUtils.TAG = "dajiujiao";
    }

    /**
     * ERROR级别日志
     *
     * @param e
     */
    public static void e(Throwable e) {
        LogUtils.e(e.getMessage(), e);
    }

    /**
     * ERROR级别日志
     *
     * @param msg
     */
    public static void e(String msg) {
        if (Validators.isEmpty(msg)) {
            return;
        }
        LogUtils.e(msg);
    }

    /**
     * DEBUG级别日志
     *
     * @param content
     */
    public static void debug(String content) {
        if (Validators.isEmpty(content)) {
            return;
        }
        LogUtils.e(content);
    }

}
