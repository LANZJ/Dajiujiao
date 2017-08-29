package com.xuan.bigappleui.lib;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.xuan.bigappleui.lib.utils.BULogUtil;
import com.xuan.bigappleui.lib.utils.bitmap.BPBitmapLoader;

/**
 * Bigapple框架
 *
 * Created by xuan on 16/2/23.
 */
public class BigappleUI {
    public static Context application;

    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Context context) {
        if (null == context) {
            BULogUtil.e("BigappleUI can not init. Cause context is null.");
            return;
        }

        if (application instanceof Activity) {
            BULogUtil.d("BigappleUI init by Activity.");
            application = context.getApplicationContext();
        } else if (context instanceof Application) {
            BULogUtil.d("BigappleUI init by Application.");
            application = context;
        } else {
            BULogUtil.e("BigappleUI can not be init. Cause context is wrong type.");
        }

        BPBitmapLoader.init(application);
    }

    /**
     * 获取当前程序实例
     *
     * @return
     */
    public static Context getApplicationContext() {
        return application;
    }

}
