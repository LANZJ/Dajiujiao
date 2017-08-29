package com.xuan.bigdog.lib.bservice.bregister;

/**
 * 设置密码回调
 * <p/>
 * Created by xuan on 16/2/16.
 */
public interface BDSetPasswordListener {
    /**
     * 成功
     */
    void success();

    /**
     * 失败
     *
     * @param message
     */
    void fail(String message);
}
