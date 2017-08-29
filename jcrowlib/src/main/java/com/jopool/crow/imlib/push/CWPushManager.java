package com.jopool.crow.imlib.push;

import android.content.Context;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.android.pushservice.PushSettings;

/**
 * 推送管理
 *
 * @author xuan
 */
public class CWPushManager {

    /**
     * 启动推送
     *
     * @param context
     * @param key
     */
    public static void startWork(Context context, String key) {
        PushManager.startWork(context, PushConstants.LOGIN_TYPE_API_KEY, key);
    }

}
